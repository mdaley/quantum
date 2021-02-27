package quantum.ch3;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;
import utils.Utils;

public class Exercise_3_2_1 extends Exercise {
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

        ComplexMatrix multiplied = matrix.clone();

        for (int i = 0; i < times; i++) {
            System.out.println("Power " + i + 1);
            System.out.println(multiplied.toPrettyString());
            System.out.println();

            multiplied = multiplied.multiply(matrix);
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
