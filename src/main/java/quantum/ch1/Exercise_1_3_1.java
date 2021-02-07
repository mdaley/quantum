package quantum.ch1;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import quantum.Exercise;
import utils.ExtendedNumberFormat;
import utils.Utils;

public class Exercise_1_3_1 extends Exercise {
    private static final ComplexFormat COMPLEX_FORMAT = new ComplexFormat(ExtendedNumberFormat.getExtendedInstance());

    @Override
    public String title() {
        return "Cartesian to Polar";
    }

    @Override
    public String description() {
        return "Show the polar representation of a complex number";
    }

    @Override
    public boolean repeats() {
        return false;
    }

    @Override
    public void execute() {
        Complex n = Utils.inputComplexNumber(COMPLEX_FORMAT, "First number: ");

        //System.out.println("Polar = " + n.toPolarString(PolarMode.PI_TEXT));

    }
}
