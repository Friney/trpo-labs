package ru.trpo.rgr;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class WordFilterReader extends FilterReader {

    private final Set<String> bannedWords;
    private final Queue<Character> outputQueue = new LinkedList<>();
    private final StringBuilder wordBuffer = new StringBuilder();
    private final char maskChar;

    public WordFilterReader(Reader in, Set<String> bannedWords, char maskChar) {
        super(in);
        this.maskChar = maskChar;
        this.bannedWords = bannedWords
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public int read() throws IOException {
        if (!outputQueue.isEmpty()) {
            return outputQueue.poll();
        }

        int charInt;
        while ((charInt = in.read()) != -1) {
            char sym = (char) charInt;

            if (Character.isLetterOrDigit(sym)) {
                wordBuffer.append(sym);
            } else {
                wordFiltering();
                outputQueue.add(sym);
                return read();
            }
        }

        if (!wordBuffer.isEmpty()) {
            wordFiltering();
            return read();
        }

        return -1;
    }

    private void wordFiltering() {
        if (wordBuffer.isEmpty()) return;

        String word = wordBuffer.toString();

        outputQueue.add(word.charAt(0));
        boolean banned = bannedWords.contains(word.toLowerCase());

        for (int i = 1; i < word.length(); i++) {
            if (banned) {
                outputQueue.add(maskChar);
            } else {
                outputQueue.add(word.charAt(i));
            }
        }

        wordBuffer.setLength(0);
    }
}