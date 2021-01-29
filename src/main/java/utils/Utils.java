package utils;

import static quantum.complex.ComplexNumber.fromString;

import quantum.complex.ComplexNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    public static String input(String msg) {
        String input;

        System.out.print(msg);
        if (System.console() != null) {
            input = System.console().readLine();
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                input = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return input;
    }

    public static ComplexNumber inputComplexNumber(String msg) {
        boolean ok = false;
        ComplexNumber number = null;

        while(!ok) {
            try {
                number = fromString(input(msg));
                ok = true;
            } catch(IllegalArgumentException __) {
            }
        }

        return number;
    }
}
