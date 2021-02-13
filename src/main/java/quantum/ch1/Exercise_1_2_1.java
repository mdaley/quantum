package quantum.ch1;

import quantum.Exercise;
import quantum.complex.Complex;
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
    public void execute() {
        Complex n = Utils.inputComplexNumber("Complex Number: ");

        System.out.printf("Modulus = %s\n", n.polar().modulus);
        System.out.printf("Conjugate = %s\n", n.conjugate());
    }
}
