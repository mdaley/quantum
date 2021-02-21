package quantum.ch2;

import quantum.Exercise;
import quantum.complex.ComplexMatrix;
import utils.Utils;

public class Exercise_2_4_1 extends Exercise {
    @Override
    public String title() {
        return "Inner product of a complex vector / matrix";
    }

    @Override
    public String description() {
        return "V * V(transpose) or diagonals of M * M(adjoint), results always a real number.";
    }

    @Override
    public void execute() {
        ComplexMatrix m = Utils.inputMatrix("Matrix/Vector: ");

        System.out.println("Scalar product = " + m.innerProduct());
        System.out.println("\n\n");
    }
}
