package quantum.complex;

import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexVector.complexColumnVector;
import static quantum.complex.ComplexVector.complexRowVector;
import static quantum.complex.Polar.polar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class ComplexMatrix {

    public final int rows;
    public final int columns;
    public final Complex[][] values;

    @Override
    public ComplexMatrix clone() {
        Complex[][] data = new Complex[rows][columns];
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                data[m][n] = values[m][n];
            }
        }

        return complexMatrix(rows, columns, data);
    }

    public static ComplexMatrix complexMatrix(int rows, int columns, Complex[][] data) {
        return new ComplexMatrix(rows, columns, data);
    }

    public static ComplexMatrix complexMatrix(int rows, int columns, Complex value) {
        Complex[][] data = new Complex[rows][columns];
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                data[m][n] = value;
            }
        }

        return complexMatrix(rows, columns, data);
    }

    public static ComplexMatrix identityMatrix(int size) {
        Complex[][] data = new Complex[size][size];
        for (int m = 0; m < size; m++) {
            for (int n = 0; n < size; n++) {
                data[m][n] = m == n ? Complex.ONE : Complex.ZERO;
            }
        }

        return complexMatrix(size, size, data);
    }

    public static ComplexMatrix diagonalMatrix(Complex[] data) {
        int size = data.length;
        Complex[][] values = new Complex[size][size];

        for (int m = 0; m < size; m++) {
            for (int n = 0; n < size; n++) {
                values[m][n] = m == n ? data[m] : Complex.ZERO;
            }
        }

        return new ComplexMatrix(size, size, values);
    }

    public static ComplexMatrix diagonalMatrix(Complex value, int size) {
        Complex[] values = new Complex[size];
        for (int i = 0; i < size; i++) {
            values[i] = value;
        }

        return diagonalMatrix(values);
    }

    public static ComplexMatrix diagonalMatrix(String data) {
        String[] values = data.split("\\|");

        Complex[] complexValues = new Complex[values.length];

        for (int i = 0; i < values.length; i++) {
            complexValues[i] = complex(values[i]);
        }

        return diagonalMatrix(complexValues);
    }

    public static ComplexMatrix complexMatrix(String data) {
        int rowCount;
        int colCount = -1;
        String[] rows = data.split("\\|\\|");
        rowCount = rows.length;

        List<Complex> list = new ArrayList<>();

        for (int i = 0; i < rows.length; i++) {
            String[] elements = rows[i].split("\\|");
            if (colCount == -1) {
                colCount = elements.length;
            } else if (elements.length != colCount) {
                throw new IllegalArgumentException("Matrix is not of regular shape");
            }

            for (int j = 0; j < elements.length; j++) {
                Complex complex;
                try {
                    complex = complex(elements[j].trim());
                } catch (IllegalArgumentException ignored) {
                    complex = polar(elements[j].trim()).complex();
                }

                list.add(complex);
            }
        }

        Complex[][] values = new Complex[rowCount][colCount];

        int i = 0;
        for (int m = 0; m < rowCount; m++) {
            for (int n = 0; n < colCount; n++) {
                values[m][n] = list.get(i++);
            }
        }

        return new ComplexMatrix(rowCount, colCount, values);
    }

    ComplexMatrix(int rows, int columns, Complex[][] values) {
        this.rows = rows;
        this.columns = columns;
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                strb.append(values[m][n].toString());
                if (n < columns - 1) {
                    strb.append("| ");
                }
            }

            if (m < rows - 1) {
                strb.append("|| ");
            }
        }

        return strb.toString();
    }

    public String toString(Function<Complex[][], String> formatter) {
        return formatter.apply(values);
    }

    public String toPrettyString() {
        return toPrettyString(null);
    }
    public String toPrettyString(Function<Complex, String> formatter) {
        String[] cells = new String[rows * columns];
        int len = -1;

        int i = 0;
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                String cell = values[m][n].toString(formatter);
                cells[i++] = cell;
                len = Math.max(cell.length(), len);
            }
        }

        StringBuilder strb = new StringBuilder();

        i = 0;
        for (int m = 0; m < rows; m++) {
            if (m == 0 && rows == 1) {
                strb.append("┃ ");
            } else if (m == 0) {
                strb.append("┏ " );
            } else if (m == rows - 1) {
                strb.append("┗ ");
            } else {
                strb.append("┃ ");
            }
            for (int n = 0; n < columns; n++) {
                strb.append(String.format("%" + len + "s", cells[i++]));
                if (n < columns - 1) {
                    strb.append(", ");
                }
            }
            if (m == 0 && rows == 1) {
                strb.append(" ┃");
            } else if (m == 0) {
                strb.append(" ┓");
            } else if (m == rows - 1) {
                strb.append(" ┛");
            } else {
                strb.append(" ┃");
            }

            if (m < rows - 1) {
                strb.append("\n");
            }
        }

        return strb.toString();
    }

    public void addInPlace(ComplexMatrix matrix) {
        throwIfNotSameDimensions(matrix);

        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                values[m][n] = values[m][n].add(matrix.values[m][n]);
            }
        }
    }

    public ComplexMatrix add(ComplexMatrix matrix) {
        ComplexMatrix result = clone();
        result.addInPlace(matrix);

        return result;
    }

    public void subtractInPlace(ComplexMatrix matrix) {
        throwIfNotSameDimensions(matrix);

        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                values[m][n] = values[m][n].subtract(matrix.values[m][n]);
            }
        }
    }

    public ComplexMatrix subtract(ComplexMatrix matrix) {
        ComplexMatrix result = clone();
        result.subtractInPlace(matrix);

        return result;
    }

    public void scalarMultiplyInPlace(ComplexMatrix matrix) {
        throwIfNotSameDimensions(matrix);

        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                values[m][n] = values[m][n].multiply(matrix.values[m][n]);
            }
        }
    }

    public ComplexMatrix scalarMultiply(ComplexMatrix matrix) {
        ComplexMatrix result = clone();
        result.scalarMultiplyInPlace(matrix);

        return result;
    }

    public static ComplexMatrix multiply(ComplexMatrix matrix1, ComplexMatrix matrix2) {
        if (matrix1.columns != matrix2.rows) {
            throw new IllegalArgumentException(String.format("Cannot multiply a [%d, %d] matrix by a [%d, %d] matrix",
                    matrix1.rows, matrix1.columns, matrix2.rows, matrix2.columns));
        }

        Complex[][] values = new Complex[matrix1.rows][matrix2.columns];

        for (int n = 0; n < matrix1.rows; n++) {
            for (int m = 0; m < matrix2.columns; m++) {
                Complex c = complex(0, 0);
                for (int i = 0; i < matrix1.columns; i++) {
                    c = c.add(matrix1.values[n][i].multiply(matrix2.values[i][m]));
                }
                values[n][m] = c;
            }
        }

        return complexMatrix(matrix1.rows, matrix2.columns, values);
    }

    public ComplexMatrix multiply(ComplexMatrix matrix) {
        return multiply(this, matrix);
    }

    public void negateInPlace() {
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                values[m][n] = complex(-values[m][n].real, -values[m][n].img);
            }
        }
    }

    public ComplexMatrix negate() {
        ComplexMatrix result = clone();
        result.negateInPlace();

        return result;
    }

    public ComplexMatrix transpose() {
        Complex[][] transposeValues = new Complex[columns][rows];
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                transposeValues[n][m] = this.values[m][n];
            }
        }

        return new ComplexMatrix(columns, rows, transposeValues);
    }

    public void conjugateInPlace() {
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                values[m][n] = values[m][n].conjugate();
            }
        }
    }

    public ComplexMatrix conjugate() {
        ComplexMatrix result = clone();
        result.conjugateInPlace();
        return result;
    }

    public ComplexMatrix adjoint() {
        ComplexMatrix result = transpose();
        result.conjugateInPlace();
        return result;
    }

    public double innerProduct() {
        ComplexMatrix adjoint = adjoint();
        double result = 0.0;
        for (int m = 0; m < rows; m++) {
            for (int i = 0; i < columns; i++) {
                result += values[m][i].multiply(adjoint.values[i][m]).real;
            }
        }

        return result;
    }

    public static ComplexMatrix tensorProduct(ComplexMatrix m1, ComplexMatrix m2) {
        int tensorRows = m1.rows * m2.rows;
        int tensorColumns = m1.columns * m2.columns;
        Complex[][] data = new Complex[tensorRows][tensorColumns];
        for (int m = 0; m < m1.rows; m++) {
            for (int p = 0; p < m2.rows; p++) {
                for (int n = 0; n < m1.columns; n++) {
                    for (int q = 0; q < m2.columns; q++) {
                        data[m * m2.rows + p][n * m2.columns + q] = m1.values[m][n].multiply(m2.values[p][q]);
                    }
                }
            }
        }

        return complexMatrix(tensorRows, tensorColumns, data);
    }

    public Complex sum() {
        double real = 0, img = 0;
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                real += values[m][n].real;
                img += values[m][n].img;
            }
        }

        return complex(real, img);
    }

    /**
     * A complex matrix is Hermitian if it's adjoint (i.e. transposed conjugate) is equal to the matrix itself.
     * The matrix has to be square and it's diagonal has to be only real.
     *
     * @return true if the matrix is Hermitian, otherwise false
     */
    public boolean isHermitian() {
        return isSquareAndDiagonalOnlyReal() && equals(adjoint());
    }

    /**
     * A matrix is boolean is if only contains ones and zeros.
     * @return true if matrix is boolean
     */
    public boolean isBoolean() {
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                if (values[m][n] != Complex.ONE && values[m][n] != Complex.ZERO) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * A complex matrix is unitary if it, multiplied by it's adjoint is equal to the identity matrix of the same
     * (square) size.
     */
    public boolean isUnitary() {
        if (!isSquare()) {
            return false;
        }

        ComplexMatrix matrix = multiply(this, adjoint());

        return matrix.equals(identityMatrix(rows));
    }

    public boolean isSquare() {
        return rows == columns;
    }

    public boolean isSquareAndDiagonalOnlyReal() {
        if (!isSquare()) {
            return false;
        }

        for (int i = 0; i < rows; i++) {
            if (values[i][i].img != 0.0) {
                return false;
            }
        }

        return true;
    }

    public ComplexMatrix row(int m) {
        if (m > rows -1 || m < 0) {
            throw new IllegalArgumentException(String.format("row %d does not exist", m));
        }

        return complexRowVector(columns, rowData(m));
    }

    Complex[] rowData(int m) {
        return values[m];
    }

    public ComplexMatrix column(int n) {
        if (n > columns - 1 || n < 0) {
            throw new IllegalArgumentException(String.format("column %d does not exist", n));
        }

        Complex[] data = new Complex[rows];
        for (int i = 0; i < rows; i++) {
            data[i] = values[i][n];
        }

        return complexColumnVector(rows, columnData(n));
    }

    Complex[] columnData(int n) {
        Complex[] data = new Complex[rows];
        for (int i = 0; i < rows; i++) {
            data[i] = values[i][n];
        }

        return data;
    }

    private void throwIfNotSameDimensions(ComplexMatrix matrix) {
        if (rows != matrix.rows || columns != matrix.columns) {
            throw new IllegalArgumentException(String.format("Could not perform operation on different shape matrices: [%d,%d] vs [%d,%d]",
                    matrix.rows, matrix.columns, rows, columns));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (getClass() != o.getClass()) && getClass() != o.getClass().getSuperclass()) return false;
        ComplexMatrix that = (ComplexMatrix) o;
        return rows == that.rows && columns == that.columns && Arrays.deepEquals(values, that.values);
    }

    public boolean equals(ComplexMatrix o, double accuracy) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.rows != rows || o.columns != columns) return false;

        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                if (!values[m][n].equals(o.values[m][n], accuracy)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns);
        result = 31 * result + Arrays.deepHashCode(values);
        return result;
    }

    /**
     * Checks if the matrix doubly stochastic, that is: it is square and the sum of each column and each
     * row is one.
     * @param accuracy allow this small deviation + or - from 1 when checking rows and column sums.
     * @return true if the matrix is doubly stochastic.
     */
    public boolean isDoublyStochastic(double accuracy) {
        if (!isSquare()) {
            return false;
        }

        for (int i = 0; i < rows; i++) {
            if (!row(i).sum().equals(Complex.ONE, accuracy) || column(i).sum().equals(Complex.ONE, accuracy)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the matrix doubly stochastic, that is: it is square and the sum of each column and each
     * row is one (to within a small amount of 1E-10).
     * @return true if the matrix is doubly stochastic.
     */
    public boolean isDoublyStochastic() {
        return isDoublyStochastic(1e-10);
    }

    public static Complex trace(ComplexMatrix m) {
        if (!m.isSquare()) {
            throw new IllegalArgumentException("Can only get trace of a square matrix");
        }

        double real = 0.0;
        double img = 0.0;

        for (int i = 0; i < m.rows; i++) {
            Complex value = m.values[i][i];
            real += value.real;
            img += value.img;
        }

        return complex(real, img);
    }

    public Complex trace() {
        return trace(this);
    }

    public static boolean isDiagonal(ComplexMatrix matrix) {
        if (!matrix.isSquare()) {
            return false;
        }

        for (int m = 0; m < matrix.rows; m++) {
            for (int n = 0; n < matrix.columns; n++) {
                if (m != n && !matrix.values[m][n].isZero()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isDiagonal() {
        return isDiagonal(this);
    }

    public static Complex determinant(ComplexMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("Can only calculate determinant for a square matrix");
        }

        if (matrix.isDiagonal()) {
            Complex det = matrix.values[0][0];
            for (int i = 1; i < matrix.rows; i++) {
                det = det.multiply(matrix.values[i][i]);
            }

            return det;
        }

        if (matrix.rows == 2) {
            return matrix.values[0][0].multiply(matrix.values[1][1]).subtract(matrix.values[0][1].multiply(matrix.values[1][0]));
        } else {
            Complex det = Complex.ZERO;
            for (int i = 0; i < matrix.columns; i++) {
                Complex subDet = determinant(matrix.submatrix(0, i));
                Complex value = matrix.values[0][i];
                if (i % 2 == 0) {
                    det = det.add(subDet.multiply(value));
                } else {
                    det = det.subtract(subDet.multiply(value));
                }
            }

            return det;
        }
    }

    public Complex determinant() {
        return determinant(this);
    }

    public static ComplexMatrix submatrix(ComplexMatrix matrix, Set<Integer> rowsToRemove, Set<Integer> columnsToRemove) {
        int newRows = matrix.rows - rowsToRemove.size();
        int newColumns = matrix.columns - columnsToRemove.size();
        Complex[][] newValues = new Complex[newRows][newColumns];

        int p = 0;
        for (int m = 0; m < matrix.rows; m++) {
            if (!rowsToRemove.contains(m)) {
                int q = 0 ;
                for (int n = 0; n < matrix.columns; n++) {
                    if (!columnsToRemove.contains(n)) {
                        newValues[p][q] = matrix.values[m][n];

                        q++;
                    }
                }

                p++;
            }
        }

        return complexMatrix(newRows, newColumns, newValues);
    }

    public static ComplexMatrix submatrix(ComplexMatrix matrix, int rowToRemove, int columnToRemove) {
        return submatrix(matrix, Set.of(rowToRemove), Set.of(columnToRemove));
    }

    public ComplexMatrix submatrix(Set<Integer> rowsToRemove, Set<Integer> columnsToRemove) {
        return submatrix(this, rowsToRemove, columnsToRemove);
    }

    public ComplexMatrix submatrix(int rowToRemove, int columnToRemove) {
        return submatrix(Set.of(rowToRemove), Set.of(columnToRemove));
    }

    public static ComplexMatrix concat(ComplexMatrix m1, ComplexMatrix m2) {
        if (m1.rows != m2.rows) {
            throw new IllegalArgumentException("Both matrices must have the same number of rows");
        }

        Complex[][] values = new Complex[m1.rows][m1.columns + m2.columns];

        for (int m = 0; m < m1.rows; m++) {
            for (int n = 0; n < m1.columns; n++) {
                values[m][n] = m1.values[m][n];
            }

            for (int p = 0; p < m1.columns; p++) {
                values[m][p + m1.columns] = m2.values[m][p];
            }
        }

        return new ComplexMatrix(m1.rows, m1.columns + m2.columns, values);
    }

    public static ComplexMatrix inverse(ComplexMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("A non-square matrix does not have an inverse");
        } else if (matrix.determinant().equals(Complex.ZERO)) {
            throw new IllegalArgumentException("A matrix with determinant of zero has no inverse");
        }

        int size = matrix.rows;

        // build a new matrix with a same size itentity matrix to the right of the matrix to be inverted.
        ComplexMatrix working = concat(matrix, identityMatrix(size));

        // run the gaussian elimination process
        for (int m = 0; m < size; m++) {
            Complex pivotValue = working.values[m][m];

            pivotValue = rearrangeRowsIfPivotIsZero(working, size, m, pivotValue);

            for (int p = 0; p < size; p++) {
                if (p != m) {
                    Complex zeroer = working.values[p][m].divide(pivotValue);
                    for (int n = 0 ; n < size * 2; n++) {
                        working.values[p][n] = working.values[p][n].subtract(working.values[m][n].multiply(zeroer));
                    }
                }
            }
        }

        // normalise the left part of the working matrix to identity matrix.
        for (int m = 0; m < size; m++) {
            Complex pivotValue = working.values[m][m];
            for (int n = 0; n < size * 2; n++) {
                if (!pivotValue.equals(Complex.ONE)) {
                    working.values[m][n] = working.values[m][n].divide(pivotValue);
                }
            }
        }

        // collect the result - the inverse matrix - from the right part of the working matrix.
        Complex[][] result = new Complex[size][size];

        for (int m = 0; m < size; m++) {
            for (int n = 0; n < size; n++) {
                result[m][n] = working.values[m][n + size];
            }
        }

        return complexMatrix(matrix.rows, matrix.columns, result);
    }

    private static Complex rearrangeRowsIfPivotIsZero(ComplexMatrix working, int size, int currentRow, Complex pivotValue) {
        // If the pivot is zero, a lower row can be swapped with the current row if the lower row has a usable non-zero pivot.
        if (pivotValue.equals(Complex.ZERO)) {
            int pivotableRow = -1;
            for (int p = currentRow + 1; p < size; p++) {
                if (!working.values[p][currentRow].equals(Complex.ZERO)) {
                    pivotableRow = p;
                }
            }

            if (pivotableRow == -1) {
                throw new RuntimeException("No pivotable value available. Can't determine inverse matrix");
            }

            // swap rows
            for (int n = 0; n < size * 2; n++) {
                Complex save = working.values[currentRow][n];
                working.values[currentRow][n] = working.values[pivotableRow][n];
                working.values[pivotableRow][n] = save;
            }

            pivotValue = working.values[currentRow][currentRow];
        }

        return pivotValue;
    }

    public ComplexMatrix inverse() {
        return inverse(this);
    }
}
