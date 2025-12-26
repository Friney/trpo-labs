package ru.trpo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

public class Matrix {

    private static final double EPS = 1e-7;

    private double[][] data;
    private int rows;
    private int cols;

    public Matrix() {
        this.rows = 0;
        this.cols = 0;
        this.data = new double[0][0];
    }

    public Matrix(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Invalid input, matrices must have a positive size");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] newData) {
        if (newData == null || newData.length < 1) {
            throw new IllegalArgumentException("Data can't be empty");
        }
        int newRows = newData.length;
        int newCols = newData[0].length;
        if (newCols < 1) {
            throw new IllegalArgumentException("Invalid input, matrices must have a positive size");
        }
        for (double[] datum : newData) {
            if (datum.length != newCols) {
                throw new IllegalArgumentException("All rows must have the equal number of columns");
            }
        }
        data = newData.clone();
        this.rows = newRows;
        this.cols = newCols;
        this.data = newData;
    }

    public Matrix(Matrix other) {
        checkOnExist(other);
        this.rows = other.rows;
        this.cols = other.cols;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(other.data[i], 0, this.data[i], 0, cols);
        }
    }

    public Matrix copy() {
        return new Matrix(this);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void resizeRows(int newRows) {
        if (newRows < 1) {
            throw new IllegalArgumentException("Invalid input, matrices must have a positive size");
        }
        if (newRows == this.rows) {
            return;
        }
        double[][] newData = new double[newRows][this.cols];
        int copyRows = Math.min(this.rows, newRows);
        for (int i = 0; i < copyRows; i++) {
            System.arraycopy(this.data[i], 0, newData[i], 0, this.cols);
        }
        this.rows = newRows;
        this.data = newData;
    }

    public void resizeCols(int newCols) {
        if (newCols < 1) {
            throw new IllegalArgumentException("Invalid input, matrices must have a positive size");
        }
        if (newCols == this.cols) {
            return;
        }
        double[][] newData = new double[this.rows][newCols];
        int copyCols = Math.min(this.cols, newCols);
        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.data[i], 0, newData[i], 0, copyCols);
        }
        this.cols = newCols;
        this.data = newData;
    }

    public double get(int i, int j) {
        checkIndex(i, j);
        return data[i][j];
    }

    public void set(int i, int j, double value) {
        checkIndex(i, j);
        data[i][j] = value;
    }

    public Matrix add(Matrix other) {
        checkSameShape(other);
        Matrix res = new Matrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[i][j] = this.data[i][j] + other.data[i][j];
            }
        }
        return res;
    }

    public Matrix subtract(Matrix other) {
        checkSameShape(other);
        Matrix res = new Matrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[i][j] = this.data[i][j] - other.data[i][j];
            }
        }
        return res;
    }

    public Matrix multiply(double k) {
        Matrix res = new Matrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[i][j] = this.data[i][j] * k;
            }
        }
        return res;
    }

    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("The number of columns of the first matrix is not equal to the number of rows of the second matrix");
        }
        Matrix res = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.data[i][k] * other.data[k][j];
                }
                res.data[i][j] = sum;
            }
        }
        return res;
    }

    public Matrix transpose() {
        Matrix t = new Matrix(this.cols, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                t.data[j][i] = this.data[i][j];
            }
        }
        return t;
    }

    public double determinant() {
        if (this.rows != this.cols) {
            throw new IllegalStateException("The matrix is not square");
        }
        if (rows == 1) {
            return data[0][0];
        } else if (rows == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        } else {
            double result = 0;
            for (int i = 0; i < rows; i++) {
                Matrix minor = minor(1, i + 1);
                double detMinor = minor.determinant();
                if (i % 2 == 0) {
                    result += data[0][i] * detMinor;
                } else {
                    result -= data[0][i] * detMinor;
                }
            }
            return result;
        }
    }

    public Matrix minor(int row, int column) {
        Matrix result = new Matrix(rows - 1, cols - 1);
        for (int i = 0, o = 0; i < rows; i++) {
            if (i == row - 1) {
                continue;
            }
            for (int j = 0, m = 0; j < cols; j++) {
                if (j == column - 1) {
                    continue;
                }
                result.data[o][m] = data[i][j];
                m++;
            }
            o++;
        }
        return result;
    }

    public Matrix cofactors() {
        if (this.rows != this.cols) {
            throw new IllegalStateException("The matrix is not square");
        }
        Matrix result = new Matrix(rows, cols);
        if (cols == 1) {
            double determinant = this.determinant();
            result.data[0][0] = determinant;
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Matrix minor = this.minor(i + 1, j + 1);
                    double determinant = minor.determinant();
                    if ((i + j) % 2 == 0) {
                        result.data[i][j] = determinant;
                    } else {
                        result.data[i][j] = -determinant;
                    }
                }
            }
        }
        return result;
    }

    public Matrix inverse() {
        double determinant = determinant();
        if (Math.abs(determinant) < EPS) {
            throw new IllegalStateException("Determinant can't be zero");
        }
        Matrix cofactorsT = this.cofactors().transpose();
        return cofactorsT.multiply(1.0 / determinant);
    }

    public StringWriter saveToText() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
        bufferedWriter.write(Integer.toString(rows));
        bufferedWriter.write(" ");
        bufferedWriter.write(Integer.toString(cols));
        bufferedWriter.newLine();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    bufferedWriter.write(" ");
                }
                bufferedWriter.write(Double.toString(data[i][j]));
            }
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        return stringWriter;
    }

    public static Matrix loadFromText(Reader in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(in);
        String header = bufferedReader.readLine();
        String[] StringRowsAndCols = header.trim().split("\\s+");
        int rows = Integer.parseInt(StringRowsAndCols[0]);
        int cols = Integer.parseInt(StringRowsAndCols[1]);
        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            String line = bufferedReader.readLine();
            String[] parts = line.trim().split("\\s+");
            for (int j = 0; j < cols; j++) {
                m.data[i][j] = Double.parseDouble(parts[j]);
            }
        }
        return m;
    }

    public ByteArrayOutputStream saveToBinary() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(rows);
        dataOutputStream.writeInt(cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dataOutputStream.writeDouble(data[i][j]);
            }
        }
        dataOutputStream.flush();
        return byteArrayOutputStream;
    }

    public static Matrix loadFromBinary(InputStream in) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(in);
        int rows = dataInputStream.readInt();
        int cols = dataInputStream.readInt();
        Matrix matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.data[i][j] = dataInputStream.readDouble();
            }
        }
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rows).append('x').append(cols).append('\n');
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(data[i][j]);
            }
            if (i + 1 < rows) {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix other)) {
            return false;
        }
        if (this.rows != other.rows || this.cols != other.cols) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(this.data[i][j] - other.data[i][j]) > EPS) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result = 31 * Double.hashCode(data[i][j]);
            }
        }
        return result;
    }

    private void checkIndex(int i, int j) {
        if (i < 0 || j < 0 || i >= rows || j >= cols) {
            throw new IndexOutOfBoundsException("Matrix index out of range: (" + i + ", " + j + ") for size (" + rows + ", " + cols + ")");
        }
    }

    private void checkOnExist(Matrix other) {
        if (other == null) {
            throw new IllegalArgumentException("Other matrix is null");
        }
    }

    private void checkSameShape(Matrix other) {
        checkOnExist(other);
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrix must be the same size");
        }
    }
}
