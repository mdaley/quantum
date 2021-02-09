package quantum.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import quantum.complex.ExtendedComplex;
import utils.PolarFormat;

public class PolarFormatTest {

    @Test
    void to_polar() {
        assertEquals("(1.414, 0.785π)", PolarFormat.getInstance().format(ExtendedComplex.extendedValueOf(1, 1)));
    }
}
