package quantum.complex;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexEnvironment.setFloor;
import static quantum.complex.ComplexMatrix.complexMatrix;
import static quantum.complex.ComplexMatrix.identityMatrix;
import static quantum.complex.ComplexMatrix.multiply;
import static quantum.complex.ComplexMatrix.tensorProduct;
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

    @Test
    void rows_and_columns_can_be_retrieved() {
        ComplexMatrix m = identityMatrix(5);

        assertEquals(complexMatrix(" 1 | 0 | 0 | 0 | 0"), m.row(0));
        assertEquals(complexMatrix(" 0 | 0 | 0 | 1 | 0"), m.row(3));
        assertEquals(complexMatrix(" 0 || 1 || 0 || 0 || 0"), m.column(1));
        assertEquals(complexMatrix(" 0 || 0 || 0 || 0 || 1"), m.column(4));
    }

    @Test
    void adjoint_works_correctly() {
        assertEquals(complexMatrix("1 + i | 2 | 2 - i || i | 0 | 3"), complexMatrix("1 - i | -i || 2 | 0 || 2 + i | 3").adjoint());
    }

    @Test
    void inner_product_calculated_correctly() {
        ComplexMatrix matrix = complexMatrix("1+i|(2,0)||i|3-6i");
        assertEquals(52, matrix.innerProduct());
    }

    @Test
    void is_hermitian_works_orrectly() {
        assertTrue(complexMatrix("654").isHermitian());
        assertTrue(complexMatrix("7 | 6 + 5i || 6 - 5i | -4").isHermitian());
        assertTrue(complexMatrix("5 | 4 + 5i | 6 - 16i || 4 - 5i | 13 | 7 || 6 + 16i | 7 | -2.1").isHermitian());

        assertFalse(complexMatrix("i").isHermitian());
        assertFalse(complexMatrix("1 | 1").isHermitian());
        assertFalse(complexMatrix("1 || 1").isHermitian());
        assertFalse(complexMatrix("i5 | 4 + 5i | 6 - 16i | 2 || 4 - 5i | 13 | 7 | 2 || 6 + 16i | 7 | -2.1 | i").isHermitian());
    }

    @Test
    void is_unitary_works_correctly() {
        // testing for zero only works reliably if a noise floor is set below which doubles are set to zero. If you don't
        // do this, approximations in calculations will result in values not actually being zero when, theoretically,
        // they should be. So, isUnitary would never succeed, even when it should.
        setFloor(1e-15);

        Complex[][] data = new Complex[][] {
                {complex("1+i").divide(2), Complex.I.divide( sqrt(3)), complex("3+i").divide(2 * sqrt(15))},
                {complex("-0.5"), Complex.ONE.divide(sqrt(3)), complex("4+3i").divide(2 * sqrt(15))},
                {complex("0.5"), Complex.MINUS_I.divide(sqrt(3)), complex("5i").divide((2 * sqrt(15)))}
        };
        assertTrue(complexMatrix(3, 3, data).isUnitary());
    }

    public static Stream<Arguments> tensor_product() {

        return Stream.of(
                arguments("0", "0", "0"),
                arguments("-4", "2i", "2i"),
                arguments("i | 2i || -i | -2i", "1| 2", "i || -i"),
                // exercise 2.7.1
                arguments("-3 || 6 || -4 || 8 || -7 || 14", "3 || 4|| 7", "-1 || 2"),
                // exercise 2.7.3
                arguments("3 + 2i | 1 + 18i | 29 - 11i | 5 - 1i | 19 + 17i | 18 - 40i | 2i | -8 + 6i | 14 + 10i ||" +
                        " 26 + 26i | 18 + 12i | -4 + 19i | 52 | 30 - 6i | 15 + 23i | -4 + 20i | 12i | -10 + 4i ||" +
                        " 0 | 3 + 2i | -12 + 31i | 0 | 5 - 1i | 19 + 43i | 0 | 2i | -18 + 4i ||" +
                        " 0 | 0 | 0 | 12 | 36 + 48i | 60 - 84i | 6 - 3i | 30 + 15i | 9 - 57i ||" +
                        " 0 | 0 | 0 | 120 + 24i | 72 | 24 + 60i | 66 - 18i | 36 - 18i | 27 + 24i ||" +
                        " 0 | 0 | 0 | 0 | 12 | 24 + 108i | 0 | 6 - 3i | 39 + 48i ||" +
                        " 2 | 6 + 8i | 10 - 14i | 4 + 4i | -4 + 28i | 48 - 8i | 9 + 3i | 15 + 45i | 66 - 48i ||" +
                        " 20 + 4i | 12 | 4 + 10i | 32 + 48i | 24 + 24i | -12 + 28i | 84 + 48i | 54 + 18i | 3 + 51i ||" +
                        " 0 | 2 | 4 + 18i | 0 | 4 + 4i | -28 + 44i | 0 | 9 + 3i | -9 + 87i",
                        "3 + 2i | 5 - i | 2i || 0 | 12 | 6 - 3i || 2 | 4 + 4i | 9 + 3i",
                        "1 | 3 + 4i | 5 - 7i || 10 + 2i | 6 | 2 + 5i || 0 | 1 | 2 + 9i"));
    }

    @ParameterizedTest
    @MethodSource("tensor_product")
    void tensor_product_works_correctly(String result, String m1, String m2) {
        ComplexMatrix actual = tensorProduct(complexMatrix(m1), complexMatrix(m2));
        assertEquals(complexMatrix(result), actual);
        System.out.println(actual.toPrettyString());
    }
}
