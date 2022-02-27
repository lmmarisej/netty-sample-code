package io.netty.cases.chapter.demo4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.DefaultPromise;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * Created by 李林峰 on 2018/8/11.
 */
public class HttpClient {

    private Channel channel;
    HttpClientHandler handler = new HttpClientHandler();

    private void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new HttpClientCodec());
                ch.pipeline().addLast(new HttpObjectAggregator(Short.MAX_VALUE));
                ch.pipeline().addLast(handler);
            }
        });
        ChannelFuture f = b.connect(host, port).sync();
        channel = f.channel();
    }

    private HttpResponse blockSend(FullHttpRequest request) throws InterruptedException, ExecutionException {
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        DefaultPromise<HttpResponse> respPromise = new DefaultPromise<>(channel.eventLoop());       // 一个Channel只能被特定的EventLoop执行
        handler.setRespPromise(respPromise);
        channel.writeAndFlush(request);
        HttpResponse response = respPromise.get();      // 阻塞等待服务端响应
        if (response != null)
            System.out.print("The client received http response, the body is :" + new String(response.body()));
        return response;
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1", 18084);
        ByteBuf body = Unpooled.wrappedBuffer("Http message!".getBytes(StandardCharsets.UTF_8));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                "http://127.0.0.1/user?id=10&addr=Ninja", body);
        HttpResponse response = client.blockSend(request);
    }
}
