package quantum.ch4;

import static utils.Symbols.BRA_KET;
import static utils.Symbols.BRA_PHI;
import static utils.Symbols.KET_PSI;
import static utils.Utils.inputBoolean;
import static utils.Utils.inputInteger;
import static utils.Utils.inputMatrix;

import quantum.Exercise;
import quantum.complex.Complex;
import quantum.complex.ComplexMatrix;

public class Exercise_4_1_1 extends Exercise {
    @Override
    public String title() {
        return "Linear quantum system simulator";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void execute() {
        ComplexMatrix initialKet; // will be a column vector

        while (true) {
            initialKet = inputMatrix("Initial state ket " + KET_PSI + ": ").transpose();
            if (initialKet.columns == 1 && initialKet.rows > 0) {
                break;
            } else {
                System.out.println("Input must be a row vector!");
            }
        }

        // calculate squared norm
        double squaredNorm = initialKet.scalarMultiply(initialKet.conjugate()).sum().real;

        System.out.println("Quantum model has " + initialKet.rows + " state" + (initialKet.rows == 1 ? "" : "s" + "."));
        System.out.println("Initial quantum state " + KET_PSI + " = \n\n" + initialKet.transpose().toPrettyString());
        System.out.println("Norm = " + Math.sqrt(squaredNorm));

        do {
            if (inputBoolean("Do you want to get probability that the system is in a given state? ")) {
                int state;

                do {
                    while (true) {
                        state = inputInteger("Choose state number (0 to " + (initialKet.rows - 1) + "): ");
                        if (state >= 0 && state < initialKet.rows) {
                            break;
                        }
                        {
                            System.out.println("State number out of range!");
                        }
                    }

                    Complex chosenState = initialKet.values[state][0];
                    double chosenStateSquaredNorm = chosenState.multiply(chosenState.conjugate()).real;

                    double prob = chosenStateSquaredNorm / squaredNorm;
                    System.out.println("Probability of being in state " + state + " = " + prob);
                } while (inputBoolean("Another? "));
            }

            if (inputBoolean("Do you want to see amplitude for transitioning to a new state? ")) {
                ComplexMatrix newKet;

                while (true) {
                    newKet = inputMatrix("Input the new state " + BRA_PHI + ": ").transpose();

                    if (newKet.columns == 1 && newKet.rows == initialKet.rows) {
                        break;
                    } else {
                        System.out.println("New state must have same dimensions as initial state!");
                    }
                }

                ComplexMatrix newBra = newKet.adjoint();

                ComplexMatrix transitionProbabiity = newBra.multiply(initialKet);

                System.out.println("Transition amplitude " + BRA_KET + " = " + transitionProbabiity.toPrettyString());
            }
        } while(inputBoolean("Again? "));
    }
}
