package quantum.ch2;

import quantum.Exercise;
import quantum.complex.ComplexMatrix;
import utils.Utils;

public class Exercise_2_1_2 extends Exercise {
    @Override
    public String title() {
        return "Inverse of a vector";
    }

    @Override
    public String description() {
        return "The vector that can be added to this vector to makes the zeros vector";
    }

    @Override
    public void execute() {
        ComplexMatrix m = Utils.inputColumnVector("Column vector: ");

        ComplexMatrix inverse = m.negate();
        System.out.println("Inverse matrix\n");
        System.out.println(inverse.toPrettyString());
        System.out.println("\n");
    }
}
