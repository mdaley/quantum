package quantum.ch4;

import static quantum.complex.Complex.complex;
import static quantum.complex.ComplexMatrix.diagonalMatrix;
import static utils.Symbols.OMEGA;
import static utils.Symbols.PSI;
import static utils.Symbols.ket;
import static utils.Utils.inputMatrix;

import quantum.Exercise;
import quantum.complex.ComplexMatrix;

public class Exercise_4_2_1 extends Exercise  {
    @Override
    public String title() {
        return "Mean value and variance of an observable";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void execute() {
        ComplexMatrix ket; // a column vector

        while (true) {
            ket = inputMatrix("Input the state " + ket(PSI) + ": ").transpose();
            if (ket.columns == 1 && ket.rows > 0) {
                break;
            } else {
                System.out.println("Input must be a row vector!");
            }
        }

        ComplexMatrix observable;

        while (true) {
            observable = inputMatrix("Input the observable " + OMEGA + ": ");
            if (observable.isHermitian() && observable.rows == ket.rows) {
                break;
            } else {
                System.out.println("Observable must be a hermitian matrix with same dimension as the state!");
            }
        }

        ComplexMatrix omegaKet = observable.multiply(ket);
        ComplexMatrix bra = omegaKet.adjoint();

        System.out.println(OMEGA + "(" + ket(PSI) + ") = \n");
        System.out.println(omegaKet.toPrettyString());

        System.out.println("\n associated bra = \n");
        System.out.println(bra.toPrettyString());

        double mean = bra.multiply(ket).values[0][0].real;

        System.out.println("\naverage value of " + OMEGA + " on " + ket(PSI) + " = " + mean);

        ComplexMatrix deltaOmaga = observable.subtract(diagonalMatrix(complex(mean, 0.0), observable.rows));

        ComplexMatrix deltaOmegaSq = deltaOmaga.multiply(deltaOmaga);

        double variance = ket.adjoint().multiply(deltaOmegaSq).multiply(ket).values[0][0].real;

        System.out.println("\nvariance = " + variance);
    }
}
