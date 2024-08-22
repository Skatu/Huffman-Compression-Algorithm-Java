import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

import java.util.PriorityQueue;

public class Main {
    private static final BiMap<Character, String> bitTable = HashBiMap.create();

    public static void main(String[] args) {
        String word = "ABBCDDDE";
        String encoded=encode(word);
        String decoded=decode(encoded);

        System.out.println("Original: "+word);
        System.out.println("Encoded: "+encoded);

        System.out.println("Decoded:"+decoded);
    }

    private static String encode(String str){
        Map<String, Integer> frequencies = getFrequencies(str);
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

        HeapNode<String> node = heap.poll();
        buildTable(node,bitTable,"");
        StringBuilder result = new StringBuilder();

        for(Character c : str.toCharArray()){
            result.append(bitTable.get(c));
        }
        return result.toString();
    }

    private static void buildTable(HeapNode<String> node, Map<Character, String> bitTable, String bits) {
        if(node==null){
            return;
        }
        HeapNode<String> right = node.right;
        HeapNode<String> left = node.left;

        if(node.value.length()==1){
            bitTable.put(node.value.charAt(0),bits);
        }
        buildTable(left,bitTable,bits+"0");
        buildTable(right,bitTable,bits+"1");
    }

    private static Map<String, Integer> getFrequencies(String word) {
      Map<String, Integer> map = new HashMap<>();

      for(char c : word.toCharArray()) {
          map.compute(c+"", (k, v) -> v == null ? 1 : v + 1);
      }
      return map;
    }

    private static String decode(String encoded){
        BiMap<String, Character> inversedBitTable = bitTable.inverse();
        StringBuilder decoded =new StringBuilder();
        int i=0;
        StringBuilder bits= new StringBuilder();
        while(i<encoded.length()) {
            bits.append(encoded.charAt(i++));
            if(inversedBitTable.containsKey(bits.toString())){
                decoded.append(inversedBitTable.get(bits.toString()));
                bits.setLength(0);
            }
        }
        return decoded.toString();
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
