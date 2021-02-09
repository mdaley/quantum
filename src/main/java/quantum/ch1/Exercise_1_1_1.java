package quantum.ch1;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import quantum.Exercise;
import utils.ExtendedComplexFormat;
import utils.Utils;

public class Exercise_1_1_1 extends Exercise {

    private static final ComplexFormat COMPLEX_FORMAT = ExtendedComplexFormat.getExtendedInstance();

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
        Complex n1 = Utils.inputComplexNumber(COMPLEX_FORMAT, "First number: ");
        Complex n2 = Utils.inputComplexNumber(COMPLEX_FORMAT, "Second number: ");

        String n1f = COMPLEX_FORMAT.format(n1);
        String n2f = COMPLEX_FORMAT.format(n2);
        System.out.printf("(%s) + (%s) = %s\n", n1f, n2f, COMPLEX_FORMAT.format(n1.add(n2)));
        System.out.printf("(%s) - (%s) = %s\n", n1f, n2f, COMPLEX_FORMAT.format(n1.subtract(n2)));
        System.out.printf("(%s) * (%s) = %s\n", n1f, n2f, COMPLEX_FORMAT.format(n1.multiply(n2)));
        System.out.printf("(%s) / (%s) = %s\n", n1f, n2f, COMPLEX_FORMAT.format(n1.divide(n2)));
    }

}
