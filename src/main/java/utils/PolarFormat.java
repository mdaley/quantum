package utils;

import quantum.complex.ExtendedComplex;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PolarFormat {

    private static final String PI_SYMBOL = "π";
    private final NumberFormat modulusFormat;
    private final NumberFormat angleFormat;
    private final String angleSymbol;
    private final String outputPattern;

    public static PolarFormat getInstance() {
        return new PolarFormat(ExtendedNumberFormat.getInstance(), ExtendedNumberFormat.getInstance(), PI_SYMBOL, "(%s, %s%s)");
    }

    public PolarFormat(NumberFormat modulusFormat, NumberFormat angleFormat, String angleSymbol, String outputPattern) {
        this.modulusFormat = modulusFormat;
        this.angleFormat = angleFormat;
        this.angleSymbol = angleSymbol;
        this.outputPattern = outputPattern;
    }

    public String format(ExtendedComplex complex) {
        return String.format(outputPattern, modulusFormat.format(complex.modulus()), angleFormat.format(complex.angle()),
                angleSymbol);
    }
}
