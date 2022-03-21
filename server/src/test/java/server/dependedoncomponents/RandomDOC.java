package server.dependedoncomponents;

import java.util.Random;

public class RandomDOC extends Random {

    private int result;

    public RandomDOC(int result) {
        this.result = result;
    }

    @Override
    public int nextInt(int bound) {
        return result;
    }
}
