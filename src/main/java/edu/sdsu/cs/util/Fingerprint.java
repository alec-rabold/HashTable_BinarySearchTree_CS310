package edu.sdsu.cs.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Provides a selection of methods describing the individual machine's
 * characteristics.
 *
 * @author Shawn Healey, San Diego State University
 * @version 1.0
 */
public final class Fingerprint {

    private static final int ACTIVITY_TIMEOUT_SEC = 15;

    /**
     * Creates a listing of some of the host computer's characteristics.
     *
     * @return A List of String objects representing the human-readable output
     */
    public static List<String> getFingerprint() {
        List<String> buffer = new LinkedList<>();

        buffer.add(LocalDateTime.now().toString());

        buffer.add(OutputStrings.simpleDivider());
        buffer.add(describeUser());
        buffer.add(describeJava());

        buffer.add(OutputStrings.simpleDivider());
        buffer.add(describeOs());
        buffer.add(describeSystem());

        buffer.add(OutputStrings.simpleDivider());
        buffer.addAll(describeNetworkInterfaces());

        buffer.add(OutputStrings.simpleDivider());
        buffer.addAll(describeNetworkMachines());

        buffer.add(OutputStrings.simpleDivider());
        buffer.addAll(describeOpenPorts());
        buffer.add(OutputStrings.simpleDivider());

        buffer.add(LocalDateTime.now().toString());
        return buffer;
    }

    private static String describeUser() {
        return String.format("User Name : %-30s \nUser Home : %-65s \nWork "
                + "Dir  : %-65s", System.getProperty("user.name", "not " +
                "specified"), System.getProperty("user.home", "not " +
                "specified"), System.getProperty("user.dir", "not specified"));
    }

    private static String describeJava() {
        return String.format("Java Home : %-65s\nJava Ver  : %-20s", System
                .getProperty("java.home", "not specified"), System
                .getProperty("java.version", "not specified"));
    }

    private static String describeOs() {
        return (String.format("Current OS  : %s(%s)-%-20s", System
                .getProperty("os.name", "not specified"), System.getProperty
                ("os.arch", "not specified"), System.getProperty("os" + "" +
                ".version", "not specified")));
    }

    private static String describeSystem() {
        return (String.format("Processors  : %d \nFree Memory : %s \nMax " +
                "Memory  : %s", Runtime.getRuntime().availableProcessors(),
                NumberFormat.getInstance().format(Runtime.getRuntime()
                        .freeMemory()), NumberFormat.getInstance().format
                        (Runtime.getRuntime().maxMemory())));
    }

    private static List<String> describeNetworkInterfaces() {
        List<String> buffer = new LinkedList<>();
        for (InetAddress address : getConnectedNetworkAddresses()) {
            buffer.add(String.format("%s : %s", address.getCanonicalHostName
                    (), getFormattedAddress(getHardwareAddress(address))));
        }
        return buffer;
    }

    private static List<String> describeNetworkMachines() {
        List<String> buffer = new LinkedList<>();
        Deque<String> addresses = new LinkedList<>(getNetworkIPs());
        StringBuilder sb = new StringBuilder();
        int cursor = 0;
        while (!addresses.isEmpty()) {
            sb.append(String.format("%15s ", addresses.poll()));
            cursor++;
            if (cursor > 4) {
                cursor = 0;
                buffer.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        return buffer;
    }

    private static List<String> describeOpenPorts() {
        List<String> buffer = new LinkedList<>();

        List<Integer> openPorts = PortScanner.scanAllPorts("localhost");
        for (Integer port : openPorts) {
            buffer.add("Open Port : " + port);
        }
        return buffer;
    }

    private static List<InetAddress> getConnectedNetworkAddresses() {
        List<InetAddress> output = new LinkedList<>();

        Enumeration<NetworkInterface> networks;
        try {
            networks = NetworkInterface.getNetworkInterfaces();
            while (networks != null && networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();

                Enumeration ee = network.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    output.add(i);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return output;
    }

    private static String getFormattedAddress(byte[] address) {
        if (address == null) return "";

        StringBuilder sb = new StringBuilder();
        for (int curPart = 0; curPart < address.length; curPart++) {
            sb.append(String.format("%02X%s", address[curPart], curPart ==
                    address.length - 1 ? "" : "-"));
        }
        return sb.toString();
    }

    private static byte[] getHardwareAddress(InetAddress address) {
        NetworkInterface network;
        try {
            network = NetworkInterface.getByInetAddress(address);
            return network.getHardwareAddress();

        } catch (SocketException e) {
            return address.getAddress();
        }
    }

    private static List<String> getNetworkIPs() {

        // We want ordered keys, so we use a tree map to get that automatically
        Map<String, Integer> activeMachines = new TreeMap<>();

        for (InetAddress curNetwork : getConnectedNetworkAddresses()) {
            activeMachines.putAll(getActivityCounts(curNetwork.getAddress()));
        }

        return new ArrayList<>(activeMachines.keySet());
    }

    private static Map<String, Integer> getActivityCounts(final byte[] ip) {
        final Map<String, Integer> counts = new HashMap<>();

        if (ip[0] == 127) {
            counts.put("127.0.0.*", 1);
        } else {
            List<Thread> threadsToStart = new LinkedList<>();
            for (int i = 1; i < 255; i++) {
                final byte hostNumber = (byte) i;
                threadsToStart.add(new Thread(() -> listenForActivity(ip,
                        counts, hostNumber)));
            }
            startAndWaitForThreads(threadsToStart);
        }
        return counts;
    }

    private static void listenForActivity(byte[] ip, Map<String, Integer>
            counts, byte j) {
        try {
            ip[3] = j;
            InetAddress address = InetAddress.getByAddress(ip);
            String output = address.toString().substring(1);
            if (address.isReachable(ACTIVITY_TIMEOUT_SEC * 1000)) {
                synchronized (counts) {
                    if (counts.containsKey(output)) {
                        counts.put(output, counts.get(output) + 1);
                    } else counts.put(output, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startAndWaitForThreads(List<Thread> toExecute) {

        ExecutorService executor = Executors.newFixedThreadPool(32);

        for (Thread cur : toExecute)
            executor.submit(cur);
        executor.shutdown();
        try {
            executor.awaitTermination(ACTIVITY_TIMEOUT_SEC, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
