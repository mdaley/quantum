package quantum.complex;

import static quantum.complex.Complex.complex;
import static quantum.complex.Polar.polar;

/**
 * A vector is just a 1 row or 1 column matrix. Here are convenience `vector` methods but they all
 * operate on and/or create vectors.
 */
public class ComplexVector extends ComplexMatrix {

    // not to be used.
    private ComplexVector() {
        super(0, 0, null);
    }

    public static ComplexMatrix complexRowVector(int length, Complex[] rowData) {
        Complex[][] data = new Complex[1][];
        data[0] = rowData;
        return new ComplexMatrix(1, length, data);
    }

    public static ComplexMatrix complexColumnVector(int length, Complex[] columnData) {
        Complex[][] data = new Complex[length][1];
        for (int i = 0; i < length; i++) {
            data[i][0] = columnData[i];
        }
        return new ComplexMatrix(length,1, data);
    }

    public static ComplexMatrix complexRowVector(String data) {
        Complex[] values = parseData(data);
        return complexRowVector(values.length, values);
    }

    public static ComplexMatrix complexColumnVector(String data) {
        Complex[] values = parseData(data);
        return complexColumnVector(values.length, values);
    }

    private static Complex[] parseData(String data) {
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

                return values;
            }
        }

        throw new IllegalArgumentException("Invalid data - cannot create vector");
    }
}
