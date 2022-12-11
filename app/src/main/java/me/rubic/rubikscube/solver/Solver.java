package me.rubic.rubikscube.solver;

public class Solver {

    public static String simpleSolve(String scrambledCube) {
        return new Search().solution(scrambledCube, 21, 100000000, 0, 0);
    }

}
