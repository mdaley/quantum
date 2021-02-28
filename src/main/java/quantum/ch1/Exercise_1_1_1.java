package quantum.ch1;

import static utils.Utils.inputComplexNumber;

import quantum.Exercise;
import quantum.complex.Complex;

import java.util.function.Function;

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

        Function<Complex, String> formatter = c -> {
            if (c.img == 0.0) {
                return Double.toString(c.real);
            } else if (c.real == 0.0) {
                return c.img + "i";
            } else {
                return String.format("%s %s %si", c.real, c.img < 0 ? "-" : "+", Math.abs(c.img));
            }
        };

        String n1f = n1.toString(formatter);
        String n2f = n2.toString(formatter);

        System.out.printf("(%s) + (%s) = %s\n", n1f, n2f, n1.add(n2).toString(formatter));
        System.out.printf("(%s) - (%s) = %s\n", n1f, n2f, n1.subtract(n2).toString(formatter));
        System.out.printf("(%s) * (%s) = %s\n", n1f, n2f, n1.multiply(n2).toString(formatter));
        System.out.printf("(%s) / (%s) = %s\n", n1f, n2f, n1.divide(n2).toString(formatter));
    }

}
