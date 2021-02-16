package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexVector.complexColumnVector;
import static quantum.complex.ComplexVector.complexRowVector;
import static quantum.complex.Polar.polar;

import org.junit.jupiter.api.Test;

public class ComplexVectorTest {
    public static final ComplexVector COMPLEX_ROW_VECTOR = complexRowVector(4, new Complex[] {
            complex(1, 1), complex(2, 0), complex(0, 1), complex(3, -6)
            });

    @Test
    void construct_row_vector_from_string() {
        // look out, one of the numbers is in polar form!
        ComplexVector vector = complexRowVector("1 + i| (2, 0) | i| 3 -6i");

        assertEquals(COMPLEX_ROW_VECTOR, vector);

        assertEquals("┃ 1.0 + 1.0i,        2.0,       1.0i, 3.0 - 6.0i ┃", vector.toPrettyString());
    }

    @Test
    void construct_column_vector_from_string() {
        ComplexVector vector = complexColumnVector("1 + i| (2, 0) | i| 3 -6i");

        assertEquals(
                "┏ 1.0 + 1.0i ┓\n" +
                "┃        2.0 ┃\n" +
                "┃       1.0i ┃\n" +
                "┗ 3.0 - 6.0i ┛", vector.toPrettyString());
    }

    @Test
    void transpose_works_correctly() {
        ComplexVector vector = complexColumnVector("1 + i| (2, 0) | i| 3 -6i");

        // to a row vector
        assertEquals("┃ 1.0 + 1.0i,        2.0,       1.0i, 3.0 - 6.0i ┃", vector.transpose().toPrettyString());

        // to a row vector and then back to a column vector
        assertEquals(
                "┏ 1.0 + 1.0i ┓\n" +
                        "┃        2.0 ┃\n" +
                        "┃       1.0i ┃\n" +
                        "┗ 3.0 - 6.0i ┛", vector.transpose().transpose().toPrettyString());
    }
}
