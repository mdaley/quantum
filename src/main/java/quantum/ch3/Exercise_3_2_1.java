package quantum.ch3;

import static quantum.complex.ComplexMatrix.complexMatrix;
import static quantum.complex.ComplexVector.complexColumnVector;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;
import quantum.complex.ComplexVector;
import utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Exercise_3_2_1 extends Exercise {
    public static final NumberFormat DP3 = new DecimalFormat("0.####");
    @Override
    public String title() {
        return "Probabilistic state system";
    }

    @Override
    public String description() {
        return "null";
    }

    @Override
    public void execute() {
        ComplexMatrix initialState = null;
        ComplexMatrix stateChange = null;

        if (!Utils.inputBoolean("Use canned values ")) {
            initialState = Utils.inputColumnVector("Enter the starting state vector: \n");

            boolean notOk = true;

            boolean doublyStochasticOnly = Utils.inputBoolean("Do you want to be strict (doubly stochastic)? ");

            while (notOk) {
                stateChange = Utils.inputMatrix("Enter the probabilistic state change matrix: \n");
                if (stateChange.rows == initialState.rows
                        && ((doublyStochasticOnly && stateChange.isDoublyStochastic())
                        || (stateChange.isSquare() && columnsAddToOne(stateChange)))) {
                    notOk = false;
                } else {
                    if (doublyStochasticOnly) {
                        System.out.println("The matrix must be doubly stochastic, that is each row and each column must have a sum\n" +
                                "of 1. Please try again.\n");
                    } else {
                        System.out.println("The matrix must be square and columns must each add up to one.");
                    }
                }
            }
        } else {
            initialState = complexColumnVector("1|0|0|0|0|0|0|0");
            stateChange = complexMatrix("0|0|0|0|0|0|0|0||" +
                    "0.5|0|0|0|0|0|0|0||" +
                    "0.5|0|0|0|0|0|0|0||" +
                    "0|0.33333|0|1|0|0|0|0||" +
                    "0|0.33333|0|0|1|0|0|0||" +
                    "0|0.33333|0.33333|0|0|1|0|0||" +
                    "0|0|0.33333|0|0|0|1|0||" +
                    "0|0|0.33333|0|0|0|0|1");
        }

        int steps = Utils.inputInteger("Number of steps to process: ");

        ComplexMatrix[] states = new ComplexMatrix[steps + 1];
        int[] widths = new int[steps + 1];

        for (int i = 0; i < initialState.rows; i++) {
            widths[0] = Math.max(widths[0], DP3.format(initialState.values[i][0].real).length());
        }

        states[0] = initialState;

        for (int i = 1; i < steps + 1; i++) {
            states[i] = stateChange.multiply(states[i - 1]);

            for (int j = 0; j < states[i].rows; j++) {
                widths[i] = Math.max(widths[i], DP3.format(states[i].values[j][0].real).length());
            }
        }

        System.out.print("States after each tick:\nticks  ");

        for (int i = 0; i < steps + 1; i++) {
            System.out.printf("%" + widths[i] + "s ", i);
        }

        System.out.println();

        for (int j = 0; j < initialState.rows; j++) {
            System.out.print(j == 0 ? "states " : "       ");
            for (int i = 0; i < steps + 1; i++) {
                String real = DP3.format(states[i].values[j][0].real);
                System.out.printf("%" + widths[i] + "s ", real);
            }
            System.out.println();
        }

        System.out.printf("count  ");

        for (int i = 0; i < steps + 1; i++) {
            System.out.printf("%" + widths[i] + "s ", (int) Math.round(states[i].sum().real));
        }

        System.out.println();
    }

    private boolean columnsAddToOne(ComplexMatrix m) {
        for (int n = 0; n < m.columns; n++) {
            if (!m.column(n).sum().equals(Complex.ONE, 1E-3)) {
                return false;
            }
        }

        return true;
    }
}
