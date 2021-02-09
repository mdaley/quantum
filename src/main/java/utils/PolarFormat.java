package utils;

import quantum.complex.ExtendedComplex;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PolarFormat {

    private static final String PI_SYMBOL = "π";
    private NumberFormat modulusFormat;
    private NumberFormat angleFormat;
    private String angleSymbol;
    private String outputPattern;

    public static PolarFormat getInstance() {
        return new PolarFormat(DecimalFormat.getInstance(), DecimalFormat.getInstance(), PI_SYMBOL, "(%s, %s%s)");
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
