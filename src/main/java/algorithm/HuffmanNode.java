package algorithm;

import lombok.Data;

@Data
class HuffmanNode {

    private int frequency;
    private byte value;
    private boolean leafNode;

    private HuffmanNode leftNode;
    private HuffmanNode rightNode;
}
