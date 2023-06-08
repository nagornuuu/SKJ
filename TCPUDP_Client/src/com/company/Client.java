package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {


    public static void main(String[] args) throws IOException {

        Socket socket = null;
        PrintWriter out = null;
        String address = "172.20.10.3";
        int port = 1234;
        int clientPort = 8080;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), 500);
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host");
            System.exit(-1);
        }
        catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(-1);
        }

        out.println("105425");
        UDPClient client = new UDPClient(clientPort);
        out.println(address + ":" + clientPort);        // send the address and port of the client to the server

        int sum = 0;
        for (int i = 0; i < 5; i++) {           // 5 numbers
            String num = client.receiveMsg();   // receive number
            sum += Integer.parseInt(num);       // add to sum
            System.out.println(sum);            // print sum
        }
        client.sendMsg(String.valueOf(sum), UDPClient.address, UDPClient.port); // send sum to server

        String numToDelete2 = client.receiveMsg();                      // receive number to delete 2
        System.out.println(numToDelete2);                               // print number
        String numNo2 = numToDelete2.replace("2", "");  // delete 2
        System.out.println(numNo2);                                     // print number without 2
        client.sendMsg(numNo2, UDPClient.address, UDPClient.port);      // send number without 2 to server

        int numToCon = Integer.parseInt(client.receiveMsg());           // receive number to convert
        System.out.println(numToCon);                                   // print number

        String conNums ="";                                            // string to store converted number
        for (int i = 0; i < 3; i++) {                                  // convert 3 times
            conNums += String.valueOf(numToCon);                       // add number to string
            System.out.println(conNums);                               // print string
        }

        client.sendMsg(conNums, UDPClient.address, UDPClient.port);   // send converted number to server
        String flag = client.receiveMsg();                            // receive flag
        System.out.println("port z ktorego komunikuje: " + flag);     // print flag

        client.sendMsg(String.valueOf(clientPort), UDPClient.address, UDPClient.port);
        String portFromServer = client.receiveMsg();
        System.out.println("Flaga: " + portFromServer);

        client.close();
        socket.close();

    }
}
