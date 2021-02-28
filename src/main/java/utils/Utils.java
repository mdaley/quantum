package utils;

import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexMatrix.complexMatrix;
import static quantum.complex.ComplexVector.complexColumnVector;

import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;

import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

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
                number = complex(input(msg));
                ok = true;
            } catch (IllegalArgumentException ignored) {
                System.out.println("Not a valid comple number.");
            }
        }

        return number;
    }

    public static ComplexMatrix inputColumnVector(String msg) {
        boolean ok = false;
        ComplexMatrix matrix = null;

        while(!ok) {
            try {
                matrix = complexColumnVector(input(msg));
                ok = true;
            } catch (IllegalArgumentException ignored) {
                System.out.println("Not a valid column vector.");
            }
        }

        return matrix;
    }

    public static int inputInteger(String msg) {
        while(true) {
            try {
                return Integer.parseInt(input(msg));
            } catch (NumberFormatException e) {
            }
        }
    }

    public static ComplexMatrix inputMatrix(String msg) {
        boolean ok = false;
        ComplexMatrix matrix = null;

        while(!ok) {
            try {
                matrix = complexMatrix(input(msg));
                ok = true;
            } catch (IllegalArgumentException ignored) {
                System.out.println("Not a valid matrix.");
            }
        }

        return matrix;
    }

    public static boolean inputBoolean(String msg) {
        while(true) {
            String input = input(msg);
            if (input != null) {
                String value = input.toLowerCase(Locale.getDefault());
                if (Set.of("true", "yes", "y", "t").contains(value)) {
                  return true;
                } else if (Set.of("false", "no", "n", "f").contains(value)) {
                return false;
                }
            }
        }
    }
}
