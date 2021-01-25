package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.ComplexNumber.fromString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    @Test
    void can_create_complex_number_from_string() {
        // nothing
        assertEquals(new ComplexNumber(0.0, 0.0), fromString(null));
        assertEquals(new ComplexNumber(0.0, 0.0), fromString(""));

        // real only / imaginary only
        assertEquals(new ComplexNumber(12.3, 0.0), fromString("12.3"));
        assertEquals(new ComplexNumber(-12.3, 0.0), fromString("-12.3"));
        assertEquals(new ComplexNumber(0.0, 1.6), fromString("1.6i"));
        assertEquals(new ComplexNumber(0.0, -7), fromString("-7i"));

        // both real and imaginary
        assertEquals(new ComplexNumber(2.5, 1.6), fromString("2.5 + 1.6i"));
        assertEquals(new ComplexNumber(-2.5, 1.6), fromString("-2.5 + 1.6i"));
        assertEquals(new ComplexNumber(2.5, -1.6), fromString("2.5 - 1.6i"));
        assertEquals(new ComplexNumber(-2.5, -1.6), fromString("-2.5 - 1.6i"));
    }

    /*@Test
    void creating_complex_number_from_string_can_throw() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> fromString("NaN"));
        assertTrue(thrown.getMessage().contains("Not a valid complex number"));
        assertThrows(IllegalArgumentException.class, () -> fromString("NaNi"));
        assertThrows(IllegalArgumentException.class, () -> fromString("1.2 3.4i"));

    }*/
}
