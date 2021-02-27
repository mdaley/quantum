package quantum.complex;

import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexVector.complexColumnVector;
import static quantum.complex.ComplexVector.complexRowVector;
import static quantum.complex.Polar.polar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ComplexMatrix {

    public final int rows;
    public final int columns;
    public final Complex[][] values;

    @Override
    protected ComplexMatrix clone() {
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
        String[] cells = new String[rows * columns];
        int len = -1;

        int i = 0;
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                String cell = values[m][n].toString();
                cells[i++] = cell;
                len = cell.length() > len ? cell.length() : len;
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

    public void inverseInPlace() {
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                values[m][n] = complex(-values[m][n].real, -values[m][n].img);
            }
        }
    }

    public ComplexMatrix inverse() {
        ComplexMatrix result = clone();
        result.inverseInPlace();

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
     * @return
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

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
