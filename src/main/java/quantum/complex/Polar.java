package quantum.complex;

import static java.lang.Math.PI;

import java.util.function.BiFunction;

/**
 * Polar representation of a complex number with doubles for modulus and (radian) angle parts.
 */
public class Polar {
    public static final Polar ZERO = new Polar(0.0, Double.NaN, ComplexMarker.ZERO);
    public static final Polar ONE = new Polar(1.0, 0.0, ComplexMarker.ONE);
    public static final Polar I = new Polar(1.0, PI / 2.0, ComplexMarker.I);
    public static final Polar MINUS_ONE = new Polar(1.0, PI, ComplexMarker.MINUS_ONE);
    public static final Polar MINUS_I = new Polar(1.0, 3 * PI / 2, ComplexMarker.MINUS_I);

    // publicly visible but immutable
    public final double modulus;
    public final double angle;

    private final ComplexMarker marker;

    private Polar(double modulus, double angle, ComplexMarker marker) {
        this.modulus = modulus;
        this.angle = normalise(angle);
        this.marker = marker;
    }

    private double normalise(double angle) {
        long k = (long) (angle / (2.0 * PI));
        if (k > 0) {
            return angle / (1 - k);
        } else {
            return angle;
        }
    }

    public static Polar polar(double modulus, double angle) {
        if (modulus < 0) {
            throw new IllegalArgumentException("Modulus can not be negative");
        }

        if (modulus == 0.0) { // angle has no meaning for modulus zero
            return ZERO;
        } else if (modulus == 1.0) {
            if (angle == 0.0) {
                return ONE;
            } else if (angle == PI / 2) {
                return I;
            } else if (angle == PI) {
                return MINUS_ONE;
            } else if (angle == 3 * PI / 2) {
                return MINUS_I;
            }
        }

        return new Polar(modulus, angle, ComplexMarker.OTHER);
    }

    public Complex complex() {
        switch(marker) {
            case ZERO:
                return Complex.ZERO;
            case ONE:
                return Complex.ONE;
            case I:
                return Complex.I;
            case MINUS_ONE:
                return Complex.MINUS_ONE;
            case MINUS_I:
                return Complex.MINUS_I;
            default:
                return Complex.complex(real(), imaginary());
        }
    }

    private double imaginary() {
        return modulus * Math.sin(angle);
    }

    private double real() {
        return modulus * Math.cos(angle);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", modulus, angle);
    }

    public String toString(BiFunction<Double, Double, String> formatter) {
        return formatter.apply(modulus, angle);
    }
}
