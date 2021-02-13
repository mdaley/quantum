package quantum.complex;

import static java.lang.Math.PI;

import java.math.BigDecimal;
import java.math.MathContext;

public class Complex2 {
    private final double realOrModulus;
    private final double imaginaryOrAngle;
    private final boolean cartesian;

    private transient Double modulusOrReal;
    private transient Double angleOrImaginary;

    private Complex2(double realOrModulus, double imaginaryOrAngle, boolean cartesian) {
        this.realOrModulus = realOrModulus;
        this.imaginaryOrAngle = imaginaryOrAngle;
        this.cartesian = cartesian;
    }

    public static Complex2 fromCartesian(double real, double imaginary) {
        return new Complex2(real, imaginary, true);
    }

    public static Complex2 fromPolar(double modulus, double angle) {
        if (modulus < 0.0) {
            throw new IllegalArgumentException("Modulus must be >= 0.0");
        }

        return new Complex2(modulus, angle, false);
    }

    public double real() {
        return cartesian ? realOrModulus : calculateReal();
    }

    public double imaginary() {
        return cartesian ? imaginaryOrAngle : calculateImaginary();
    }

    public double modulus() {
        return !cartesian ? realOrModulus : calculateModulus();
    }

    public double angle() {
        return !cartesian ? imaginaryOrAngle : calculateAngle();
    }

    private double calculateReal() {
        if (modulusOrReal == null) {
            modulusOrReal = realOrModulus == 0.0 ? 0.0 : realOrModulus * Math.cos(imaginaryOrAngle);
        }

        return modulusOrReal;
    }

    private double calculateImaginary() {
        if (angleOrImaginary == null) {
            angleOrImaginary = realOrModulus == 0.0 ? 0.0 : realOrModulus * Math.sin(imaginaryOrAngle);
        }

        return angleOrImaginary;
    }

    private double calculateAngle() {
        if (angleOrImaginary == null) {
            angleOrImaginary = innerCalculateAngle(realOrModulus, imaginaryOrAngle);
        }

        return angleOrImaginary;
    }

    private double calculateModulus() {
        if (modulusOrReal == null) {
            modulusOrReal = innerCalculateModulus(realOrModulus, imaginaryOrAngle);
        }

        return modulusOrReal;
    }

    private static double innerCalculateModulus(double real, double imaginary) {
        double sumSquares = real * real + imaginary * imaginary;

        if (sumSquares == Double.POSITIVE_INFINITY) {
            BigDecimal br = BigDecimal.valueOf(real);
            BigDecimal bi = BigDecimal.valueOf(imaginary);
            return br.multiply(br).add(bi.multiply(bi)).sqrt(MathContext.DECIMAL128).doubleValue();
        } else {
            return Math.sqrt(sumSquares);
        }
    }

    private static double innerCalculateAngle(double real, double imaginary) {
        if (real == 0 && imaginary == 0) {
            return Double.NaN;
        } else if (real == 0) {
            return imaginary > 0 ? PI / 2 : 3 * PI / 2;
        } else if (imaginary == 0) {
            return real > 0 ? 0.0 : PI;
        } else if (real > 0) {
            if (imaginary > 0) {
                // quadrant 1
                return Math.atan(imaginary / real);
            } else {
                // quadrant 4
                return 2.0 * PI - Math.atan( -1.0 * imaginary / real);
            }
        } else { // r < 0
            if (imaginary > 0) {
                // quadrant 2
                return PI - Math.atan(-1.0 * imaginary / real);
            } else {
                // quadrant 3
                return PI + Math.atan(imaginary / real);
            }
        }
    }
}
