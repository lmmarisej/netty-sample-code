package io.netty.cases.chapter.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Logger;

/**
 * Created by 李林峰 on 2018/8/3.
 */
public class EchoExitServer2 {

    static Logger logger = Logger.getLogger(EchoExitServer2.class.getName());

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    })
                    .bind(18080)        // 返回ChannelFuture
                    .sync()
                    .channel()
                    .closeFuture()
                    // ChannelFuture主要功能之一：提供注册异步IO执行结果的回调
                    // await：ChannelFuture主要功能之二，调用线程同步等待执行结果
                    // 监听NioServerSocketChannel的关闭事件，并同步阻塞main函数
                    .addListener((ChannelFutureListener) future -> {
                        // 业务逻辑处理代码，此处省略...
                        logger.info(future.channel().toString() + " 链路关闭");
                    });
        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
    }
}
