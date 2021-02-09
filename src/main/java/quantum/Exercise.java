package quantum;

public abstract class Exercise {
    private Reference reference;

    public Reference getReference() {
        if (reference == null) {
            getReferenceFromClassName();
        }

        return reference;
    }

    public abstract String title();

    public abstract String description();

    public abstract void execute();

    private void getReferenceFromClassName() {
        String className = this.getClass().getSimpleName();
        String[] parts = className.split("_");
        if (parts.length < 4) {
            invalidExerciseName();
        }

        int point = Integer.parseUnsignedInt(parts[parts.length - 1]);
        int minor = Integer.parseUnsignedInt(parts[parts.length - 2]);
        int major = Integer.parseUnsignedInt(parts[parts.length - 3]);

        reference = new Reference(major, minor, point);
    }

    private void invalidExerciseName() {
        throw new RuntimeException("Exercise classes must be named {Name}_{major}_{minor}_{point} where {major}, {minor} and {point} are integers <= 0");
    }
}
