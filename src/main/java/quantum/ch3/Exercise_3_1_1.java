package quantum.ch3;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;
import utils.Utils;

public class Exercise_3_1_1 extends Exercise {
    @Override
    public String title() {
        return "Boolean state system";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void execute() {
        ComplexMatrix initialState = Utils.inputColumnVector("Enter the starting state vector: \n");

        boolean notOk = true;
        ComplexMatrix stateChange = null;

        while(notOk) {
            stateChange = Utils.inputMatrix("Enter the state change matrix: \n");
            if (stateChange.isSquare() && stateChange.rows == initialState.rows
                && isBooleanAdjacencyMatrix(stateChange)) {
                notOk = false;
            } else {
                System.out.println("The matrix must be square with the same dimensions as the length of the\n" +
                        "state vector, it must contain only ones and zeros and there must be only a single one\n" +
                        "value in each column. Please try again.\n");
            }
        }

        int steps = Utils.inputInteger("Number of steps to process: ");

        ComplexMatrix[] states = new ComplexMatrix[steps + 1];
        int[] widths = new int[steps + 1];

        widths[0] = Integer.toString((int)Math.round(initialState.sum().real)).length();

        states[0] = initialState;

        for (int i = 1; i < steps + 1; i++) {
            states[i] = stateChange.multiply(states[i - 1]);

            widths[i] = Integer.toString((int)Math.round(states[i].sum().real)).length();

        }

        System.out.print("States after each tick:\nticks  ");

        for (int i = 0; i < steps + 1; i++) {
            System.out.printf("%" + widths[i] + "s ", i);
        }

        System.out.println();

        for (int j = 0; j < initialState.rows; j++) {
            System.out.print(j == 0 ? "states " : "       ");
            for (int i = 0; i < steps + 1; i++) {
                int real = (int) Math.round(states[i].values[j][0].real);
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

    private boolean isBooleanAdjacencyMatrix(ComplexMatrix m) {
        if (!m.isBoolean()) {
            return false;
        }

        for (int n = 0; n < m.columns; n++) {
            if (m.column(n).sum() != Complex.ONE) {
                return false;
            }
        }

        return true;
    }
}
