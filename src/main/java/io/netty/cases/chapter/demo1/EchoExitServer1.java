package io.netty.cases.chapter.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Created by 李林峰 on 2018/8/3.
 */
public class EchoExitServer1 {

    public static void main(String[] args) throws Exception {
        // EventLoop线程启动后不会主动退出，只有调用其shutdown方法才会退出
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);        // 监听连接事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();               // 处理IO事件
        try {
            ServerBootstrap b = new ServerBootstrap();          // server启动配置器
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });
            b.bind(18080).sync();       // 同步的方式绑定服务端端口，进行监听
        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
    }
}
