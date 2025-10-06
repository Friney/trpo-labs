package ru.trpo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatrixTest {

    private Matrix createTestMatrix() {
        Matrix matrix = new Matrix(3, 3);
        double[][] values = {
                {2, 5, 7},
                {6, 3, 4},
                {5, -2, -3}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix.set(i, j, values[i][j]);
            }
        }
        return matrix;
    }

    @Test
    public void testWrongConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(-7, -1));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        Matrix matrix = createTestMatrix();
        Matrix matrixCopy = new Matrix(matrix);
        assertEquals(matrix, matrixCopy);
        assertEquals(3, matrixCopy.getCols());
        assertEquals(3, matrixCopy.getRows());
    }

    @Test
    public void testCopyMethod() {
        Matrix matrix = createTestMatrix();
        Matrix matrixCopy = matrix.copy();
        assertEquals(matrix, matrixCopy);
        assertEquals(3, matrixCopy.getCols());
        assertEquals(3, matrixCopy.getRows());
    }

    @Test
    public void testIndexAccess() {
        Matrix matrix = new Matrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(8, 8));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(0, -1, 5.0));
    }

    @Test
    public void testResizeRowsUp() {
        Matrix matrix = createTestMatrix();
        matrix.resizeRows(5);
        assertEquals(5, matrix.getRows());
        assertEquals(0.0, matrix.get(4, 0), 1e-10);
        // Check that original data is preserved
        assertEquals(2.0, matrix.get(0, 0), 1e-10);
    }

    @Test
    public void testResizeColsUp() {
        Matrix matrix = createTestMatrix();
        matrix.resizeCols(5);
        assertEquals(5, matrix.getCols());
        assertEquals(0.0, matrix.get(0, 4), 1e-10);
        // Check that original data is preserved
        assertEquals(2.0, matrix.get(0, 0), 1e-10);
    }

    @Test
    public void testResizeColsDown() {
        Matrix matrix = createTestMatrix();
        assertThrows(IllegalArgumentException.class, () -> matrix.resizeCols(-1));
        assertEquals(3, matrix.getCols());
    }

    @Test
    public void testResizeRowsDown() {
        Matrix matrix = createTestMatrix();
        assertThrows(IllegalArgumentException.class, () -> matrix.resizeRows(-1));
        assertEquals(3, matrix.getRows());
    }

    @Test
    public void testDeterminant() {
        Matrix matrix = createTestMatrix();
        assertEquals(-1.0, matrix.determinant(), 1e-10);

        Matrix nonSquare = new Matrix(1, 7);
        assertThrows(IllegalStateException.class, nonSquare::determinant);
    }

    @Test
    public void testTranspose() {
        Matrix matrix = createTestMatrix();
        Matrix transposed = matrix.transpose();
        Matrix doubleTransposed = transposed.transpose();
        assertEquals(matrix, doubleTransposed);
    }

    @Test
    public void testCofactors() {
        Matrix matrix = createTestMatrix();
        Matrix cofactors = matrix.cofactors();
        double[][] expectedValues = {
                {-1, 38, -27},
                {1, -41, 29},
                {-1, 34, -24}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedValues[i][j], cofactors.get(i, j), 1e-10);
            }
        }

        Matrix matrix1x1 = new Matrix(1, 1);
        matrix1x1.set(0, 0, 5);
        Matrix cofactors1x1 = matrix1x1.cofactors();
        assertEquals(5.0, cofactors1x1.get(0, 0), 1e-10);

        Matrix nonSquare = new Matrix(1, 7);
        assertThrows(IllegalStateException.class, nonSquare::cofactors);
    }

    @Test
    public void testInverseMatrix() {
        Matrix matrix = createTestMatrix();
        Matrix inverse = matrix.inverse();
        double[][] expectedValues = {
                {1, -1, 1},
                {-38, 41, -34},
                {27, -29, 24}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedValues[i][j], inverse.get(i, j), 1e-10);
            }
        }

        Matrix nonSquare = new Matrix(1, 7);
        assertThrows(IllegalStateException.class, nonSquare::inverse);
    }

    @Test
    public void testMatrixMultiplication() {
        Matrix matrix1 = createTestMatrix();
        Matrix matrix2 = new Matrix(3, 3);
        double[][] identityValues = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix2.set(i, j, identityValues[i][j]);
            }
        }
        Matrix result = matrix1.multiply(matrix2);
        assertEquals(matrix1, result);

        Matrix incompatible = new Matrix(1, 7);
        assertThrows(IllegalArgumentException.class, () -> incompatible.multiply(matrix1));
    }

    @Test
    public void testNumberMultiplication() {
        Matrix matrix1 = createTestMatrix();
        Matrix result = matrix1.multiply(2);
        double[][] expectedValues = {
                {4, 10, 14},
                {12, 6, 8},
                {10, -4, -6}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedValues[i][j], result.get(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testMatrixSubtraction() {
        Matrix matrix1 = createTestMatrix();
        Matrix matrix2 = new Matrix(3, 3);
        double[][] identityValues = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix2.set(i, j, identityValues[i][j]);
            }
        }
        Matrix result = matrix1.subtract(matrix2);
        double[][] expectedValues = {
                {1, 5, 7},
                {6, 2, 4},
                {5, -2, -4}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedValues[i][j], result.get(i, j), 1e-10);
            }
        }

        Matrix incompatible = new Matrix(1, 7);
        assertThrows(IllegalArgumentException.class, () -> incompatible.subtract(matrix1));
    }

    @Test
    public void testMatrixAddition() {
        Matrix matrix1 = createTestMatrix();
        Matrix matrix2 = new Matrix(3, 3);
        double[][] identityValues = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix2.set(i, j, identityValues[i][j]);
            }
        }
        Matrix result = matrix1.add(matrix2);
        double[][] expectedValues = {
                {3, 5, 7},
                {6, 4, 4},
                {5, -2, -2}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedValues[i][j], result.get(i, j), 1e-10);
            }
        }

        Matrix incompatible = new Matrix(1, 7);
        assertThrows(IllegalArgumentException.class, () -> incompatible.add(matrix1));
    }

    @Test
    public void testEqualsAndHashCode() {
        Matrix matrix1 = createTestMatrix();
        Matrix matrix2 = createTestMatrix();
        Matrix matrix3 = new Matrix(2, 2);

        assertEquals(matrix1, matrix2);
        assertEquals(matrix1.hashCode(), matrix2.hashCode());
        assertNotEquals(matrix1, matrix3);
    }

    @Test
    public void testToString() {
        Matrix matrix = new Matrix(2, 2);
        matrix.set(0, 0, 1.0);
        matrix.set(0, 1, 2.0);
        matrix.set(1, 0, 3.0);
        matrix.set(1, 1, 4.0);

        String result = matrix.toString();
        assertTrue(result.contains("2x2"));
        assertTrue(result.contains("1.0 2.0"));
        assertTrue(result.contains("3.0 4.0"));
    }

    @Test
    public void testTextIO() throws IOException {
        Matrix original = createTestMatrix();

        // Test save and load
        String text = original.saveToText().toString();

        StringReader reader = new StringReader(text);
        Matrix loaded = Matrix.loadFromText(reader);

        assertEquals(original, loaded);
    }

    @Test
    public void testBinaryIO() throws IOException {
        Matrix original = createTestMatrix();

        // Test save and load
        byte[] data = original.saveToBinary().toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Matrix loaded = Matrix.loadFromBinary(in);

        assertEquals(original, loaded);
    }

    @Test
    public void testEmptyMatrix() {
        Matrix empty = new Matrix();
        assertEquals(0, empty.getRows());
        assertEquals(0, empty.getCols());
        assertEquals("0x0\n", empty.toString());
    }

    @Test
    public void testSingleElementMatrix() {
        Matrix single = new Matrix(1, 1);
        single.set(0, 0, 42.0);
        assertEquals(42.0, single.determinant(), 1e-10);
        assertEquals(42.0, single.get(0, 0), 1e-10);
    }
}
