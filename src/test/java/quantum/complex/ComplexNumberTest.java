package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.ComplexNumber.fromString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class ComplexNumberTest {

    private static Stream<Arguments> string_from_complex() {
        return Stream.of(arguments("0.0", 0.0, 0.0),
                arguments("3.655", 3.655, 0.0),
                arguments("-3.655",-3.655, 0.0),
                arguments("1.23i", 0.0, 1.23),
                arguments("-1.23i", 0.0, -1.23),
                arguments("3.6 + 1.23i", 3.6, 1.23),
                arguments("3.6 - 1.23i", 3.6, -1.23),
                arguments("-3.6 - 1.23i", -3.6, -1.23),
                arguments("-3.6 + 1.23i", -3.6, 1.23));
    }
    @ParameterizedTest
    @MethodSource("string_from_complex")
    void can_turn_complex_number_into_string(String expected, double real, double img) {
        assertEquals(expected, new ComplexNumber(real, img).toString());
    }

    private static Stream<Arguments> complex_from_string() {
        return Stream.of(
                // nothing
                arguments(0.0, 0.0, null),
                arguments(0.0, 0.0, ""),
                // real only / imaginary only
                arguments(0.0, 0.0, "0"),
                arguments(1, 0.0, "1"),
                arguments(0, 1, "i"),
                arguments(-1, 0.0, "-1"),
                arguments(0, -1, "-i"),
                arguments(12.3, 0.0, "12.3"),
                arguments(-12.3, 0.0, "-12.3"),
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
                arguments(-2.5e-2, -1.6e+2, "-2.5e-2 + -1.6e+2i"));
    }

    @ParameterizedTest
    @MethodSource("complex_from_string")
    void can_create_complex_number_from_string(double real, double img, String value) {
        assertEquals(new ComplexNumber(real, img), fromString(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"huh", "1.2 3.4i", "1.2j", "1.2 - 3.6", "1.2i + 1.2i"})
    void creating_complex_number_from_string_can_throw(String str) {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> fromString(str));
        assertTrue(thrown.getMessage().contains("Not a valid complex number"));
    }

    private static Stream<Arguments> polar_from_complex() {
        return Stream.of(
                // raw
                arguments("(1, 0)", 1.0, 0.0, PolarMode.RAW),
                arguments("(1.414, 0.785)", 1.0, 1.0, PolarMode.RAW),
                arguments("(1, 1.571)", 0.0, 1.0, PolarMode.RAW),
                arguments("(1.414, 2.356)", -1.0, 1.0, PolarMode.RAW),
                arguments("(1, 3.142)", -1.0, 0.0, PolarMode.RAW),
                arguments("(1.414, 3.927)", -1.0, -1.0, PolarMode.RAW),
                arguments("(1, 4.712)", 0.0, -1.0, PolarMode.RAW),
                arguments("(1.414, 5.498)", 1.0, -1.0, PolarMode.RAW),
                // really raw
                arguments("(1.414, 5.497787143782138)", 1.0, -1.0, PolarMode.REALLY_RAW),
                // PI text
                arguments("(1, 0)", 1.0, 0.0, PolarMode.PI_TEXT),
                arguments("(1.414, 0.25PI)", 1.0, 1.0, PolarMode.PI_TEXT)//,
                //arguments("(1, 1}", Math.sqrt(Double.MAX_VALUE) / 2, Math.sqrt(Double.MAX_VALUE) / 2, PolarMode.PI_TEXT)
        );
    }

    @ParameterizedTest
    @MethodSource("polar_from_complex")
    void polar_form_string_from_cartesian(String polar, double real, double img, PolarMode mode) {
        assertEquals(polar, new ComplexNumber(real, img).toPolarString(mode));
    }
}
