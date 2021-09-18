package cn.cyejing.dam.common.collection;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Slf4j
public class DataCarrier<T> {

    private static final ThreadFactory threadFactory = new DefaultThreadFactory("DataCarrier-Consumer", true);

    private final Thread consumerThread;

    private final List<Buffer<T>> buffers = new CopyOnWriteArrayList<>();
    private final FastThreadLocal<Buffer<T>> bufferThreadLocal = new FastThreadLocal<Buffer<T>>() {
        @Override
        protected Buffer<T> initialValue() throws Exception {
            Buffer<T> buffer = new Buffer<>(bufferSize, rejectedStrategy);
            buffers.add(buffer);
            log.info("the thread {} add DataCarrier Buffer @{}", Thread.currentThread().getName(), Integer.toHexString(hashCode()));
            return buffer;
        }
    };

    private final int batchSize;
    private final int consumeCycle; //ms
    private final int bufferSize;
    private final Consumer<List<T>> consumer;
    private final Supplier<Boolean> rejectedStrategy;


    public DataCarrier(int consumeCycle, int bufferSize, int batchSize, Consumer<List<T>> consumer) {
        this(consumeCycle, bufferSize, batchSize, consumer, new LogStrategy());
    }


    public DataCarrier(int consumeCycle, int bufferSize, int batchSize, Consumer<List<T>> consumer, Supplier<Boolean> rejectedStrategy) {
        this.consumeCycle = consumeCycle;
        this.bufferSize = bufferSize;
        this.batchSize = batchSize;
        this.consumer = consumer;
        this.rejectedStrategy = rejectedStrategy;
        this.consumerThread = threadFactory.newThread(this::consumeData);
        this.consumerThread.start();
    }



    public boolean add(T data) {
        return bufferThreadLocal.get().add(data);
    }


    private void consumeData() {
        List<T> list = new ArrayList<>(batchSize);
        while (true) {
            try {
                Thread.sleep(consumeCycle);

                for (Buffer<T> buffer : buffers) {
                    buffer.obtain(list);
                    if (list.size() + bufferSize > batchSize) {
                        consumer.accept(list);
                        list.clear();
                        Thread.sleep(consumeCycle);
                    }
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    consumer.accept(list);
                }
            } catch (Throwable throwable) {
                log.warn("data carrier process occur error", throwable);
            } finally {
                list.clear();
            }
        }
    }

    static class Buffer<T> {
        private final Object[] buffer;
        private int writeIndex = 0;
        private int readIndex = 0;
        private final Supplier<Boolean> rejectedStrategy;


        public Buffer(int bufferSize, Supplier<Boolean> rejectedStrategy) {
            this.buffer = new Object[bufferSize];
            this.rejectedStrategy = rejectedStrategy;
        }


        public boolean add(T data) {
            if (buffer[writeIndex] != null) {
                return rejectedStrategy.get();
            } else {
                buffer[writeIndex++] = data;
                if (writeIndex == buffer.length) {
                    writeIndex = 0;
                }
                return true;
            }
        }


        @SuppressWarnings("unchecked")
        public void obtain(List<T> consumeList) {

            if (buffer[readIndex] == null) {
                return;
            }
            int start = readIndex;
            int end = writeIndex;

            int size = end - start;
            if (size <= 0) {
                size = size + buffer.length;
            }

            for (int i = 0; i < size; i++) {
                if (buffer[start] != null) {
                    consumeList.add((T) buffer[start]);
                    buffer[start] = null;
                }
                start++;
                if (start == buffer.length) {
                    start = 0;
                }
            }
            readIndex = start;
        }
    }


    public static class LogStrategy implements Supplier<Boolean> {

        @Override
        public Boolean get() {
            log.debug("buffer is full");
            return false;
        }
    }
}
