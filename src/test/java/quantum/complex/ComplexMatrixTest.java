package quantum.complex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexMatrix.complexMatrix;
import static quantum.complex.Polar.polar;

import org.junit.jupiter.api.Test;

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
        ComplexMatrix a = complexMatrix("1 + i| 2 - i||2 + i| 3 - 2i");
        ComplexMatrix b = complexMatrix("i| 2 + 2i||i|1");

        assertEquals(complexMatrix("1 + 2i| 4 + i||2 + 2i| 4 - 2i"), a.add(b));

        a.addInPlace(b);
        assertEquals(complexMatrix("1 + 2i| 4 + i||2 + 2i| 4 - 2i"), a);
    }

    @Test
    void subtract_and_subtract_in_place_work_correctly() {
        ComplexMatrix a = complexMatrix("1 + i| 2 - i||2 + i| 3 - 2i");
        ComplexMatrix b = complexMatrix("i| 2 + 2i||i|1");

        assertEquals(complexMatrix("1| -3i||2 | 2 - 2i"), a.subtract(b));

        a.subtractInPlace(b);
        assertEquals(complexMatrix("1 | -3i||2 | 2 - 2i"), a);
    }

    @Test
    void transpose_works_correctly() {
        ComplexMatrix a = complexMatrix("1 + i| 2 - i||2 + i| 3 - 2i");
        ComplexMatrix b = complexMatrix("1 + i| 2 + i||2 - i| 3 - 2i");

        assertEquals(b, a.transpose());

        String expectedString =
                "┏    1.0 + 1.0i, 123.0 + 34.0i,     1.234E100 ┓\n" +
                "┃           2.0,          1.0i,           1.0 ┃\n" +
                "┃          1.0i,      2.3E-10i,          1.0i ┃\n" +
                "┗    3.0 - 6.0i,           0.0,         -2.0i ┛";

        assertEquals(expectedString, COMPLEX_MATRIX.transpose().toPrettyString());
    }
}
