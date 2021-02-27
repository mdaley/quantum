package quantum.ch3;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;
import utils.Utils;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

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
                && stateChange.isBoolean()) {
                notOk = false;
            } else {
                System.out.println("The matrix must be square with the same dimensions as the length of the\n" +
                        "state vector, and it must contain only ones and zeros. Please try again.\n");
            }
        }

        int steps = Utils.inputInteger("Number of steps to process: ");

        ComplexMatrix[] values = new ComplexMatrix[steps + 1];
        int[] widths = new int[steps + 1];

        for (int i = 0; i < initialState.rows; i++) {
            int thisWidth = Integer.toString((int)Math.round(initialState.values[i][0].real)).length();
            widths[0] = thisWidth > widths[0] ? thisWidth : widths[0];
        }

        values[0] = initialState;
        for (int i = 1; i < steps + 1; i++) {
            values[i] = stateChange.multiply(values[i - 1]);

            for (int j = 0; j < values[i].columns; j++) {
                int thisWidth = Integer.toString((int)Math.round(values[i].values[j][0].real)).length();
                widths[i] = thisWidth > widths[i] ? thisWidth : widths[i];
            }
        }

        Function<Complex[][], String> formatter = v -> {
            StringBuilder strb = new StringBuilder();
            for (int i = 0; i < v.length; i++) {
                for (int j = 0; j < v[i].length; j++) {
                    strb.append((int)Math.round(v[i][j].real));
                    strb.append(" ");
                }
                strb.append(("\n"));
            }

            return strb.toString();
        };

        System.out.println("Widths = " + Arrays.toString(widths));

        System.out.print("States after each tick:\nticks");

        for (int i = 0; i < steps + 1; i++) {
            System.out.print(String.format("%" + widths[i] + "s ", i));
        }

        System.out.println();

        for (int j = 0; j < initialState.rows; j++) {
            System.out.print("     ");
            for (int i = 0; i < steps + 1; i++) {
                int real = (int) Math.round(values[i].values[j][0].real);
                System.out.print(String.format("%" + widths[i] + "s ", real));
            }
            System.out.println("");
        }
    }
}
