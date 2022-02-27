package io.netty.cases.chapter.demo16;

import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        String body;
        for (int count = 0; true; ++count) {
            try (Socket socket = new Socket("", 9999); OutputStream outputStream = socket.getOutputStream()) {
                body = "你好" + count;
                System.out.println("I send to Server message：" + body);
                outputStream.write(body.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
    }
}
