package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ExtendedComplexFormat;
import utils.ExtendedNumberFormat;

import java.util.stream.Stream;

public class ComplexNumberTest {

    private static Stream<Arguments> string_from_complex() {
        return Stream.of(arguments("0", 0.0, 0.0),
                arguments("3.655", 3.655, 0.0),
                arguments("-3.655",-3.655, 0.0),
                arguments("1.23i", 0.0, 1.23),
                arguments("1.23E-100i", 0.0, 1.23E-100),
                arguments("-1.23i", 0.0, -1.23),
                arguments("3.6 + 1.23i", 3.6, 1.23),
                arguments("3.6 - 1.23i", 3.6, -1.23),
                arguments("-3.6 - 1.23i", -3.6, -1.23),
                arguments("-3.6 + 1.23i", -3.6, 1.23),
                arguments("-3.6E200 + 1.23i", -3.6E200, 1.23),
                arguments("3.6E-200 + 1.23i", 3.6E-200, 1.23),
                arguments("-3.6 - 1.23E-10i", -3.6, -1.23E-10));
    }
    @ParameterizedTest
    @MethodSource("string_from_complex")
    void can_turn_complex_number_into_string(String expected, double real, double img) {
        ExtendedComplexFormat complexFormat = new ExtendedComplexFormat(ExtendedNumberFormat.getExtendedInstance());
        assertEquals(expected, complexFormat.format(Complex.valueOf(real, img)));
    }

    private static Stream<Arguments> complex_from_string() {
        return Stream.of(
                // real only / imaginary only
                arguments(false, false, 0.0, 0.0, "0"),
                arguments(false, false, 1, 0.0, "1"),
                arguments(true, false, 0, 1, "i"),
                arguments(false, false, -1, 0.0, "-1"),
                arguments(true, false, 0, -1, "-i"),
                arguments(false, false, 12.3, 0.0, "12.3"),
                arguments(false, false, -12.3, 0.0, "-12.3"),
                arguments(false, true, 0.0, 1.6, "1.6i"),
                arguments(false, true, 0.0, -7, "-7i"),
                // both real and imaginary
                arguments(true, false, 1, 1.0, "1 + i"),
                arguments(true, false, 1.0, -1.0, "1 - i"),
                arguments(false, false, 2.5, 1.6, "2.5 + 1.6i"),
                arguments(false, false, -2.5, 1.6, "-2.5 + 1.6i"),
                arguments(false, false, 2.5, -1.6, "2.5 - 1.6i"),
                arguments(false, false, -2.5, -1.6, "-2.5 - 1.6i"),
                // exp form
                arguments(true, false, 2.5e+34, 0.6e-10, "2.5e+34 + .6e-10i"),
                // no spaces
                arguments(true, false, 1, 1.0, "1+i"),
                arguments(true, false, 1.0, -1.0, "1-i"),
                arguments(true, false, 0.0, 1.0, "0.0+i"),
                arguments(false, false, 2.5, 1.6, "2.5+1.6i"),
                arguments(false, false, -2.5, 1.6, "-2.5+1.6i"),
                arguments(false, false, -2.5, -1.6, "-2.5-1.6i"),
                arguments(true, false, 2.5e+34, 0.6e-10, "2.5e+34+.6e-10i"),
                // +- for imaginary part
                arguments(true, false, 2.5, -1.0, "2.5+-i"),
                arguments(true, false, 2.5, -1.0, "2.5 + -i"),
                // odd spaces and other stuff
                arguments(false, true, 2.5, 1.6, "   2.5   +    1.6   i"),
                arguments(true, false, -2.5e+34, 0.6e-10, "- 2.5 e +  34 + .  6 e  -10 i"),
                arguments(true, false, -2.5e-2, -1.6e+2, "-2.5e-2 + -1.6e+2i"));
    }

    @ParameterizedTest
    @MethodSource("complex_from_string")
    void can_create_complex_number_from_string(boolean legacyThrows, boolean legacyWrong, double real, double img, String value) {

        // the new way always works!
        assertEquals(Complex.valueOf(real, img), ExtendedComplexFormat.getExtendedInstance().parse(value));

        // old way fails a lot of the time :-(
        if (legacyThrows) {
            assertThrows(MathParseException.class, () -> ComplexFormat.getInstance().parse(value));
        } else {
            // and sometimes is just plain wrong!
            if (legacyWrong) {
                // e.g. legacy turns "1.6i" into 1.6 + 0i, not 0 + 1.6i !!
                assertNotEquals(Complex.valueOf(real, img), ComplexFormat.getInstance().parse(value));
            } else {
                assertEquals(Complex.valueOf(real, img), ComplexFormat.getInstance().parse(value));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"NULL", "", "huh", "1.2 3.4i", "1.2j", "1.2 - 3.6", "1.2i + 1.2i"})
    void creating_complex_number_from_string_throws_when_string_is_invalid(String str) {
        Exception thrown = assertThrows(MathParseException.class, () ->
                ExtendedComplexFormat.getExtendedInstance().parse("NULL".equals(str) ? null : str));
        assertTrue(thrown.getMessage().contains("unparseable"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.2j", "1.2 - 3.6"})
    void legacy_complex_format_does_not_throw_errors_when_it_should(String str) {
        ComplexFormat.getInstance().parse(str);
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

    /*@ParameterizedTest
    @MethodSource("polar_from_complex")
    void polar_form_string_from_cartesian(String polar, double real, double img, PolarMode mode) {
        assertEquals(polar, new Complex(real, img).toPolarString());
    }*/
}
