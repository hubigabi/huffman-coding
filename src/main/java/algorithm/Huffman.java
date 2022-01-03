package algorithm;

import java.util.*;

public class Huffman {

    public static Map<Byte, String> getHuffmanCoding(Map<Byte, Integer> byteFrequency) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(byteFrequency.size(),
                Comparator.comparingInt(HuffmanNode::getFrequency));

        for (Map.Entry<Byte, Integer> entry : byteFrequency.entrySet()) {
            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.setValue(entry.getKey());
            huffmanNode.setFrequency(entry.getValue());
            huffmanNode.setLeafNode(true);
            priorityQueue.add(huffmanNode);
        }

        HuffmanNode rootNode = new HuffmanNode();
        while (priorityQueue.size() > 1) {
            HuffmanNode leftNode = priorityQueue.poll();
            HuffmanNode rightNode = priorityQueue.poll();

            HuffmanNode mergedNode = new HuffmanNode();
            mergedNode.setFrequency(leftNode.getFrequency() + rightNode.getFrequency());
            mergedNode.setLeafNode(false);
            mergedNode.setLeftNode(leftNode);
            mergedNode.setRightNode(rightNode);

            rootNode = mergedNode;
            priorityQueue.add(mergedNode);
        }

        Map<Byte, String> byteCoding = new HashMap<>();
        fillCodingMap(rootNode, "", byteCoding);
        return byteCoding;
    }

    private static void fillCodingMap(HuffmanNode node, String s, Map<Byte, String> byteCoding) {
        if (node.getLeftNode() == null && node.getRightNode() == null && node.isLeafNode()) {
            byteCoding.put(node.getValue(), s);
            return;
        }

        fillCodingMap(node.getLeftNode(), s + "0", byteCoding);
        fillCodingMap(node.getRightNode(), s + "1", byteCoding);
    }
}