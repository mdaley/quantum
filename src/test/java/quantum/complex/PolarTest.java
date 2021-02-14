package quantum.complex;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.CustomAsserts.assertClose;
import static quantum.complex.Polar.polar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class PolarTest {

    private static Stream<Arguments> construction() {
        return Stream.of(
                // decimal formats
                arguments(0.0, Double.NaN, "(0,0)"),
                arguments(1.0, 0.0, " (  1, 0  )"),
                arguments(12.345, 0.0, "(12.345,     0.000"),
                arguments(123.45, 3 * PI / 2, "(123.45," + 23 * PI / 2 + ")"),
                arguments(1.0, PI, "(1.0,-" + PI + ")"),
                arguments(1.0, PI, "(1.0,-" + 17 * PI + ")"),
                // no brackets, for lazy people
                arguments(1.0, 0.0, "1,0"),
                arguments(12.0, 3.0, "12,3"),
                arguments(12345.678, 8 - 2 * PI, "  12345.678,  8.0"),
                // pi dealt with correctly
                arguments(1.0, PI, "(1.0,π)"),
                arguments(1.0, PI, "  1.0,   Π  "),
                arguments(3.0, PI, "  3.0,   PI  "),
                arguments(1.0, 3 * PI / 4, "(1.0,0.75π)"),
                arguments(1.0, 3 * PI / 4, "1.0, 0.75 Pi"),
                // degrees dealt with correctly
                arguments(1.234e12, PI / 4, "1.234e12, 45°"), // degrees symbol - who's gonna' use that?
                arguments(3.0, PI / 2, "3, 90 degrees"),
                arguments(3.0, PI / 2, "3, 90 Degrees"),
                arguments(3.0, PI / 2, "3, 90deg"),
                arguments(0.123, 3 * PI / 2, "0.123, 270 dEgReEs"));
    }

    @ParameterizedTest
    @MethodSource("construction")
    void polar_can_be_constructed_from_string(double modulus, double angle, String value) {
        Polar polar = polar(value);
        assertEquals(modulus, polar.modulus);
        assertClose(angle, polar.angle);
    }
}
