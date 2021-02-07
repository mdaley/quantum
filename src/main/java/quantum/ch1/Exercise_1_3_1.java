package quantum.ch1;

import org.apache.commons.math3.complex.Complex;
import quantum.Exercise;
import quantum.complex.ComplexNumber;
import quantum.complex.PolarMode;
import utils.Utils;

public class Exercise_1_3_1 extends Exercise {
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
        Complex n = Utils.inputComplexNumber("First number: ");

        //System.out.println("Polar = " + n.toPolarString(PolarMode.PI_TEXT));

    }
}
