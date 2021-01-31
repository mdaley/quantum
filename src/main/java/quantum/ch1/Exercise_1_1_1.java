package quantum.ch1;

import quantum.Exercise;
import quantum.complex.ComplexNumber;
import utils.Utils;

public class Exercise_1_1_1 extends Exercise {

    @Override
    public String title() {
        return "Basic arithmetic";
    }

    @Override
    public String description() {
        return "Add, substract, multiply and divide two complex numbers";
    }

    @Override
    public boolean repeats() {
        return true;
    }

    @Override
    public void execute() {
        ComplexNumber n1 = Utils.inputComplexNumber("First number: ");
        ComplexNumber n2 = Utils.inputComplexNumber("Second number: ");

        System.out.printf("%s + %s = %s\n", n1, n2, ComplexNumber.add(n1, n2));
        System.out.printf("%s - %s = %s\n", n1, n2, ComplexNumber.sub(n1, n2));
        System.out.printf("%s * %s = %s\n", n1, n2, ComplexNumber.mul(n1, n2));
        System.out.printf("%s / %s = %s\n", n1, n2, ComplexNumber.div(n1, n2));
    }

}
