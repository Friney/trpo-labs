package ru.trpo.lab_third;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import ru.trpo.lab_first.Matrix;

public class Lab3Main {

    public static void main(String[] args) throws Exception {

        VectorOfListsUserType lists = new VectorOfListsUserType(3);

        URL urlInt = Lab3Main.class
                .getClassLoader()
                .getResource("int.txt");

        URL urlMatrix = Lab3Main.class
                .getClassLoader()
                .getResource("matrix.txt");

        if (urlInt == null || urlMatrix == null) {
            throw new RuntimeException("Файлы ресурсов не найдены");
        }

        // =====================================================
        System.out.println("Исходная структура (3 блока):");
        System.out.println(lists);

        printSeparator();

        // =====================================================
        System.out.println("Добавление элементов:");

        lists.addLast(0, (UserType) new IntUserType().parseValue("10"));
        lists.addLast(0, (UserType) new IntUserType().parseValue("2"));

        lists.addLast(1, (UserType) new MatrixUserType().readValue(
                new InputStreamReader(urlMatrix.openStream(), StandardCharsets.UTF_8)
        ));
        lists.addLast(1, (UserType) new MatrixUserType().parseValue(
                new Matrix(new double[][]{{1, 0}, {0, 1}}).toString()
        ));

        lists.addLast(2, (UserType) new IntUserType().readValue(
                new InputStreamReader(urlInt.openStream(), StandardCharsets.UTF_8)
        ));
        lists.addLast(2, (UserType) new MatrixUserType().parseValue(
                new Matrix(new double[][]{{4, 1}, {2, 2}}).toString()
        ));

        System.out.println(lists);

        printSeparator();

        // =====================================================
        System.out.println("Получение элемента get(1, 1):");
        UserType value = lists.get(1, 1);
        System.out.println("Результат: " + value);

        printSeparator();

        // =====================================================
        System.out.println("Вставка insert(0, 1, 100):");
        lists.insert(0, 1, (UserType) new IntUserType().parseValue("100"));
        System.out.println(lists);

        printSeparator();

        // =====================================================
        System.out.println("Удаление remove(2, 0):");
        lists.remove(2, 0);
        System.out.println(lists);

        printSeparator();

        // =====================================================
        System.out.println("Обход forEach:");
        lists.forEach(v -> System.out.print(v + " "));
        System.out.println();

        printSeparator();

        // =====================================================
        System.out.println("Лексикографическая сортировка:");
        lists.lexicographicalSort();
        System.out.println(lists);
    }

    private static void printSeparator() {
        System.out.println("\n========================\n");
    }
}
