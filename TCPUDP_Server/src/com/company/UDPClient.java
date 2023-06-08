package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {

    public static int port;
    public static DatagramSocket socket;
    public static InetAddress address;

    public UDPClient(int port){

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Socket Error");
        }
    }

    public void sendMsg(String msg, InetAddress address, int port){
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        try {
            socket.send(packet);
        }
        catch (IOException e){
            System.out.println("No IO");
        }
    }

    public String receiveMsg() {
        byte[] buf = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);


        try {
            socket.receive(packet);
            address = packet.getAddress();
            port = packet.getPort();
        }
        catch (IOException e){
            System.out.println("No IO");
        }

        return new String(packet.getData(),packet.getOffset(), packet.getLength());
    }

    public void close() {
        socket.close();
    }
}
