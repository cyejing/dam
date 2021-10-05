package cn.cyejing.dam.core.support;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class DataCarrierTest {

    @Test
    @Ignore
    public void test() throws InterruptedException {
        AtomicInteger consumerCount = new AtomicInteger(0);
        DataCarrier<Data> dataCarrier = new DataCarrier<>(10, 100, 100, data -> {
            consumerCount.getAndAdd(data.size());
        });

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        AtomicInteger adder = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                executorService.submit(() -> {
                    if (dataCarrier.add(new Data(ThreadLocalRandom.current().nextInt(99999), "fun"))) {
                        adder.incrementAndGet();
                    }
                });
            }
            Thread.sleep(10);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        Thread.sleep(1000);
        System.out.println(adder.get() + ":" + consumerCount.get());
        assertEquals(adder.get(), consumerCount.get());
    }


    @Test
    @Ignore
    public void testMax() throws InterruptedException {
        AtomicInteger consumerCount = new AtomicInteger(0);
        DataCarrier<Data> dataCarrier = new DataCarrier<>(10, 100, 100, data -> {
            consumerCount.getAndAdd(data.size());
        });

        ExecutorService executorService = Executors.newFixedThreadPool(14);
        AtomicInteger adder = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                executorService.submit(() -> {
                    if (dataCarrier.add(new Data(ThreadLocalRandom.current().nextInt(99999), "fun"))) {
                        adder.incrementAndGet();
                    }
                });
            }
            Thread.sleep(10);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        Thread.sleep(1000);
        System.out.println(adder.get() + ":" + consumerCount.get());
        assertEquals(adder.get(), consumerCount.get());
    }

    @Test
    @Ignore
    public void testBigBuff() throws InterruptedException {
        AtomicInteger consumerCount = new AtomicInteger(0);
        DataCarrier<Data> dataCarrier = new DataCarrier<>(10, 1024 * 10, 1024, data -> {
            consumerCount.getAndAdd(data.size());
        });

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        AtomicInteger adder = new AtomicInteger(0);
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 1024 * 8; j++) {
                executorService.submit(() -> {
                    if (dataCarrier.add(new Data(ThreadLocalRandom.current().nextInt(99999), "fun"))) {
                        adder.incrementAndGet();
                    }
                });
            }
            Thread.sleep(10);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        Thread.sleep(1000);
        System.out.println(adder.get() + ":" + consumerCount.get());
        assertEquals(adder.get(), consumerCount.get());
    }

    public static class Data {
        private int num;
        private String name;

        public Data(int num, String name) {
            this.num = num;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "num=" + num +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
