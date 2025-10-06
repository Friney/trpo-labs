package ru.trpo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class Main {

    public static void main(String[] args) {
        Matrix matrix = new Matrix(
                new double[][]{
                        {2, 5, 7},
                        {6, 3, 4},
                        {5, -2, -3}
                }
        );

        System.out.println("Исходная матрица (3x3):\n" + matrix);

        printSeparator();

        System.out.println("Транспонированная матрица:\n" + matrix.transpose());

        printSeparator();

        double det = matrix.determinant();
        System.out.println("Определитель: " + det + "\n");

        Matrix inv = matrix.inverse();
        System.out.println("Обратная матрица:\n" + inv + "\n");

        // matrix * inverse = единичная
        Matrix identityCheck = matrix.multiply(inv);
        System.out.println("Проверка обратной (должно быть единичная):\n" + identityCheck + "\n");

        printSeparator();

        System.out.println("Матрица * 2:\n" + matrix.multiply(2) + "\n");

        printSeparator();

        Matrix resized = matrix.copy();
        resized.resizeRows(2);
        resized.resizeCols(2);
        System.out.println("После resizeRows(2) и resizeCols(2):\n" + resized + "\n");

        printSeparator();

        Matrix minor = matrix.minor(2, 2);
        System.out.println("Минор (2,2):\n" + minor);

        printSeparator();

        try {
            StringWriter stringWriter = matrix.saveToText();
            System.out.println("Сохранено в текст:\n" + stringWriter);

            Matrix fromText = Matrix.loadFromText(new StringReader(stringWriter.toString()));
            System.out.println("Загружено из текста:\n" + fromText);
        } catch (IOException e) {
            System.err.println("Ошибка при работе с текстовым форматом: " + e.getMessage());
        }

        printSeparator();

        try {
            ByteArrayOutputStream byteArrayOutputStream = matrix.saveToBinary();
            System.out.println("Сохранено в бинарном:");
            for (byte b : byteArrayOutputStream.toByteArray()) {
                System.out.print(b + " ");
            }
            System.out.println();
            System.out.println();

            Matrix fromBin = Matrix.loadFromBinary(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            System.out.println("Загружено из бинарного:\n" + fromBin);
        } catch (IOException e) {
            System.err.println("Ошибка при работе с бинарным форматом: " + e.getMessage());
        }
    }

    private static void printSeparator() {
        System.out.println("\n========================\n");
    }


}
