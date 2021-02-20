package quantum.ch2;

import quantum.Exercise;
import quantum.complex.ComplexMatrix;
import utils.Utils;

public class Exercise_2_2_1 extends Exercise {
    @Override
    public String title() {
        return "Multiple two matrices / vectors";
    }

    @Override
    public String description() {
        return "Input two matrices / vectors; multiply matri1 by matrix 2";
    }

    @Override
    public void execute() {
        ComplexMatrix m1 = Utils.inputMatrix("Matrix 1: ");
        ComplexMatrix m2 = Utils.inputMatrix("Matrix 2: ");

        try {
            ComplexMatrix multiplied = ComplexMatrix.multiply(m1, m2);

            System.out.println("Multiplication of the two matrices\n");
            System.out.println(multiplied.toPrettyString());
            System.out.println("\n");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
