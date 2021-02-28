package quantum.ch3;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;
import utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.Function;

public class Exercise_3_1_2 extends Exercise {
    public static final NumberFormat DP3 = new DecimalFormat("0.####");
    public static final NumberFormat DP_3_SCI = new DecimalFormat("0.####E0");

    @Override
    public String title() {
        return "Multiply matrix by itself n-times";
    }

    @Override
    public String description() {
        return "Multiplying doubly stochastic probabilistic matrices (exercise 3.2.6)";
    }

    @Override
    public void execute() {
        boolean ok = false;
        ComplexMatrix matrix = null;

        while (!ok) {
            matrix = Utils.inputMatrix("Enter a probabilistic doubly stochastic matrix: \n");
            if (isDoublyStochastic(matrix)) {
                ok = true;
            } else {
                System.out.println("The matrix must be square and it must be doubly stochastic, that is the " +
                        "each row must sum to 1, as must each column.\n");
            }
        }

        int times = Utils.inputInteger("Power to multiply matrix to: ");

        Function<Complex, String> formatter = c -> {
            if (c.img == 0.0) {
                return niceFormat(c.real);
            } else if (c.real == 0.0) {
                return niceFormat(c.img) + "i";
            } else {
                return String.format("%s %s %si", niceFormat(c.real), c.img < 0 ? "-" : "+", niceFormat(Math.abs(c.img)));
            }
        };

        ComplexMatrix multiplied = matrix.clone();

        for (int i = 0; i < times; i++) {
            System.out.println("Power " + (i + 1));
            System.out.println(multiplied.toPrettyString(formatter));
            System.out.println();

            multiplied = multiplied.multiply(matrix);
        }
    }

    private String niceFormat(double d) {
        if (Math.abs(d) < 0.0001 || Math.abs(d) > 1E6) {
            return DP_3_SCI.format(d);
        } else {
            return DP3.format(d);
        }
    }

    private boolean isDoublyStochastic(ComplexMatrix matrix) {
        if (!matrix.isSquare()) {
            return false;
        }

        for (int i = 0; i < matrix.rows; i++) {
            if (!isVeryClose(matrix.row(i).sum(), Complex.ONE) || !isVeryClose(matrix.column(i).sum(), Complex.ONE)) {
                return false;
            }
        }

        return true;
    }

    private boolean isVeryClose(Complex a, Complex b) {
        return Math.abs(a.real - b.real) < 1e-10 && Math.abs(a.img - b.img) < 1e-10;
    }
}
