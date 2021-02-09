package quantum.complex;

import utils.ExtendedComplexFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComplexMatrix {
    public static final ExtendedComplexFormat COMPLEX_FORMAT = ExtendedComplexFormat.getExtendedInstance();

    private int rows;
    private int columns;
    private ExtendedComplex[][] values;

    public static ComplexMatrix fromString(String data) {
        int rowCount;
        int colCount = -1;
        String[] rows = data.split(";");
        rowCount = rows.length;

        List<ExtendedComplex> list = new ArrayList<>();

        for (int i = 0; i < rows.length; i++) {
            String[] elements = rows[i].split(",");
            if (colCount == -1) {
                colCount = elements.length;
            } else if (elements.length != colCount) {
                throw new IllegalArgumentException("Matrix is not square");
            }

            for (int j = 0; j < elements.length; j++) {
                list.add(ExtendedComplex.extendedValueOf(COMPLEX_FORMAT.parse(elements[j].trim())));
            }
        }

        ExtendedComplex[][] values = new ExtendedComplex[rowCount][colCount];

        int i = 0;
        for (int m = 0; m < rowCount; m++) {
            for (int n = 0; n < colCount; n++) {
                values[m][n] = list.get(i++);
            }
        }

        return new ComplexMatrix(rowCount, colCount, values);
    }

    private ComplexMatrix(int rows, int columns, ExtendedComplex[][] values) {
        this.rows = rows;
        this.columns = columns;
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                strb.append(COMPLEX_FORMAT.format(values[m][n]));
                if (n < columns - 1) {
                    strb.append(", ");
                }
            }

            if (m < rows - 1) {
                strb.append("; ");
            }
        }

        return strb.toString();
    }

    public String toPrettyString() {
        String[] cells = new String[rows * columns];
        int len = -1;

        int i = 0;
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                String cell = COMPLEX_FORMAT.format(values[m][n]);
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
            strb.append("\n");
        }

        return strb.toString();
    }
}
