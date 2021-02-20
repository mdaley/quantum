package quantum.complex;

import static java.lang.Math.PI;

import java.util.Objects;
import java.util.function.BiFunction;

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
        this.real = fixupMinusZero(real);
        this.img = fixupMinusZero(img);
        this.marker = marker;
    }

    // let's avoid the problem that Double.equals(0.0, -0.0) doesn't return true! Avoids problem in Complex.equals
    // where 1.0 + 0.0i != 1.0 - 0.0i.
    private double fixupMinusZero(double value) {
        return value == -0.0 ? 0.0 : value;
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

    public static Complex complex(String input) {
        double real = 0.0, imaginary = 0.0;
        boolean ok = input != null && input.trim().length() > 0;

        if (ok) {
            String value = normaliseInput(input);

            try {
                if (value.length() > 0) {
                    String[] parts = value.split(" ");

                    if (parts.length == 1) {
                        if (parts[0].contains("i")) {
                            imaginary = imaginaryPartToDouble(parts[0]);
                        } else {
                            real = Double.parseDouble(parts[0]);
                        }
                    } else if (parts.length == 3 && parts[2].endsWith("i")) {
                        real = Double.parseDouble(parts[0]);
                        imaginary = imaginaryPartToDouble(parts[2]);
                        boolean negative = "-".equals(parts[1]);
                        imaginary = negative ? -imaginary : imaginary;
                    } else {
                        ok = false;
                    }
                }
            } catch (NumberFormatException e) {
                ok = false;
            }
        }

        if (!ok) {
            throw new IllegalArgumentException(
                    String.format("'%s' is not a valid complex number with form {double} +/- {double}i", input));
        }

        return complex(real, imaginary);
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

    public Complex add(Complex value) {
        return complex(real + value.real, img + value.img);
    }

    public Complex subtract(Complex value) {
        return complex(real - value.real, img - value.img);
    }

    public Complex multiply(Complex value) {
        return complex(real * value.real - img * value.img, real * value.img + img * value.real);
    }

    public Complex reciprocal() {
        return reciprocal(this);
    }

    private static Complex reciprocal(Complex value) {
        double divisor = value.real * value.real + value.img * value.img;
        return complex(value.real / divisor, - value.img / divisor);
    }

    public Complex divide(Complex value) {
        return multiply(reciprocal(value));
    }

    public Complex conjugate() {
        return complex(real, -img);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.real, real) == 0 && Double.compare(complex.img, img) == 0;
    }

    @Override
    public String toString() {
        if (real == 0.0 && img == 0.0) {
            return "0.0";
        } else if (img == 0.0) {
            return Double.toString(real);
        } else if (real == 0.0) {
            return img + "i";
        } else if (img > 0.0) {
            return real + " + " + img + "i";
        } else {
            return real + " - " + img * -1.0 + "i";
        }
    }

    public String toString(BiFunction<Double, Double, String> formatter) {
        return formatter.apply(real, img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, img);
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

    /**
     * Make sure all spaces are removed apart from around +/- between the imaginary and complex parts.
     * @param input the input complex number string
     * @return the normalised form
     */
    private static String normaliseInput(String input) {
        StringBuilder strb = new StringBuilder();

        if (input != null) {
            char previous = 'Z';
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c != ' ') {
                    if (c == '-' && previous == '+') {
                        // for when form is n + -mi instead of just n - mi
                        // the imaginary part is then a negative number that can be parsed
                        strb.append(c);
                    } else if (c == '+' || c == '-') {
                        if (previous == 'e' || previous == 'E' || previous == 'Z') {
                            strb.append(c);
                        } else {
                            strb.append(' ');
                            strb.append(c);
                            strb.append(' ');
                        }
                    } else {
                        strb.append(c);
                    }

                    previous = c;
                }
            }
        }

        return strb.toString();
    }

    private static double imaginaryPartToDouble(String part) {
        if (part.equals("i")) {
            return 1.0;
        } else if (part.equals("-i")) {
            return -1.0;
        } else {
            return Double.parseDouble(part.replace("i", ""));
        }
    }
}
