package edu.sdsu.cs;

import edu.sdsu.cs.datastructures.BinarySearchTree;

import java.util.*;
import java.io.*;

/**
 * @author ALEC RABOLD, cssc0185
  */

public class Assign4 {
    public static void main(String[] args) {
        HashMap<String, Integer> substringsCountMap = new HashMap<>();
        BinarySearchTree<String, Integer> substringsCountTree = new BinarySearchTree<>();
        try {
            String f1 = args[0];
            String f2 = args[1];

            // Read in
            File inFile = new File(f1);
            FileReader fileReader = new FileReader(inFile);
            BufferedReader in = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;

            // Write out
            PrintWriter writer = new PrintWriter(f2, "UTF-8");

            // No trim() because spaces are valid characters
            while ((line = in.readLine()) != null) {
                for(int i = 0; i < line.length(); i++) {
                    for (int j = 0; j < 5; j++) {
                        if((i + j + 1) > line.length()) break;
                        String sub = line.substring(i, i + j + 1);
                        if (substringsCountTree.contains(sub))
                            substringsCountTree.add(sub, substringsCountTree.getValue(sub) + 1);
                        else
                            substringsCountTree.add(sub, 1);
                    }
                }
            }
            fileReader.close();

            Iterator keys = substringsCountTree.keys();
            Iterator values = substringsCountTree.values();

            List<PriorityQueue<Pair>> queues = new ArrayList<>();
            for(int i = 0; i < 5; i++) {
                queues.add(new PriorityQueue<>());
            }
            while(keys.hasNext()) {
                Integer val = (Integer)values.next();
                String key = (String)keys.next();
                queues.get(key.length() - 1).add(new Pair(val, key));
            }
            for(int i = 0; i < 5; i++) {
                writer.println(("Size " + (i+1) + ": "));
                writer.println("----------------------");
                int maxElem = Math.min(20,queues.get(i).size());
                for (int j = 0; j < maxElem; j++) {
                    Pair cur = queues.get(i).poll();
                    writer.println(cur.subs + ", " + cur.numOcc);
                }
                writer.println();
            }

            writer.close();
        } catch (IOException e) {
            // e.printStackTrace();  Security vulnerability to printStackTrack to the "consumer"
            System.out.println("..Something happened. Try again?");
        }
    }

    private static class Pair implements Comparable<Pair>{
        private Integer numOcc;       // (Value)
        private String subs;    // (Key)
        protected Pair(Integer numOcc, String subs) {
            this.numOcc = numOcc;
            this.subs = subs;
        }

        // To get higher values placed at the top of priority queue
        @Override
        public int compareTo(Pair b) {
            int res = b.numOcc.compareTo(this.numOcc);
            return res;
        }
    }

}
