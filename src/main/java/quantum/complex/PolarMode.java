package quantum.complex;

import static java.lang.Math.PI;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Function;

public enum PolarMode {
    PI_TEXT("PI", Converters.PI_TEXT),
    REALLY_RAW("", Converters.REALLY_RAW),
    RAW("", Converters.RAW);

    private static class Converters {
        private static final NumberFormat FORMATTER = DecimalFormat.getInstance(Locale.getDefault());

        private static final Function<Double, String> PI_TEXT = d -> {
            String formatted = FORMATTER.format(d / PI);
            return "0".equals(formatted) ? formatted : formatted + "PI";
        };
        private static final Function<Double, String> RAW = d -> FORMATTER.format(d);
        private static final Function<Double, String> REALLY_RAW = d -> Double.toString(d);
    }

    public final String label;
    public Function<Double, String> converterFn;

    PolarMode(String label, Function<Double, String> converterFn) {
        this.label = label;
        this.converterFn = converterFn;
    }
}
