/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photovoicesgui;

/**
 *
 * @author 3337210040
 * Anjeli Gusnawan
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private JFrame frame;
    private JButton btnSelectFile;
    private JButton btnSend;
    private JLabel lblSelectedFile;
    private JFileChooser fileChooser;
    private File selectedFile;
    private Socket socket;

    public static void main(String[] args) {
        Client client = new Client();
        client.createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Pengirim Berkas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(4, 1));

        lblSelectedFile = new JLabel("Berkas terpilih: Tidak ada");
        lblSelectedFile.setFont(new Font("Roboto", Font.BOLD, 14));
        lblSelectedFile.setForeground(Color.WHITE);
        lblSelectedFile.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblSelectedFile);

        btnSelectFile = new JButton("Pilih berkas");
        btnSelectFile.setFont(new Font("Helvetica", Font.PLAIN, 14));
        btnSelectFile.setBackground(Color.WHITE);
        btnSelectFile.addActionListener(new SelectFileListener());
        panel.add(btnSelectFile);

        btnSend = new JButton("Kirim");
        btnSend.setFont(new Font("Helvetica", Font.PLAIN, 14));
        btnSend.setBackground(Color.WHITE);
        btnSend.addActionListener(new SendListener());
        btnSend.setEnabled(false);
        panel.add(btnSend);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        frame.add(panel);
        frame.setVisible(true);
    }

    private class SelectFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                lblSelectedFile.setText("Berkas terpilih: " + selectedFile.getName());
                btnSend.setEnabled(true);
            }
        }
    }

    private class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                socket = new Socket("localhost", 9999);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // Kirim nama berkas ke server
                dos.writeUTF(selectedFile.getName());

                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    String path = "sent";
                    File directory = new File(path);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    File outputFile = new File(path + File.separator + selectedFile.getName());
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, bytesRead);
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    System.out.println("Berkas terkirim: " + selectedFile.getName());
                }

                dos.flush();
                dos.close();
                socket.close();

                JOptionPane.showMessageDialog(frame, "Berkas berhasil terkirim!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Tidak dapat mengirim berkas: " + ex.getMessage());
            }
        }
    }
}
