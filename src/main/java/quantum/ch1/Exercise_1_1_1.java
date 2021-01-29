package quantum.ch1;

import quantum.Exercise;
import quantum.complex.ComplexNumber;
import utils.Utils;

public class Exercise_1_1_1 extends Exercise {

    @Override
    public String title() {
        return "Add/Multiply complex numbers";
    }

    @Override
    public String description() {
        return "Adds and multiplies two complex numbers";
    }

    @Override
    public boolean repeats() {
        return true;
    }

    @Override
    public void execute() {
        ComplexNumber n1 = Utils.inputComplexNumber("First number: ");
        ComplexNumber n2 = Utils.inputComplexNumber("Second number: ");

        ComplexNumber sum = ComplexNumber.add(n1, n2);
        ComplexNumber mul = ComplexNumber.mul(n1, n2);

        System.out.printf("%s + %s = %s%n", n1, n2, sum);
        System.out.printf("%s * %s = %s%n", n1, n2, mul);
    }

}
