package quantum.complex;

import org.junit.jupiter.api.Test;

public class ComplexMatrixTest {

    @Test
    void construct_from_string() {
        ComplexMatrix m = ComplexMatrix.fromString("1 + i, 2, i, 3 -6i; 123 + 34i, i, 2.3E-10i, 0; 1.234e+100, 1, i, -2i");
        System.out.println(m.toPrettyString());
    }
}
