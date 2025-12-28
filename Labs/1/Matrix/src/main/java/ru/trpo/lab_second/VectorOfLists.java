package ru.trpo.lab_second;

import java.util.ArrayList;
import java.util.List;

public class VectorOfLists {

    public interface IntVisitor {
        void toDo(int value);
    }

    private final List<LinkedListBlock> blocks = new ArrayList<>();

    public VectorOfLists(int blocksCount) {
        for (int i = 0; i < blocksCount; i++) {
            blocks.add(new LinkedListBlock());
        }
    }

    public void addLast(int blockIndex, int value) {
        blocks.get(blockIndex).addLast(value);
    }

    public int get(int blockIndex, int indexInList) {
        return blocks.get(blockIndex).get(indexInList);
    }

    public void insert(int blockIndex, int indexInList, int value) {
        blocks.get(blockIndex).insert(indexInList, value);
    }

    public void remove(int blockIndex, int indexInList) {
        blocks.get(blockIndex).remove(indexInList);
    }

    public void forEach(IntVisitor v) {
        for (LinkedListBlock b : blocks) {
            b.forEach(v);
        }
    }

    public void lexicographicalSort() {
        for (LinkedListBlock b : blocks) {
            b.lexicographicalSort();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VectorOfLists{\n");

        for (int i = 0; i < blocks.size(); i++) {
            sb.append("Block ").append(i).append(": ");
            blocks.get(i).forEach(v -> sb.append(v).append(" "));
            sb.append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
