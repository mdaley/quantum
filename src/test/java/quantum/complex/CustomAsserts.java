package quantum.complex;


import org.junit.jupiter.api.Assertions;

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
}
