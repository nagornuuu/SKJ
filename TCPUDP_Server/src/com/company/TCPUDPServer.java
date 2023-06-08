package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPUDPServer {

    public static class ServerThread extends Thread {
        private final Socket socket;

        public ServerThread(Socket socket) {
            super();
            this.socket = socket;
        }

        public void run() {

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                int flag = Integer.parseInt(in.readLine());
                if (flag == 105425) {
                    String client = in.readLine();
                    String ip = client.split(":")[0];
                    int port = Integer.parseInt(client.split(":")[1]);
                    System.out.println(client);

                    UDPClient udpClient = new UDPClient(5555);
                    int numbers[] = {100, 200, 300, 400, 500};
                    int sum = 0;
                    for (int x : numbers) {
                        udpClient.sendMsg(String.valueOf(x), InetAddress.getLocalHost(), port); // send numbers to client
                        sum += x; // sum = sum + x;
                    }

                    String answer = udpClient.receiveMsg(); // receive sum from client
                    String stringSum = String.valueOf(sum); // convert sum to string

                    if (stringSum.equals(answer)) {         // check if sum is correct
                        int UpNumber = 294850342;           // number to convert
                        udpClient.sendMsg(String.valueOf(UpNumber), InetAddress.getLocalHost(), port);       // send number to client
                        String UpNumberNo2 = String.valueOf(UpNumber).replace("2", "");     // remove 8 from number
                        String answer2 = udpClient.receiveMsg();                                            // receive number without 8 from client
                        if (answer2.equals(UpNumberNo2)) {                                                  // check if number without 8 is correct
                            int numToCon = 2137;                                                            // number to convert
                            udpClient.sendMsg(String.valueOf(numToCon), InetAddress.getLocalHost(), port);  // send number to client
                            String conNums = "";                                                            // string to store converted number
                            for (int i = 0; i < 3; i++) {                                                   // receive 5 numbers from client
                                conNums += String.valueOf(numToCon);                                        // add number to string
                            }
                            String answer3 = udpClient.receiveMsg();                                        // receive converted number from client
                            client = InetAddress.getLocalHost().getHostAddress() + ":" + 5555;              // get client address
                            udpClient.sendMsg(client, InetAddress.getLocalHost(), port);                    // send port to client


                            if (conNums.equals(answer3)) {
                                udpClient.sendMsg("Koniec zadania, ostatnia flaga -> 244466666", InetAddress.getLocalHost(), port);
                            } else {
                                udpClient.sendMsg("Wrong: " + answer3 + " should be " + conNums, InetAddress.getLocalHost(), port);
                            }
                        } else {
                            udpClient.sendMsg("Wrong: " + answer2, InetAddress.getLocalHost(), port);
                        }
                    } else {
                        udpClient.sendMsg("Wrong: " + answer, InetAddress.getLocalHost(), port);
                    }
                    udpClient.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void listenSocket(int port) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server listens on port: " + server.getLocalPort());

        while (true) {
            Socket client = null;
            try {
                client = server.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ServerThread serverThread = new ServerThread(client);
            serverThread.start();
        }
    }


    public static void main(String[] args) {
        int port = 1234;

        TCPUDPServer server = new TCPUDPServer();
        server.listenSocket(port);

    }

}

