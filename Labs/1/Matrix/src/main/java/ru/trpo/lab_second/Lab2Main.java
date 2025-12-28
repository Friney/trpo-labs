package ru.trpo.lab_second;


public class Lab2Main {
    public static void main(String[] args) {
        VectorOfLists lists = new VectorOfLists(3);
        System.out.println("Исходная структура (3 блока):");
        System.out.println(lists);
        printSeparator();
        System.out.println("Добавление элементов:");
        lists.addLast(0, 10);
        lists.addLast(0, 2);
        lists.addLast(1, 21);
        lists.addLast(1, 1);
        lists.addLast(2, 11);
        lists.addLast(2, 3);
        System.out.println(lists);
        printSeparator();
        System.out.println("Получение элемента get(1, 1):");
        int value = lists.get(1, 1);
        System.out.println("Результат: " + value);
        printSeparator();
        System.out.println("Вставка insert(0, 1, 100):");
        lists.insert(0, 1, 100);
        System.out.println(lists);
        printSeparator();
        System.out.println("Удаление remove(2, 0):");
        lists.remove(2, 0);
        System.out.println(lists);
        printSeparator();
        System.out.println("Обход forEach:");
        lists.forEach(v -> System.out.print(v + " "));
        System.out.println();
        printSeparator();
        System.out.println("Лексикографическая сортировка:");
        lists.lexicographicalSort();
        System.out.println(lists);
    }

    private static void printSeparator() {
        System.out.println("\n========================\n");
    }
}