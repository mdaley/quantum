package quantum.ch1;

import org.apache.commons.math3.complex.ComplexFormat;
import quantum.Exercise;
import quantum.complex.ExtendedComplex;
import utils.ExtendedComplexFormat;
import utils.PolarFormat;
import utils.Utils;

public class Exercise_1_3_1 extends Exercise {
    private static final ComplexFormat COMPLEX_FORMAT = ExtendedComplexFormat.getExtendedInstance();
    private static final PolarFormat POLAR_FORMAT = PolarFormat.getInstance();

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
        ExtendedComplex n = ExtendedComplex.extendedValueOf(Utils.inputComplexNumber(COMPLEX_FORMAT, "Number: "));

        System.out.println("Polar = " + POLAR_FORMAT.format(n));

    }
}
