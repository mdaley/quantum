package quantum.complex;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexEnvironment.setFloor;
import static quantum.complex.CustomAsserts.assertClose;
import static quantum.complex.Polar.polar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class ComplexTest {

    public static Stream<Arguments> construction() {
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

                // some larger values
                arguments(20000.0, 0.0, 20000.0, 0),
                arguments(34589.019, 6.2475, 34567.0, -1234.0),

                // really large/small values
                arguments(1.23E-200, PI / 2, 0.0, 1.23E-200)
                //arguments(1.23E200, 3 * PI / 2, 0.0, -1.23E200)
                //arguments(Double.POSITIVE_INFINITY, PI / 4, Double.MAX_VALUE, Double.MAX_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("construction")
    void construction_is_correct_both_cartesian_and_polar(double modulus, double angle, double real, double imaginary) {
        setFloor(Double.MIN_VALUE);

        Complex complex = complex(real, imaginary);
        assertEquals(real, complex.real);
        assertEquals(imaginary, complex.img);

        Polar polar = complex.polar();
        assertClose(modulus, polar.modulus);
        assertClose(angle, polar.angle);

        polar = polar(modulus, angle);
        assertEquals(modulus, polar.modulus);
        assertEquals(angle, polar.angle);

        complex = polar.complex();
        assertClose(real, complex.real);
        assertClose(imaginary, complex.img);

    }

    private static Stream<Arguments> complex_from_string() {
        return Stream.of(
                // real only / imaginary only
                arguments(0.0, 0.0, "0"),
                arguments(1, 0.0, "1"),
                arguments(0, 1, "i"),
                arguments(0, 2, "2i"),
                arguments(0, -2, "-2i"),
                arguments(-1, 0.0, "-1"),
                arguments(0, -1, "-i"),
                arguments(12.3, 0.0, "12.3"),
                arguments(12.0, 0.0, "   12  "),
                arguments(-12.3, 0.0, "-12.3"),
                arguments(-17.0, 0.0, "  -17  "),
                arguments(0.0, 1.6, "1.6i"),
                arguments(0.0, -7, "-7i"),
                // both real and imaginary
                arguments(1, 1.0, "1 + i"),
                arguments(1.0, -1.0, "1 - i"),
                arguments(2.5, 1.6, "2.5 + 1.6i"),
                arguments(-2.5, 1.6, "-2.5 + 1.6i"),
                arguments(2.5, -1.6, "2.5 - 1.6i"),
                arguments(-2.5, -1.6, "-2.5 - 1.6i"),
                // exp form
                arguments(2.5e+34, 0.6e-10, "2.5e+34 + .6e-10i"),
                // no spaces
                arguments(1, 1.0, "1+i"),
                arguments(1.0, -1.0, "1-i"),
                arguments(0.0, 1.0, "0.0+i"),
                arguments(2.5, 1.6, "2.5+1.6i"),
                arguments(-2.5, 1.6, "-2.5+1.6i"),
                arguments(-2.5, -1.6, "-2.5-1.6i"),
                arguments(2.5e+34, 0.6e-10, "2.5e+34+.6e-10i"),
                // +- for imaginary part
                arguments(2.5, -1.0, "2.5+-i"),
                arguments(2.5, -1.0, "2.5 + -i"),
                // odd spaces and other stuff
                arguments(2.5, 1.6, "   2.5   +    1.6   i"),
                arguments(-2.5e+34, 0.6e-10, "- 2.5 e +  34 + .  6 e  -10 i"),
                arguments(-2.5e-2, -1.6e+2, "   -2.5e-2 + -1.6e+2i"),
                // scientific form special cases
                arguments(2.3e-10, 0.0, "2.3e-10"),
                arguments(0.0, -2.3e-10, "-2.3e-10i"),
                arguments(0.0, -2.3e-10, "-2.3E-10i"),
                arguments(0.0, 2.3e100, "2.3E100i"),
                arguments(0.0, 2.3e100, "2.3E+100i"));
    }

    @ParameterizedTest
    @MethodSource("complex_from_string")
    void can_create_complex_number_from_string(double real, double img, String value) {
        assertEquals(complex(real, img), complex(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"NULL", "", "huh", "1.2 3.4i", "1.2j", "1.2 - 3.6", "1.2i + 1.2i"})
    void creating_complex_number_from_string_throws_when_string_is_invalid(String str) {
        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                complex("NULL".equals(str) ? null : str));
        assertTrue(thrown.getMessage().contains("not a valid complex number"));
    }
}
