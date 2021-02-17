package utils;

import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexVector.complexColumnVector;

import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;

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
                number = complex(input(msg));
                ok = true;
            } catch (IllegalArgumentException ignored) {
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
            }
        }

        return matrix;
    }
}
