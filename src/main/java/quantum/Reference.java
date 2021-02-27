package quantum;

import java.util.Objects;

public final class Reference implements Comparable<Reference> {
    public final int main;
    public final int minor;
    public final int point;

    public Reference(int main, int minor, int point) {
        this.main = main;
        this.minor = minor;
        this.point = point;
    }

    public static Reference fromString(String value) {
        String[] parts = value.split("\\.");
        if (parts.length == 3) {
            int major = Integer.parseUnsignedInt(parts[0]);
            int minor = Integer.parseUnsignedInt(parts[1]);
            int point = Integer.parseUnsignedInt(parts[2]);

            return new Reference(major, minor, point);
        }

        return null;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d", main, minor, point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reference reference = (Reference) o;

        return main == reference.main && minor == reference.minor && point == reference.point;
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, minor, point);
    }

    @Override
    public int compareTo(Reference o) {
        if (o.main == main && o.minor == minor && o.point == point) {
            return 0;
        } else if (o.main < main || (o.main == main && o.minor < minor) || (o.main == main && o.minor == minor && o.point < point)) {
            return 1;
        } else {
            return -1;
        }
    }
}
