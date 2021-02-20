package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexMatrix.complexMatrix;
import static quantum.complex.ComplexMatrix.identityMatrix;
import static quantum.complex.ComplexMatrix.multiply;
import static quantum.complex.Polar.polar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ComplexMatrixTest {

    public static final ComplexMatrix COMPLEX_MATRIX = complexMatrix(3, 4, new Complex[][]{
            {complex(1, 1), polar(2, 0).complex(), complex(0, 1), complex(3, -6)},
            {complex(123, 34), complex(0, 1), complex(0, 2.3e-10), complex(0, 0)},
            {complex(1.234e100, 0), complex(1, 0), complex(0, 1), complex(0, -2)}
    });

    @Test
    void construct_from_string() {
        // look out, one of the numbers is in polar form!
        ComplexMatrix m = complexMatrix("1 + i| (2, 0) | i| 3 -6i|| 123 + 34i| i| 2.3E-10i| 0|| 1.234e+100| 1| i| -2i");

        assertEquals(COMPLEX_MATRIX, m);

        System.out.println(m.toPrettyString());
    }

    @Test
    void to_string_works() {
        String expectedString =
                "1.0 + 1.0i| 2.0| 1.0i| 3.0 - 6.0i|| " +
                "123.0 + 34.0i| 1.0i| 2.3E-10i| 0.0|| " +
                "1.234E100| 1.0| 1.0i| -2.0i";

        assertEquals(expectedString, COMPLEX_MATRIX.toString());
    }

    @Test
    void to_pretty_string_works() {
        String expectedString =
                "┏    1.0 + 1.0i,           2.0,          1.0i,    3.0 - 6.0i ┓\n" +
                "┃ 123.0 + 34.0i,          1.0i,      2.3E-10i,           0.0 ┃\n" +
                "┗     1.234E100,           1.0,          1.0i,         -2.0i ┛";

        assertEquals(expectedString, COMPLEX_MATRIX.toPrettyString());
    }

    @Test
    void add_and_add_in_place_work_correctly() {
        ComplexMatrix a = complexMatrix("1 + i | 2 - i || 2 + i | 3 - 2i");
        ComplexMatrix b = complexMatrix("i | 2 + 2i || i| 1");

        assertEquals(complexMatrix("1 + 2i | 4 + i || 2 + 2i | 4 - 2i"), a.add(b));

        a.addInPlace(b);
        assertEquals(complexMatrix("1 + 2i | 4 + i || 2 + 2i | 4 - 2i"), a);
    }

    @Test
    void subtract_and_subtract_in_place_work_correctly() {
        ComplexMatrix a = complexMatrix("1 + i | 2 - i || 2 + i | 3 - 2i");
        ComplexMatrix b = complexMatrix("i | 2 + 2i || i | 1");

        assertEquals(complexMatrix("1| -3i || 2 | 2 - 2i"), a.subtract(b));

        a.subtractInPlace(b);
        assertEquals(complexMatrix("1 | -3i || 2 | 2 - 2i"), a);
    }

    @Test
    void transpose_works_correctly() {
        ComplexMatrix a = complexMatrix("1 + i | 2 - i || 2 + i | 3 - 2i");
        ComplexMatrix b = complexMatrix("1 + i | 2 + i || 2 - i | 3 - 2i");

        assertEquals(b, a.transpose());

        String expectedString =
                "┏    1.0 + 1.0i, 123.0 + 34.0i,     1.234E100 ┓\n" +
                "┃           2.0,          1.0i,           1.0 ┃\n" +
                "┃          1.0i,      2.3E-10i,          1.0i ┃\n" +
                "┗    3.0 - 6.0i,           0.0,         -2.0i ┛";

        assertEquals(expectedString, COMPLEX_MATRIX.transpose().toPrettyString());
    }

    public static Stream<Arguments> matrix_multiply() {
        return Stream.of(
                // 2x2 x 2x2 -> 2x2
                arguments(complexMatrix("1 + 2i | 1 || -i | 1"), complexMatrix("1 + 2i | 1 || -i | 1"), complexMatrix("1 | 0 || 0 | 1")),
                // 1x3 x 3x1 -> 1x1
                arguments(complexMatrix("3 - 4i"), complexMatrix("1 -i | 2 | -3i"), complexMatrix("1 || 1 || 1")),
                // a more complex situation (2.35 from the book) 3x3 x 3x3 -> 3x3
                arguments(complexMatrix("26-52i | 60+24i | 26 || 9 + 7i | 1 +29i | 14 || 48-21i | 15+22i | 20-22i"),
                        complexMatrix("3+2i | 0 | 5-6i || 1 | 4+2i | i || 4-i| 0 | 4"),
                        complexMatrix("5 | 2-i | 6-4i || 0 | 4 + 5i | 2 || 7-4i | 2+7i | 0")));
    }

    @ParameterizedTest
    @MethodSource("matrix_multiply")
    void multiply_works_as_expected(ComplexMatrix expected, ComplexMatrix m1, ComplexMatrix m2) {
        assertEquals(expected, multiply(m1, m2));
    }

    public static Stream<Arguments> matrix_multiply_throws() {
        return Stream.of(
                arguments(complexMatrix(1, 2, Complex.ONE), identityMatrix(1)),
                arguments(complexMatrix(1, 1, Complex.ONE), complexMatrix(2, 1, Complex.MINUS_I)),
                arguments(complexMatrix(1, 3, Complex.ZERO), complexMatrix(2, 3, Complex.I)),
                arguments(identityMatrix(7), complexMatrix(6, 7, Complex.I)));
    }

    @ParameterizedTest
    @MethodSource("matrix_multiply_throws")
    void multiplying_incompatible_matrices_throws(ComplexMatrix m1, ComplexMatrix m2) {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> multiply(m1, m2));
        assertTrue(t.getMessage().contains("Cannot multiply"));
    }

    @Test
    void initialise_with_all_same_value() {
        assertEquals(complexMatrix("i | i | i || i | i | i"), complexMatrix(2, 3, Complex.I));
    }

    @Test
    void identity_matrix_initialised_correctly() {
        assertEquals(complexMatrix("1 | 0 | 0 || 0 | 1 | 0 || 0 | 0 | 1"), identityMatrix(3));
    }
}
