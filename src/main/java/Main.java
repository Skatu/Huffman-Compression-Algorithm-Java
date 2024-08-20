import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args) {
        String word = "ABBCDDDE";

        Map<String, Integer> frequencies = getFrequencies(word);

        PriorityQueue<HeapNode<String>> heap = new PriorityQueue<>();
        frequencies.forEach((k, v) -> heap.offer(new HeapNode<>(k,v)));

        while(heap.size() > 1){
            HeapNode<String> left = heap.poll();
            HeapNode<String> right = heap.poll();
            assert right != null;
            HeapNode<String> parent = new HeapNode<>(left.value + right.value, left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            heap.offer(parent);
        }

        //build table
        HeapNode<String> node = heap.poll();
        Map<Character, Byte> bitTable = new HashMap<>();
        buildTable(node,bitTable);
    }

    private static void buildTable(HeapNode<String> node, Map<Character, Byte> bitTable) {
        if(node==null){
            return;
        }
        HeapNode<String> right = node.right;
        HeapNode<String> left = node.right;
    }
/*
    private static void buildTable(HeapNode<String> node, Map<Character, Byte> bitTable) {
        if(node==null){
            return;
        }

        BitSet s = new BitSet();
        HeapNode<String> right = node.right;
        HeapNode<String> left = node.right;
    }
*/
    private static Map<String, Integer> getFrequencies(String word) {
      Map<String, Integer> map = new HashMap<>();

      for(char c : word.toCharArray()) {
          map.compute(c+"", (k, v) -> v == null ? 1 : v + 1);
      }
      return map;
    }

    static class HeapNode<T> implements Comparable<HeapNode<T>> {
        T value;
        int frequency;
        HeapNode<T> left;
        HeapNode<T> right;

        public HeapNode(T value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(HeapNode o) {
            return Integer.compare(frequency,o.frequency);
        }
    }
}
