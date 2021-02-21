package quantum.complex;

/**
 * Global environment for complex number calculations.
 */
public class ComplexEnvironment {

    private static double floor = 1e-10;

    private ComplexEnvironment() {
        // not to be instantiated.
    }

    /**
     * Set a global value for the `floor` below which very small +ve and -ve doubles are set to zero. This will help
     * where tests rely of values being zero but the inaccuracy of calculations results in numbers that are not quite
     * zero.
     * @param noiseFloor the noise floor to set.
     */
    public static void setFloor(double noiseFloor) {
        floor = noiseFloor;
    }

    static double floor(double value) {
        return (value < ComplexEnvironment.floor && value > -ComplexEnvironment.floor) ? 0.0 : value;
    }

}
