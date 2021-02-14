package quantum.ch1;

import static java.lang.Math.PI;

import quantum.Exercise;
import quantum.complex.Complex;
import utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.BiFunction;

public class Exercise_1_3_1 extends Exercise {

    public static final String PI_SYMBOL = "π";
    public static final NumberFormat DP3 = new DecimalFormat("0.####");
    public static final NumberFormat DP_3_SCI = new DecimalFormat("0.####E0");

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

        BiFunction<Double, Double, String> formatter = (m, a) -> {
            String mf;
            String af;

            if (Math.abs(m) < 0.0001 || Math.abs(m) > 1E6) {
                mf = DP_3_SCI.format(m);
            } else {
                mf = DP3.format(m);
            }

            double aPi = a / PI;

            if (aPi == 0.0) {
                af = "0";
            } else if (Math.abs(aPi) < 0.001) {
                af = DP_3_SCI.format(aPi) + PI_SYMBOL;
            } else {
                af = DP3.format(aPi) + PI_SYMBOL;
            }

            return String.format("(%s, %s)", mf, af);
        };
        System.out.println("Polar = " + n.polar().toString(formatter));

    }
}
