package utils;

import java.util.Set;

public class MinMax {
    public final double min;
    public final double max;

    private MinMax(Set<Double> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one value");
        }

        double _min = Double.MAX_VALUE;
        double _max = Double.MIN_VALUE;

        for (double value : values) {
            _min = Math.min(value, _min);
            _max = Math.max(value, _max);
        }

        min = _min;
        max = _max;
    }

    private MinMax(double[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("There must be at least one value");
        }

        double _min = Double.MAX_VALUE;
        double _max = Double.MIN_VALUE;

        for (double value : values) {
            _min = Math.min(value, _min);
            _max = Math.max(value, _max);
        }

        min = _min;
        max = _max;
    }

    public static MinMax minMaxOf(Set<Double> values) {
        return new MinMax(values);
    }

    public static MinMax minMaxOf(double[] values) {
        return new MinMax(values);
    }
}
