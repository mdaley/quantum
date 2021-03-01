package quantum.ch3;

import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexMatrix.complexMatrix;
import static quantum.complex.ComplexVector.complexColumnVector;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Exercise_3_2_2 extends Exercise {
    @Override
    public String title() {
        return "Configurable slits and targets!";
    }

    @Override
    public String description() {
        return "User asked to specify how many slits and how many targets and the probabilities of firing bullet from gun to each" +
                "slit and from each slit to each target. The initial state and the state change propabilistic matrix are created" +
                "and displayed, and then the simulation is run of what happens....";
    }

    @Override
    public void execute() {

        int slits = Utils.inputInteger("Number of slits: ");
        int targets = Utils.inputInteger("Number of targets: ");

        System.out.println("When the gun fires a bullet, there's a chance of going through each slit.\n" +
                "Please enter these chances...");
        double[] gunToSlit = new double[slits];

        for (int i = 0; i < slits; i++) {
            gunToSlit[i] = Utils.inputDouble("Gun to slit " + i + ": ");
        }

        System.out.println("The bullet has a chance of going from a slit to a target. Please enter these chances...");

        Map<Integer, double[]> slitsToTargets = new HashMap<>();
        for (int i = 0; i < slits; i++) {
            double[] slitToTargets = new double[targets];

            for (int j = 0; j < targets; j++) {
                slitToTargets[j] = Utils.inputDouble("Slit " + i + " to target " + j + ": ");
            }

            slitsToTargets.put(i, slitToTargets);
        }

        int size = 1 + slits + targets;
        ComplexMatrix initialState = complexColumnVector(size,
                IntStream.range(0, size)
                .mapToObj(i -> i == 0 ? Complex.ONE : Complex.ZERO)
                .collect(Collectors.toList())
                        .toArray(Complex[]::new));

        System.out.println("Initial state:\n\n" + initialState.toPrettyString());

        Complex[][] values = new Complex[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0) { // gun -> slits row
                    if (j > 0 && j < 1 + slits) {
                        values[i][j] = complex(gunToSlit[j - 1], 0);
                    } else {
                        values[i][j] = Complex.ZERO;
                    }
                } else if (i > 0 && i < 1 + slits) { // slits -> targets rows
                    if (j > slits && j < 1 + slits + targets) {
                        values[i][j] = complex(slitsToTargets.get(i - 1)[j - 1 - slits], 0);
                    } else {
                        values[i][j] = Complex.ZERO;
                    }
                } else {
                    if (i > slits) {
                        if (j == i) {
                            values[i][j] = Complex.ONE;
                        } else {
                            values[i][j] = Complex.ZERO;
                        }
                    }
                }
            }
        }

        ComplexMatrix stateChanger = complexMatrix(size, size, values).transpose();

        System.out.println("State Changing matrix:\n\n" + stateChanger.toPrettyString());
    }
}
