package utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;

import java.text.FieldPosition;
import java.text.ParsePosition;

public class ExtendedComplexFormat extends ComplexFormat {

    public static ExtendedComplexFormat getExtendedInstance() {
        ExtendedNumberFormat f = ExtendedNumberFormat.getExtendedInstance();
        return new ExtendedComplexFormat(f);
    }

    public ExtendedComplexFormat(ExtendedNumberFormat format) {
        super(format);
    }

    @Override
    public StringBuffer format(Complex complex, StringBuffer toAppendTo, FieldPosition pos) {
        if (complex.getImaginary() == 0.0) {
            return super.getRealFormat().format(complex.getReal(), toAppendTo, pos);
        } else if (complex.getReal() == 0.0) {
            StringBuffer format = super.getImaginaryFormat().format(complex.getImaginary(), toAppendTo, pos);
            format.append(this.getImaginaryCharacter());
            return format;
        } else {
            StringBuffer buf = super.format(complex, toAppendTo, pos);
            String[] parts = buf.toString().split(" ");
            // special cases
            if (parts.length == 3) {
                if (parts[2].equals("0i")) {
                    buf.delete(buf.length() - 5, buf.length()); // remove img part and sign
                } else if (parts[2].equals("1i")) {
                    buf.delete(buf.length() - 2, buf.length() - 1); // remove number before img part
                } else if (parts[0].equals("0")) {
                    buf.delete(0, 2); // remove real part
                    closeUpOrRemoveSign(buf, parts);
                } else if (parts[0].equals("-0")) {
                    buf.delete(0, 3); // remove real part
                    closeUpOrRemoveSign(buf, parts);
                }
            }

            return buf;
        }
    }

    private void closeUpOrRemoveSign(StringBuffer buf, String[] parts) {
        // where there is no real part to show,this is so we get '- 34.5i' => '-34.5i' and '+ 12.123i' => '12.123i'
        if (parts[1].equals("+")) {
            buf.delete(0, 2);
        } else {
            buf.delete(1, 2);
        }
    }

    @Override
    public Complex parse(String source) throws MathParseException {
        return parse(source, null);
    }

    @Override
    public Complex parse(String source, ParsePosition pos) {

        double real = 0.0, imaginary = 0.0;
        boolean ok = source != null && source.trim().length() > 0;

        if (ok) {
            String value = normaliseInput(source);

            try {
                if (value.length() > 0) {
                    String[] parts = value.split(" ");

                    if (parts.length == 1) {
                        if (parts[0].contains("i")) {
                            imaginary = doubleFromImaginary(parts[0]);
                        } else {
                            real = Double.parseDouble(parts[0]);
                        }
                    } else if (parts.length == 3 && parts[2].endsWith("i")) {
                        real = Double.parseDouble(parts[0]);
                        imaginary = doubleFromImaginary(parts[2]);
                        boolean negative = "-".equals(parts[1]);
                        imaginary = negative ? -imaginary : imaginary;
                    } else {
                        ok = false;
                    }
                }
            } catch (NumberFormatException e) {
                ok = false;
            }
        }

        if (!ok) {
            throw new MathParseException(source, 0, Complex.class);
        }

        return Complex.valueOf(real, imaginary);
    }

    /**
     * Make sure all spaces are removed apart from around +/- between the imaginary and complex parts.
     * @param input the input complex number string
     * @return the normalised form
     */
    private static String normaliseInput(String input) {
        StringBuilder strb = new StringBuilder();

        if (input != null) {
            char previous = 'Z';
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c != ' ') {
                    if (c == '-' && previous == '+') {
                        // for when form is n + -mi instead of just n - mi
                        // the imaginary part is then a negative number that can be parsed
                        strb.append(c);
                    } else if (c == '+' || c == '-') {
                        if (previous == 'e' || i == 0) {
                            strb.append(c);
                        } else {
                            strb.append(' ');
                            strb.append(c);
                            strb.append(' ');
                        }
                    } else {
                        strb.append(c);
                    }

                    previous = c;
                }
            }
        }

        return strb.toString();
    }

    private static double doubleFromImaginary(String img) {
        double value;

        if (img.equals("i")) {
            value = 1.0;
        } else if (img.equals("-i")) {
            value = -1.0;
        } else {
            value = Double.parseDouble(img.replace("i", ""));
        }

        return value;
    }
}
