package cn.cyejing.dam.core.support;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;


@Slf4j
public class DataCarrier<T> {

    private static final ThreadFactory threadFactory = new DefaultThreadFactory("DataCarrier-Consumer-0", true);

    private final Thread consumerThread;

    private final List<Buffer<T>> buffers = new CopyOnWriteArrayList<>();
    private final FastThreadLocal<Buffer<T>> bufferThreadLocal = new FastThreadLocal<Buffer<T>>() {
        @Override
        protected Buffer<T> initialValue() throws Exception {
            Buffer<T> buffer = new Buffer<>(bufferSize);
            buffers.add(buffer);
            log.info("the thread {} add DataCarrier Buffer @{}", Thread.currentThread().getName(), Integer.toHexString(hashCode()));
            return buffer;
        }
    };

    private final int batchSize;
    private final int consumeCycle; //ms
    private final int bufferSize;
    private final Consumer<List<T>> consumer;

    public DataCarrier(int consumeCycle, int bufferSize, int batchSize, Consumer<List<T>> consumer) {
        this.consumeCycle = consumeCycle;
        this.bufferSize = bufferSize;
        this.batchSize = batchSize;
        this.consumer = consumer;
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


        public Buffer(int bufferSize) {
            this.buffer = new Object[bufferSize];
        }


        public boolean add(T data) {
            if (buffer[writeIndex] != null) {
                return false;
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

}
