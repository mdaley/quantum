package utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;

import java.util.Scanner;

public class Utils {
    private static Scanner scanner = null;

    private static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        return scanner;
    }

    public static String input(String msg) {
        String input;

        System.out.print(msg);
        if (System.console() != null) {
            input = System.console().readLine();
        } else {
            input = getScanner().nextLine();
        }

        return input;
    }

    public static Complex inputComplexNumber(String msg) {
        boolean ok = false;
        Complex number = null;

        while (!ok) {
            try {
                number = ComplexFormat.getInstance().parse(input(msg));
                ok = true;
            } catch (MathParseException ignored) {
            }
        }

        return number;
    }
}
