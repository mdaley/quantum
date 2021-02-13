package quantum.ch1;

import static utils.Utils.inputComplexNumber;

import quantum.Exercise;
import quantum.complex.Complex;

import java.util.function.BiFunction;

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

        BiFunction<Double, Double, String> formatter = (r, i) -> {
            if (i == 0.0) {
                return Double.toString(r);
            } else if (r == 0.0) {
                return i + "i";
            } else {
                return String.format("%s %s %si", r, i < 0 ? "-" : "+", Math.abs(i));
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
