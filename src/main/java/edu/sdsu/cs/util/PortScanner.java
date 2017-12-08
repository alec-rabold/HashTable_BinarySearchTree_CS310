package edu.sdsu.cs.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic tools for opening sockets on a host machine.
 *
 * @author Shawn Healey, San Diego State University
 * @version 1.0
 */
final class PortScanner {

    /**
     * Identifies which of ports 1-65535 are open.
     *
     * @param host the address to scan
     * @return A list of Integer objects representing the open ports found
     */
    static List<Integer> scanAllPorts(String host) {
        List<Integer> openPorts = new LinkedList<>();

        for (int curPort = 1; curPort <= 0xFFFF; curPort++) {
            if (isOpen(host, curPort)) openPorts.add(curPort);
        }
        return openPorts;
    }

    /**
     * Tests to see if processes can connect to a particular socket with a
     * default, 1000mS timeout setting.
     *
     * @param host The host number to inspect.
     * @param port the specific port number to open
     * @return true if this method successfully connected to the specified port
     */
    static boolean isOpen(String host, Integer port) {
        return isOpen(host, port, 1000);
    }

    /**
     * Tests to see if processes can connect to a particular socket.
     *
     * @param host the address to test
     * @param port the specific port number to open
     * @param mS   the timeout
     * @return true if this method successfully connected to the specified port
     */
    static boolean isOpen(String host, Integer port, int mS) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), mS);
            socket.close();
            return true;
        } catch (IOException ioE) {
            return false;
        }
    }
}
