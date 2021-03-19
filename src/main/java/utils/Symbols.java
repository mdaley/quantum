package utils;

public class Symbols {
    public static final String PSI = "\uD835\uDF33";
    public static final String PHI = "\uD835\uDF53";
    public static final String OMEGA = "\uD835\uDEC0";
    public static final String COMPLEX = "ℂ";
    public static final String REAL = "ℝ";

    public static String ket(String symbol) {
        return "⎟" + symbol + "⟩";
    }

    public static String bra(String symbol) {
        return "⟨" + symbol + "⎜";
    }

    public static String braKet(String braSymbol, String ketSymbol) {
        return "⟨" + braSymbol + "⎜" + ketSymbol + "⟩";
    }
}
