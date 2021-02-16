package quantum.complex;

import static quantum.complex.Complex.complex;
import static quantum.complex.Polar.polar;

public class ComplexVector extends ComplexMatrix {
    private final boolean isRowVector;

    public static ComplexVector complexRowVector(int length, Complex[] data) {
        return new ComplexVector(length, true, data);
    }

    public static ComplexVector complexColumnVector(int length, Complex[] data) {
        return new ComplexVector(length, false, data);
    }

    public static ComplexVector complexRowVector(String data) {
        return complexVector(data, true);
    }

    public static ComplexVector complexColumnVector(String data) {
        return complexVector(data, false);
    }

    private static ComplexVector complexVector(String data, boolean isRowVector) {
        if (!data.contains("\\|\\|")) {
            String elements[] = data.split("\\|");
            if (elements.length > 0) {
                Complex[] values = new Complex[elements.length];

                for (int i = 0; i < elements.length; i++) {
                    try {
                        values[i] = complex(elements[i]);
                    } catch (IllegalArgumentException ignored) {
                        values[i] = polar(elements[i]).complex();
                    }
                }

                return new ComplexVector(elements.length, isRowVector, values);
            }
        }

        throw new IllegalArgumentException("Invalid data - cannot create vector");
    }

    private ComplexVector(int length, boolean isRowVector, Complex[] data) {
        super(isRowVector ? 1 : length, isRowVector ? length: 1, buildData(data, isRowVector, length));
        this.isRowVector = isRowVector;
    }

    private static Complex[][] buildData(Complex[] rowData, boolean isRowVector, int length) {
        Complex[][] data;

        if (isRowVector) {
            data = new Complex[1][length];
            for (int i = 0; i < length; i++) {
                data[0][i] = rowData[i];
            }
        } else {
            data = new Complex[length][1];
            for (int i = 0; i < length; i++) {
                data[i][0] = rowData[i];
            }
        }

        return data;
    }

    @Override
    public ComplexVector transpose() {
        if (isRowVector) {
            return new ComplexVector(columns, false, rowData(0));
        } else {
            return new ComplexVector(rows, true, columnData(0));
        }
    }
}
