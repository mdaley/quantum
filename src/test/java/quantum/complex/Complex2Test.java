package quantum.complex;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.CustomAsserts.assertClose;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.stream.Stream;

public class Complex2Test {

    public static Stream<Arguments> cartesian_construction() {
        return Stream.of(
                // zero
                arguments(0.0, Double.NaN, 0.0, 0.0),

                // axes
                arguments(1.0, 0.0, 1.0, 0.0),
                arguments(1.0, PI / 2, 0.0, 1.0),
                arguments(1.0, PI, -1.0, 0.0),
                arguments(1.0, 3 * PI / 2, 0.0, -1.0),

                // 45 degrees
                arguments(1.414, PI / 4, 1.0, 1.0),
                arguments(1.414, 3 * PI / 4, -1.0, 1.0),
                arguments(1.414, 5 * PI / 4, -1.0, -1.0),
                arguments(1.414, 7 * PI / 4, 1.0, -1.0),

                // larger values
                arguments(20000.0, 0.0, 20000.0, 0),
                arguments(34589.019, 6.2475, 34567.0, -1234.0),

                // really large/small values
                arguments(0.0, PI / 2, 0.0, 1.23E-200),
                arguments(1.23E200, 3 * PI / 2, 0.0, -1.23E200),
                arguments(Double.POSITIVE_INFINITY, PI / 4, Double.MAX_VALUE, Double.MAX_VALUE));

    }

    @ParameterizedTest
    @MethodSource("cartesian_construction")
    void construction_is_correct_in_both_modes(double modulus, double angle, double real, double imaginary) {
        Complex2 complex = Complex2.fromCartesian(real, imaginary);
        assertClose(real, complex.real());
        assertClose(imaginary, complex.imaginary());
        assertClose(modulus, complex.modulus());
        assertClose(angle, complex.angle());

        complex = Complex2.fromPolar(modulus, angle);
        assertClose(real, complex.real());
        assertClose(imaginary, complex.imaginary());
        assertClose(modulus, complex.modulus());
        assertClose(angle, complex.angle());
    }

    private static double round(double value) {
        DecimalFormat format = new DecimalFormat("0.###");
        format.setRoundingMode(RoundingMode.HALF_UP);
        try {
            return format.parse(format.format(value)).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
