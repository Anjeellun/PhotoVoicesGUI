/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photovoicesgui;

/**
 *
 * @author 337210040
 * Anjeli Gusnawan
 */

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("Server sedang berjalan dan menunggu koneksi...");

        while (true) {
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Klien terhubung: " + socket);

                try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
                    String fileName = dis.readUTF();

                    // Tambahkan Direktori
                    String path = "received";
                    File directory = new File(path);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    try (FileOutputStream fos = new FileOutputStream(path + "/" + fileName)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead = 0;
                        while (dis.available() > 0 && (bytesRead = dis.read(buffer)) > 0) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        System.out.println("File diterima: " + fileName);
                    }
                }
            }
        }
    }
}
