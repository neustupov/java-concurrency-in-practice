package org.neustupov;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPerTskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            new Thread(() -> handleRequest(connection)).start();
        }
    }

    private static void handleRequest(Socket connection) {

    }
}
