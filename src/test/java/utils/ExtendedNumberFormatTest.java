package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

public class ExtendedNumberFormatTest {

    @Test
    void doubles_are_formatted_nicely() {
        DecimalFormat stdFormatter = (DecimalFormat) DecimalFormat.getInstance();
        stdFormatter.applyPattern("0.############");
        stdFormatter.setGroupingUsed(false);

        DecimalFormat newFormatter = ExtendedNumberFormat.getExtendedInstance();
        newFormatter.applyPattern("0.#######");
        newFormatter.setGroupingUsed(false);

        // The standard formatter, showing not so nice results
        assertEquals("1.23", stdFormatter.format(1.23));
        assertEquals("12345678.123", stdFormatter.format(12345678.123));
        // this is what we don't want!
        assertEquals("1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                stdFormatter.format(1.23e+300));
        assertEquals("0.000000000123", stdFormatter.format(1.23E-10));

        // the new formatter, showing improvements by moving to scientific form for very large or small numbers
        assertEquals("1.23", newFormatter.format(1.23));
        assertEquals("1234567891.234", newFormatter.format(1234567891.234));
        assertEquals("1.235E10", newFormatter.format(12345678912.345)); // half-up truncation
        assertEquals("-1234567891.234", newFormatter.format(-1234567891.234));
        assertEquals("-1.234E10", newFormatter.format(-12344444444.345));
        assertEquals("1.23E300", newFormatter.format(1.23e+300));
        assertEquals("0.0000123", newFormatter.format(1.23e-5));
        assertEquals("1.23E-6", newFormatter.format(1.23e-6));
    }

    @Test
    void longs_are_formatted_nicely() {
        DecimalFormat stdFormatter = (DecimalFormat) DecimalFormat.getInstance();
        stdFormatter.applyPattern("0.############");
        stdFormatter.setGroupingUsed(false);

        DecimalFormat newFormatter = ExtendedNumberFormat.getExtendedInstance();
        newFormatter.applyPattern("0.#######");
        newFormatter.setGroupingUsed(false);

        // The standard formatter, showing not so nice results
        assertEquals("1", stdFormatter.format(1L));
        assertEquals("123456789", stdFormatter.format(123456789L));
        assertEquals("12345678912", stdFormatter.format(12345678912L));
        assertEquals("9223372036854775807", stdFormatter.format(Long.MAX_VALUE));
        assertEquals("-9223372036854775808", stdFormatter.format(Long.MIN_VALUE));

        // the new formatter, showing improvements by moving to scientific form for very large or small numbers
        assertEquals("1", newFormatter.format(1L));
        assertEquals("1234567891", newFormatter.format(1234567891L));
        assertEquals("1.235E10", newFormatter.format(12345678912L)); // half-up truncation
        assertEquals("-1234567891", newFormatter.format(-1234567891L));
        assertEquals("-1.234E10", newFormatter.format(-12344444444L));
        assertEquals("9.223E18", newFormatter.format(Long.MAX_VALUE));

        assertEquals("-9.223E18", newFormatter.format(Long.MIN_VALUE + 1));

        // this is a special case; see comments in ExtendedFormatter.format(long).
        assertEquals("-9.223E18", newFormatter.format(Long.MIN_VALUE));
    }

    @Test
    void can_apply_specific_normal_and_exp_patterns() {
        ExtendedNumberFormat newFormatter = ExtendedNumberFormat.getExtendedInstance();
        newFormatter.applyPatterns("0.####", "0.####E0");
        newFormatter.setGroupingUsed(false);

        assertEquals("1111111.1111", newFormatter.format(1111111.111111));
        assertEquals("1.1111E10", newFormatter.format(11111111111.11));
        assertEquals("1.2346E10", newFormatter.format(12345678912.345)); // half up rounding
    }

    @Test
    void can_set_boundaries_for_displaying_scientific_form_for_doubles() {
        ExtendedNumberFormat newFormatter = ExtendedNumberFormat.getExtendedInstance();
        newFormatter.setGroupingUsed(false);

        // standard
        assertEquals("12345.678", newFormatter.format(12345.678));
        assertEquals("0", newFormatter.format(0.000123));

        newFormatter.applyExpBoundaries(1E4, 1E-2);

        // after applying boundaries
        assertEquals("1.235E4", newFormatter.format(12345.678)); // half-up truncation
        assertEquals("1.23E-4", newFormatter.format(0.000123));
    }

    @Test
    void can_set_boundaries_for_displaying_scientific_form_for_longs() {
        ExtendedNumberFormat newFormatter = ExtendedNumberFormat.getExtendedInstance();
        newFormatter.setGroupingUsed(false);

        // standard
        assertEquals("12345", newFormatter.format(12345L));

        newFormatter.applyExpBoundary(10000);

        // after applying boundary
        assertEquals("1.234E4", newFormatter.format(12345L));
    }

    @Test
    void parse_still_works() throws Exception {
        ExtendedNumberFormat newFormatter = ExtendedNumberFormat.getExtendedInstance();

        assertEquals(1.2345, newFormatter.parse("1.2345"));
        assertEquals(1.23E300, newFormatter.parse("1.23E300"));
        assertEquals(0.00000123, newFormatter.parse("0.00000123"));
        assertEquals(-1.23E-10, newFormatter.parse("-1.23E-10"));
    }
}
