package quantum.complex;

import static java.lang.Math.PI;
import static java.lang.Math.atan;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public final class ComplexNumber {
    public static final NumberFormat NUMBER_FORMAT = DecimalFormat.getInstance();
    public final double r;
    public final double i;

    public ComplexNumber(double r, double i) {
        this.r = r;
        this.i = i;
    }

    public static ComplexNumber fromString(String input) {
        double real = 0.0, imaginary = 0.0;
        boolean ok = true;

        String value = normaliseInput(input);

        try {
            if (value.length() > 0) {
                String[] parts = value.split(" ");

                if (parts.length == 1) {
                    if (parts[0].contains("i")) {
                        imaginary = doubleFromImaginary(parts[0]);
                    } else {
                        real = Double.parseDouble(parts[0]);
                    }
                } else if (parts.length == 3 && parts[2].endsWith("i")) {
                    real = Double.parseDouble(parts[0]);
                    imaginary = doubleFromImaginary(parts[2]);
                    boolean negative = "-".equals(parts[1]);
                    imaginary = negative ? -imaginary : imaginary;
                } else {
                    ok = false;
                }
            }
        } catch (NumberFormatException e) {
            ok = false;
        }

        if (!ok) {
            throw new IllegalArgumentException("Not a valid complex number with form {double} +/- {double}i");
        }

        return new ComplexNumber(real, imaginary);
    }

    public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.r + b.r, a.i + b.i);
    }

    public static ComplexNumber mul(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.r * b.r - a.i * b.i, a.r * b.i + a.i * b.r);
    }

    public static ComplexNumber div(ComplexNumber a, ComplexNumber b) {
        double divisor = (b.r * b.r) + (b.i * b.i);
        return new ComplexNumber(((a.r * b.r) + (a.i * b.i)) / divisor, ((b.r * a.i) - (a.r * b.i)) / divisor);
    }

    public static ComplexNumber sub(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.r - b.r, a.i - b.i);
    }

    public Double modulus() {
        return Math.sqrt(r * r + i * i);
    }

    public Double angle() {
        if (r == 0 && i == 0) {
            return Double.NaN;
        } else if (r == 0) {
            return i > 0 ? PI / 2 : 3 * PI / 2;
        } else if (i == 0) {
            return r > 0 ? 0.0 : PI;
        } else if (r > 0) {
            if (i > 0) {
                // quadrant 1
                return atan(i / r);
            } else {
                // quadrant 4
                return 2.0 * PI - atan( -1.0 * i / r);
            }
        } else { // r < 0
            if (i > 0) {
                // quadrant 2
                return PI - atan(-1.0 * i / r);
            } else {
                // quadrant 3
                return PI + atan(i / r);
            }
        }
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(r, -i);
    }

    public String toPolarString(PolarMode mode) {
        double angle = angle();
        return String.format("(%s, %s)", NUMBER_FORMAT.format(modulus()), mode.converterFn.apply(angle));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComplexNumber that = (ComplexNumber) o;

        return Double.compare(that.r, r) == 0 && Double.compare(that.i, i) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, i);
    }

    @Override
    public String toString() {
        if (r == 0.0 && i == 0.0) {
            return "0.0";
        } else if (i == 0.0) {
            return Double.toString(r);
        } else if (r == 0.0) {
            return i + "i";
        } else if (i > 0.0) {
            return r + " + " + i + "i";
        } else {
            return r + " - " + i * -1.0 + "i";
        }
    }

    private static double doubleFromImaginary(String img) {
        double value;

        if (img.equals("i")) {
            value = 1.0;
        } else if (img.equals("-i")) {
            value = -1.0;
        } else {
            value = Double.parseDouble(img.replace("i", ""));
        }

        return value;
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
                        if (previous == 'e' || i == 0) {
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
}
