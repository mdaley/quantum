package quantum.ch1;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import quantum.Exercise;
import utils.Utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class Exercise_1_2_1 extends Exercise {

    public static final ComplexFormat COMPLEX_FORMAT = ComplexFormat.getInstance();

    @Override
    public String title() {
        return "Modulus and conjugate";
    }

    @Override
    public String description() {
        return "Calculate the modulus and conjugate of a complex number";
    }

    @Override
    public boolean repeats() {
        return true;
    }

    @Override
    public void execute() {
        Complex n = Utils.inputComplexNumber("Number: ");

        System.out.printf("Modulus = %s\n", modulus(n));
        System.out.printf("Conjugate = %s\n", COMPLEX_FORMAT.format(n.conjugate()));
    }

    private double modulus(Complex n) {
        double squareSum = n.getReal() * n.getReal() + n.getImaginary() * n.getImaginary();

        if (Double.isInfinite(squareSum)) {
            // if number is too big, move to using big decimals.
            BigDecimal bigReal = BigDecimal.valueOf(n.getReal());
            BigDecimal bigImg = BigDecimal.valueOf(n.getImaginary());
            BigDecimal sum = bigReal.multiply(bigReal).add(bigImg.multiply(bigImg));

            return sum.sqrt(MathContext.DECIMAL128).doubleValue();
        } else {
            return Math.sqrt(squareSum);
        }
    }
}
