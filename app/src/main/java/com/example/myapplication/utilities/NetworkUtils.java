package com.example.myapplication.utilities;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class NetworkUtils {

    public static String getDeviceLocalIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress() &&
                            address.getHostAddress() != null &&
                            address.getHostAddress().matches("^192\\.168\\.\\d+\\.\\d+$")) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "10.0.2.2"; // Fallback to emulator IP
    }
    public static String replaceLocalhostWithDeviceIp(String originalUrl) {
        String deviceIp = NetworkUtils.getDeviceLocalIpAddress();
        return originalUrl.replace("localhost", deviceIp);
    }
}