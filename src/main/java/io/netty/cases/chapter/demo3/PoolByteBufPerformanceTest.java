package io.netty.cases.chapter.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * Created by 李林峰 on 2018/8/8.
 */
public class PoolByteBufPerformanceTest {

    public static void main(String[] args) {
        poolTest();
        unPoolTest();
    }

    static void unPoolTest() {
        //非内存池模式
        long beginTime = System.currentTimeMillis();
        ByteBuf buf;
        int maxTimes = 10_000_000;
        for (int i = 0; i < maxTimes; i++) {
            buf = Unpooled.buffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : " + (System.currentTimeMillis() - beginTime));
    }

    static void poolTest() {
        //内存池模式
        long beginTime = System.currentTimeMillis();
        ByteBuf buf;
        int maxTimes = 10_000_000;
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        for (int i = 0; i < maxTimes; i++) {
            buf = allocator.heapBuffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : " + (System.currentTimeMillis() - beginTime));
    }
}