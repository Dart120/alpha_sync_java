package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.Connector;
import org.alpha_sync_java.domain.utils.StreamUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class MulticastConnector implements Loggable, Connector {
    MulticastSocket socket;

    public MulticastConnector(String address, String port) throws IOException {
        InetAddress group = InetAddress.getByName(address);
        int portNum = Integer.parseInt(port);

        Stream<NetworkInterface> possibleInterfaces = findMulticastInterface();
        socket = new MulticastSocket(portNum);
        possibleInterfaces
                .map(trialInterface -> {
                    try {
                        socket.joinGroup(new InetSocketAddress(group, portNum), trialInterface);
                        return Optional.of(trialInterface);
                    } catch (Exception e) {
                        return Optional.<NetworkInterface>empty();
                    }
                })
                .flatMap(Optional::stream)
                .forEach((usedInterface) -> logger().info("Using Interface :{}", usedInterface.getDisplayName()));
    }


    @Override
    public void listen(String port, Function<String, Boolean> callback) throws Exception {
            final byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket;
            while(true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(),0,receivePacket.getLength());
                logger().info("Recieved message " + clientMessage);
                if (callback.apply(clientMessage)){
                    break;
                }
            }
    }

    @Override
    public void send(String address, String port, String message) throws Exception {
            DatagramPacket sendPacket;
            byte[] sendData;
            socket.setSoTimeout(0);
            sendData = message.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(address), Integer.parseInt(port));
            socket.send(sendPacket);
            logger().info("Sent message " + message);

    }

    private Stream<NetworkInterface> findMulticastInterface() throws SocketException {
        List<NetworkInterface> result = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces =  NetworkInterface.getNetworkInterfaces();
        return StreamUtils.of(interfaces).filter((currInterface) -> {
            try {
                return currInterface.supportsMulticast() && currInterface.isUp() && !currInterface.isLoopback();
            } catch (SocketException e) {
                return false;
            }
        });
    }
}
