package quantum.complex;

import static java.lang.Math.PI;

import java.util.Set;
import java.util.TreeSet;
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

    private static final Set<String> PI_STRINGS = new TreeSet<>(String.CASE_INSENSITIVE_ORDER) {{
        addAll(Set.of("π", "𝝅", "𝝿", "𝞹", "П", "п", "ϖ", "ℼ", "ㄇ", "兀", "pi"));
    }};

    private static final Set<String> DEGREES_STRINGS = new TreeSet<>(String.CASE_INSENSITIVE_ORDER) {{
        addAll(Set.of("°", "deg", "degrees"));
    }};

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

        double normalised = angle - k * 2 * PI;
        return normalised < 0 ? normalised + 2 * PI : normalised;
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

    public static Polar polar(String value) {

        if (value != null) {
            String normalised = value.trim().replaceAll("[\\(\\)\\s]", "");
            String[] parts = normalised.split(",");

            if (parts.length == 2) {
                try {
                    double modulus = Double.parseDouble(parts[0]);
                    double angle = stringToAngle(parts[1]);
                    return polar(modulus, angle);
                } catch (NumberFormatException ignored) {
                    // ignored
                }
            }
        }

        throw new IllegalArgumentException("Not a valid polar complex number form ({double},{double}{optional-pi-symbol})");
    }

    private static double stringToAngle(String value) {
        int lastDigitIndex = value.length() - 1;
        while(lastDigitIndex >= 0) {
            char c = value.charAt(lastDigitIndex);
            if (c > 47 && c < 58) {
                break;
            }
        lastDigitIndex--;
        }

        if (lastDigitIndex == -1 && PI_STRINGS.contains(value)) { // no digits, could be just π / PI
            return PI;
        }

        if (lastDigitIndex > 0) { // there's some digits so split and multiply
            String qualifier = value.substring(lastDigitIndex + 1);
            String subValue = value.substring(0, lastDigitIndex + 1);

            if (PI_STRINGS.contains(qualifier)) {
                return Double.parseDouble(subValue) * PI;
            }

            if (DEGREES_STRINGS.contains(qualifier)) {
                return Double.parseDouble(subValue) * PI / 180.0;
            }
        }

        return Double.parseDouble(value);
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
