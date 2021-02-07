package utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * And extended number format that can choose whether to format to scientific or normal form
 * depending on the magnitude of the number to which it is applied.
 */
public class ExtendedNumberFormat extends DecimalFormat {

    private static final String DEFAULT_EXP_PATTERN = "0.###E0";
    private static final double EXP_HIGH_BOUNDARY = 1e+10;
    private static final double EXP_LOW_BOUNDARY = 1e-5;

    private DecimalFormat expFormat;

    private double highDoubleBoundary = EXP_HIGH_BOUNDARY;
    private double lowDoubleBoundary = EXP_LOW_BOUNDARY;
    private long highLongBoundary = (long)EXP_HIGH_BOUNDARY;

    private ExtendedNumberFormat() {
        super();
        expFormat = (DecimalFormat) DecimalFormat.getInstance();
        expFormat.applyPattern(DEFAULT_EXP_PATTERN);
    }

    public void applyPatterns(String stdPattern, String expPattern) {
        applyPattern(stdPattern);
        expFormat.applyPattern(expPattern);
    }

    public void applyExpBoundaries(double high, double low) {
        this.highDoubleBoundary = high;
        this.lowDoubleBoundary = low;
    }

    public void applyExpBoundary(long high) {
        this.highLongBoundary = high;
    }

    public static ExtendedNumberFormat getExtendedInstance() {
        return new ExtendedNumberFormat();
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        if (Math.abs(number) < highDoubleBoundary && Math.abs(number) > lowDoubleBoundary) {
            return super.format(number, toAppendTo, pos);
        } else {
            return expFormat.format(number, toAppendTo, pos);
        }
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        // number == Long.MIN_VALUE is a special case because doing abs on this results
        // in the same negative number, not a max value. Long.MAX_VALUE is one lower than
        // Long.MIN_VALUE without the sign. See documentation of Math.abs(long).
        if (number != Long.MIN_VALUE && Math.abs(number) < highLongBoundary) {
            return super.format(number, toAppendTo, pos);
        } else {
            return expFormat.format(number, toAppendTo, pos);
        }
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return super.parse(source, parsePosition);
    }
}
