package quantum.complex;

import static java.lang.Math.PI;

/**
 * A complex number with double values for the real and imaginary parts.
 */
public class Complex {
    public static final Complex ZERO = new Complex(0.0, 0.0, ComplexMarker.ZERO);
    public static final Complex ONE = new Complex(1.0, 0.0, ComplexMarker.ONE);
    public static final Complex MINUS_ONE = new Complex(-1.0, 0.0, ComplexMarker.MINUS_ONE);
    public static final Complex I = new Complex(0.0, 1.0, ComplexMarker.I);
    public static final Complex MINUS_I = new Complex(0.0, -1.0, ComplexMarker.MINUS_I);

    // publicly visible but immutable
    public final double real;
    public final double img;

    private final ComplexMarker marker;

    private Complex(double real, double img, ComplexMarker marker) {
        this.real = real;
        this.img = img;
        this.marker = marker;
    }

    public static Complex complex(double real, double img) {
        if (real == 0.0) {
            if (img == 0.0) {
                return ZERO;
            } else if (img == 1.0) {
                return I;
            } else if (img == -1.0) {
                return MINUS_I;
            }
        }

        if (img == 0.0) {
            if (real == 1.0) {
                return ONE;
            } else if (real == -1.0) {
                return MINUS_ONE;
            }
        }

        return new Complex(real, img, ComplexMarker.OTHER);
    }

    public static Complex complex(String str) {
        return new Complex(0, 0, ComplexMarker.MINUS_I);
    }

    public Polar polar() {
        switch (marker) {
            case ZERO:
                return Polar.ZERO;
            case ONE:
                return Polar.ONE;
            case I:
                return Polar.I;
            case MINUS_ONE:
                return Polar.MINUS_ONE;
            case MINUS_I:
                return Polar.MINUS_I;
            default:
                return Polar.polar(modulus(), angle());
        }
    }

    private double modulus() {
        return Math.hypot(real, img);
    }

    private double angle() {
        if (real == 0 && img == 0) {
            return Double.NaN;
        } else if (real == 0) {
            return img > 0 ? PI / 2 : 3 * PI / 2;
        } else if (img == 0) {
            return real > 0 ? 0.0 : PI;
        } else if (real > 0) {
            if (img > 0) {
                // quadrant 1
                return Math.atan(img / real);
            } else {
                // quadrant 4
                return 2.0 * PI - Math.atan( -1.0 * img / real);
            }
        } else { // r < 0
            if (img > 0) {
                // quadrant 2
                return PI - Math.atan(-1.0 * img / real);
            } else {
                // quadrant 3
                return PI + Math.atan(img / real);
            }
        }
    }
}
