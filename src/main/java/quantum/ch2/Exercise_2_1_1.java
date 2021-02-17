package quantum.ch2;

import quantum.Exercise;
import quantum.complex.ComplexMatrix;
import utils.Utils;

public class Exercise_2_1_1 extends Exercise {
    @Override
    public String title() {
        return "Vector addition & scalar multiplication";
    }

    @Override
    public String description() {
        return "Input two vectors; add and scalar multiply with them";
    }

    @Override
    public void execute() {
        ComplexMatrix m1 = Utils.inputColumnVector("Column vector 1: ");
        ComplexMatrix m2 = Utils.inputColumnVector("Column vector 2: ");

        ComplexMatrix matrixAdd = m1.add(m2);
        System.out.println("Addition of the two matrices\n");
        System.out.println(matrixAdd.toPrettyString());
        System.out.println("\n");

        ComplexMatrix matrixScalarMult = m1.scalarMultiply(m2);
        System.out.println("Scalar multiplication of the two matrices\n");
        System.out.println(matrixScalarMult.toPrettyString());
        System.out.println("\n");
    }
}
