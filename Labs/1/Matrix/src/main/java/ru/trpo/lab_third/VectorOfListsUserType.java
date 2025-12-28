package ru.trpo.lab_third;

import java.util.ArrayList;
import java.util.List;

public class VectorOfListsUserType {

    public interface IntVisitor {
        void toDo(UserType value);
    }

    private final List<LinkedListBlockUserType> blocks = new ArrayList<>();

    public VectorOfListsUserType(int blocksCount) {
        for (int i = 0; i < blocksCount; i++) {
            blocks.add(new LinkedListBlockUserType());
        }
    }

    public void addLast(int blockIndex, UserType value) {
        blocks.get(blockIndex).addLast(value);
    }

    public UserType get(int blockIndex, int indexInList) {
        return blocks.get(blockIndex).get(indexInList);
    }

    public void insert(int blockIndex, int indexInList, UserType value) {
        blocks.get(blockIndex).insert(indexInList, value);
    }

    public void remove(int blockIndex, int indexInList) {
        blocks.get(blockIndex).remove(indexInList);
    }

    public void forEach(IntVisitor v) {
        for (LinkedListBlockUserType b : blocks) {
            b.forEach(v);
        }
    }

    public void lexicographicalSort() {
        for (LinkedListBlockUserType b : blocks) {
            b.lexicographicalSort();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VectorOfLists{\n");

        for (int i = 0; i < blocks.size(); i++) {
            sb.append("Block ").append(i).append(": ");
            blocks.get(i).forEach(v -> sb.append("\n").append(v).append(" "));
            sb.append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
