package quantum.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import quantum.complex.ExtendedComplex;
import utils.PolarFormat;

import java.util.stream.Stream;

public class PolarFormatTest {

    private static Stream<Arguments> polar_from_complex() {
        return Stream.of(
                // raw
                arguments("(1, 0π)", 1.0, 0.0),
                arguments("(1.414, 0.785π)", 1.0, 1.0),
                arguments("(1, 1.571π)", 0.0, 1.0),
                arguments("(1.414, 2.356π)", -1.0, 1.0),
                arguments("(1, 3.142π)", -1.0, 0.0),
                arguments("(1.414, 3.927π)", -1.0, -1.0),
                arguments("(1, 4.712π)", 0.0, -1.0),
                arguments("(1.414, 5.498π)", 1.0, -1.0));
    }

    @ParameterizedTest
    @MethodSource("polar_from_complex")
    void to_polar(String polar, double real, double img) {
        assertEquals(polar, PolarFormat.getInstance().format(ExtendedComplex.extendedValueOf(real, img)));
    }
}
