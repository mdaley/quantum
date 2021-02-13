package quantum.ch1;

import static utils.Utils.inputComplexNumber;

import quantum.Exercise;
import quantum.complex.Complex;
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
    public void execute() {
        Complex n1 = inputComplexNumber( "First number: ");
        Complex n2 = inputComplexNumber("Second number: ");

        String n1f = n1.toString();
        String n2f = n2.toString();
        System.out.printf("(%s) + (%s) = %s\n", n1f, n2f, n1.add(n2));
        System.out.printf("(%s) - (%s) = %s\n", n1f, n2f, n1.subtract(n2));
        System.out.printf("(%s) * (%s) = %s\n", n1f, n2f, n1.multiply(n2));
        System.out.printf("(%s) / (%s) = %s\n", n1f, n2f, n1.divide(n2));
    }

}
