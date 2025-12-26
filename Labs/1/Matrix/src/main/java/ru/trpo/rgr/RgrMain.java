package ru.trpo.rgr;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Set;

public class RgrMain {

    public static void main(String[] args) throws Exception {
        printSeparator();
        String text1 = "Я поставлю два, я поставлю три, я поставлю четыре, я поставлю пять!";
        Reader reader1 = new WordFilterReader(
                new StringReader(text1),
                Set.of("два", "три", "четыре"),
                '*'
        );

        print(reader1);
        printSeparator();

        URL url = RgrMain.class
                .getClassLoader()
                .getResource("input.txt");

        assert url != null;
        Reader reader2 = new WordFilterReader(
                new FileReader(url.getPath()),

                Set.of("плохой", "ужасный"),
                '*'
        );

        print(reader2);
        printSeparator();

        String text3 = "Решётка, Градус, Равно";
        Reader reader3 = new WordFilterReader(
                new WordFilterReader(
                        new WordFilterReader(
                                new StringReader(text3),
                                Set.of("Градус"),
                                '°'
                        ),
                        Set.of("решётка"),
                        '#'
                ),
                Set.of("Равно"),
                '='
        );

        print(reader3);
        printSeparator();
    }

    private static void print(Reader reader) throws Exception {
        int c;
        while ((c = reader.read()) != -1) {
            System.out.print((char) c);
        }
        System.out.println();
    }


    private static void printSeparator() {
        System.out.println("\n========================\n");
    }

}
