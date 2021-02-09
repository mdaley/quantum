package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.ExtendedNumberFormat;

import java.text.NumberFormat;
import java.util.stream.Stream;

public class ExtendedComplexTest {

    private static final NumberFormat NUMBER_FORMAT = ExtendedNumberFormat.getExtendedInstance();

    public static Stream<Arguments> modulus_from_complex() {
        return Stream.of(arguments("0", 0.0, 0.0),
                arguments("1.414", 1, 1),
                arguments("100", 0, 100),
                arguments("100", 100, 0),
                arguments("100", -100, 0),
                arguments("100", 0, -100),
                // calc uses doubles
                arguments("1.344E153", 9.5E152, 9.5E152),
                // calc overflows and has to use BigDecimals
                arguments("1.344E202", 9.5E201, 9.5E201),
                // calc overflows to BigDecimals but the answer is out of range of Double
                arguments("∞", Double.MAX_VALUE, Double.MAX_VALUE));
    }

    @ParameterizedTest
    @MethodSource("modulus_from_complex")
    void modulus_calculated_correctly(String modulus, double real, double img) {
        assertEquals(modulus, NUMBER_FORMAT.format(ExtendedComplex.extendedValueOf(real, img).modulus()));
    }

    private static Stream<Arguments> polar_from_complex() {
        return Stream.of(
                arguments("NaN", 0.0, 0.0), // the angle is indeterminate in this case
                arguments("0", 1.0, 0.0),
                arguments("0.785", 1.0, 1.0),
                arguments("1.571", 0.0, 1.0),
                arguments("2.356", -1.0, 1.0),
                arguments("3.142", -1.0, 0.0),
                arguments("3.927", -1.0, -1.0),
                arguments("4.712", 0.0, -1.0),
                arguments("5.498", 1.0, -1.0),
                arguments("3.927", -12345.0, -12345.0),
                arguments("1.571", 0.0, 1.23E-200),
                arguments("4.712", 0.0, -1.23E200),
                arguments("0.785", Double.MAX_VALUE, Double.MAX_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("polar_from_complex")
    void polar_angle_calculated_correctly(String angle, double real, double img) {
        assertEquals(angle, NUMBER_FORMAT.format(ExtendedComplex.extendedValueOf(real, img).angle()));
    }
}
