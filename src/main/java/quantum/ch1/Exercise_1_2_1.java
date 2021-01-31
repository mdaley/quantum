package quantum.ch1;

import quantum.Exercise;
import quantum.complex.ComplexNumber;
import utils.Utils;

public class Exercise_1_2_1 extends Exercise {
    @Override
    public String title() {
        return "Modulus and conjugate";
    }

    @Override
    public String description() {
        return "Calculate the modulus and conjugate of a complex number";
    }

    @Override
    public boolean repeats() {
        return true;
    }

    @Override
    public void execute() {
        ComplexNumber n = Utils.inputComplexNumber("Number: ");

        System.out.printf("Modulus = %f\n", n.modulus());
        System.out.printf("Conjugate = %s\n", n.conjugate());
    }
}
