package ru.trpo.lab_third;

import java.io.IOException;
import java.io.InputStreamReader;
import ru.trpo.lab_first.Matrix;

public class MatrixUserType implements UserType {

    private Matrix value;

    public MatrixUserType() {
        this.value = new Matrix(1, 1);
    }

    public MatrixUserType(Matrix m) {
        this.value = new Matrix(m);
    }

    @Override
    public String typeName() {
        return "Matrix";
    }

    @Override
    public Object create() {
        return new MatrixUserType();
    }

    @Override
    public Object clone() {
        return new MatrixUserType(this.value);
    }

    @Override
    public Object readValue(InputStreamReader in) throws IOException {
        this.value = Matrix.loadFromText(in);
        return this;
    }

    @Override
    public Object parseValue(String ss) {
        try {
            String[] lines = ss.trim().split("\\R");

            String[] dims = lines[0].split("x");
            int rows = Integer.parseInt(dims[0].trim());
            int cols = Integer.parseInt(dims[1].trim());

            double[][] data = new double[rows][cols];

            for (int i = 0; i < rows; i++) {
                String[] parts = lines[i + 1].trim().split("\\s+");
                for (int j = 0; j < cols; j++) {
                    data[i][j] = Double.parseDouble(parts[j]);
                }
            }

            this.value = new Matrix(data);
            return this;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Cannot parse Matrix from toString() format", e
            );
        }
    }

    @Override
    public Comparator getTypeComparator() {
        return (o1, o2) -> {
            Matrix m1 = ((MatrixUserType) o1).value;
            Matrix m2 = ((MatrixUserType) o2).value;

            int rows1 = m1.getRows();
            int cols1 = m1.getCols();
            int rows2 = m2.getRows();
            int cols2 = m2.getCols();

            int minRows = Math.min(rows1, rows2);
            int minCols = Math.min(cols1, cols2);

            for (int i = 0; i < minRows; i++) {
                for (int j = 0; j < minCols; j++) {
                    double a = m1.get(i, j);
                    double b = m2.get(i, j);
                    int cmp = Double.compare(a, b);
                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }

            if (rows1 != rows2) {
                return Integer.compare(rows1, rows2);
            }
            return Integer.compare(cols1, cols2);
        };
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
