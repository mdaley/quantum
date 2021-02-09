package quantum.complex;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.apache.commons.math3.complex.Complex;

import java.math.BigDecimal;
import java.math.MathContext;

public class ExtendedComplex extends Complex {
    public ExtendedComplex(double real) {
        super(real);
    }

    public ExtendedComplex(double real, double imaginary) {
        super(real, imaginary);
    }

    public static ExtendedComplex extendedValueOf(double real, double imaginary) {
        return new ExtendedComplex(real, imaginary);
    }

    public static ExtendedComplex extendedValueOf(Complex complex) {
        return extendedValueOf(complex.getReal(), complex.getImaginary());
    }

    public static ExtendedComplex fromPolar(double modulus, double angle) {
        // avoid negative zero by testing modulus == 0.0
        return ExtendedComplex.extendedValueOf(modulus == 0.0 ? 0.0 : modulus * Math.cos(angle),
                modulus == 0.0 ? 0.0 : modulus * Math.sin(angle));
    }

    public double modulus() {
        double r = getReal();
        double i = getImaginary();

        double sumSquares = r * r + i * i;

        if (sumSquares == Double.POSITIVE_INFINITY) {
            BigDecimal br = BigDecimal.valueOf(r);
            BigDecimal bi = BigDecimal.valueOf(i);
            return br.multiply(br).add(bi.multiply(bi)).sqrt(MathContext.DECIMAL128).doubleValue();
        } else {
            return Math.sqrt(sumSquares);
        }
    }

    public Double angle() {
        double r = getReal();
        double i = getImaginary();

        if (r == 0 && i == 0) {
            return Double.NaN;
        } else if (r == 0) {
            return i > 0 ? PI / 2 : 3 * PI / 2;
        } else if (i == 0) {
            return r > 0 ? 0.0 : PI;
        } else if (r > 0) {
            if (i > 0) {
                // quadrant 1
                return Math.atan(i / r);
            } else {
                // quadrant 4
                return 2.0 * PI - Math.atan( -1.0 * i / r);
            }
        } else { // r < 0
            if (i > 0) {
                // quadrant 2
                return PI - Math.atan(-1.0 * i / r);
            } else {
                // quadrant 3
                return PI + Math.atan(i / r);
            }
        }
    }
}
