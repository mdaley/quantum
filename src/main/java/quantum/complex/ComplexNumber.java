package quantum.complex;

import java.util.Objects;

public final class ComplexNumber {
    public final double r;
    public final double i;

    public ComplexNumber(double r, double i) {
        this.r = r;
        this.i = i;
    }

    public static ComplexNumber fromString(String input) {
        double real = 0.0, imaginary = 0.0;
        boolean ok = true;

        String value = normaliseInput(input);

        try {
            if (value.length() > 0) {
                String[] parts = value.split(" ");

                if (parts.length == 1) {
                    if (parts[0].contains("i")) {
                        imaginary = Double.parseDouble(parts[0].replace("i", ""));
                    } else {
                        real = Double.parseDouble(parts[0]);
                    }
                } else if (parts.length == 3 && parts[2].endsWith("i")) {
                    real = Double.parseDouble(parts[0]);
                    imaginary = Double.parseDouble(parts[2].replace("i", ""));
                    boolean negative = "-".equals(parts[1]);
                    imaginary = negative ? -imaginary : imaginary;
                } else {
                    ok = false;
                }
            }
        } catch (NumberFormatException e) {
            ok = false;
        }

        if (!ok) {
            throw new IllegalArgumentException("Not a valid complex number with form {double} +/- {double}i");
        }

        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.r + b.r, a.i + b.i);
    }

    public ComplexNumber mul(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.r * b.r - a.i * b.i, a.r * b.i + a.i * b.r);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComplexNumber that = (ComplexNumber) o;

        return Double.compare(that.r, r) == 0 && Double.compare(that.i, i) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, i);
    }

    @Override
    public String toString() {
        if (r == 0.0 && i == 0.0) {
            return "0.0";
        } else if (i == 0.0) {
            return Double.toString(r);
        } else if (r == 0.0) {
            return i + "i";
        } else if (i > 0.0) {
            return r + " + " + i + "i";
        } else {
            return r + " - " + i * -1.0 + "i";
        }
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
                    if (c == '+' || c == '-') {
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
}
