package utils;

import static quantum.complex.ComplexNumber.fromString;

import quantum.complex.ComplexNumber;

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

    public static ComplexNumber inputComplexNumber(String msg) {
        boolean ok = false;
        ComplexNumber number = null;

        while (!ok) {
            try {
                number = fromString(input(msg));
                ok = true;
            } catch (IllegalArgumentException ignored) {
            }
        }

        return number;
    }
}
