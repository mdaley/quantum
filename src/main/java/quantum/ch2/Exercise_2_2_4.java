package quantum.ch2;

import quantum.Exercise;

public class Exercise_2_2_4 extends Exercise {
    @Override
    public String title() {
        return "Exercise 2 2 4";
    }

    @Override
    public String description() {
        return "This does something interesting";
    }

    @Override
    public boolean repeats() {
        return false;
    }

    @Override
    public void execute() {
        System.out.println("I do something interesting");
    }
}
