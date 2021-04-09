package quantum.complex;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

public class CustomAsserts {
    public static void assertClose(double expected, double actual) {
        if (Double.doubleToLongBits(expected) == Double.doubleToLongBits(actual)) {
            return;
        }

        if ((expected == 0.0 && Math.abs(actual) < 0.001) || (actual == 0.0 && Math.abs(expected) < 0.001)) {
            return;
        }

        double diffFactor = Math.abs((expected - actual) / expected);

        if (diffFactor < 0.001) {
            return;
        }

        Assertions.assertEquals(expected, actual);
    }

    public static void assertClose(Complex expected, Complex actual) {
        assertClose(expected, actual, 0.0001);
    }

    public static void assertClose(Complex expected, Complex actual, double accuracy) {
        if (expected.equals(actual, accuracy)) {
            return;
        }

        throw new AssertionFailedError(String.format("expected: <%s> but was: <%s>", expected, actual), expected, actual);
    }

    public static void assertClose(ComplexMatrix expected, ComplexMatrix actual) {
        assertClose(expected, actual, 0.0001);
    }

    public static void assertClose(ComplexMatrix expected, ComplexMatrix actual, double accuracy) {
        if (expected.equals(actual, accuracy)) {
            return;
        }

        throw new AssertionFailedError(String.format("expected: <%s> but was: <%s>", expected, actual), expected, actual);
    }
}
