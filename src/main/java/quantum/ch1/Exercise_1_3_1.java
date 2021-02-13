package quantum.ch1;

import quantum.Exercise;
import quantum.complex.Complex;
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
    public void execute() {
        Complex n = Utils.inputComplexNumber("Complex Number: ");

        System.out.println("Polar = " + n.polar().toString());

    }
}
