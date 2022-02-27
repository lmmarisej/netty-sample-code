package io.netty.cases.chapter.demo16;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            InputStream is = clientSocket.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
            }
            System.out.println(clientSocket.getRemoteSocketAddress() + "：" + os);
        }
    }
}
