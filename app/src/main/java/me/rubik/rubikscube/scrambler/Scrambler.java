package me.rubik.rubikscube.scrambler;

import java.util.ArrayList;
import java.util.Random;

public class Scrambler  {

    static final String[] moves = new String[] {"U", "D", "R", "L", "F", "B", "U'", "D'", "R'", "L'", "F'", "B'", "U2", "D2", "R2", "L2", "F2", "B2"};

    public static String genScramble() {
        Random random = new Random();
        ArrayList<String> moveList = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            moveList.add(moves[random.nextInt(18)]);
        }

        return compact(moveList);
    }

    private static String compact(ArrayList<String> moveList) {
        for (int i = 0; i < moveList.size() - 2; i++) {
            String move = moveList.get(i);
            String nextMove = moveList.get(i + 1);

            if (remove(move, nextMove)) {
                moveList.remove(i + 1);
            }

        }

        StringBuilder builder = new StringBuilder();
        for (String move : moveList) {
            builder.append(move).append(" ");
        }
        return builder.toString();
    }

    private static boolean remove(String move, String nextMove) {
        if ((move + "'").equals(nextMove)) {
            return true;
        } else if ((nextMove + "'").equals(move)) {
            return true;
        } else if (move.endsWith("2") && nextMove.endsWith("2")) {
            return move.equals(nextMove);
        }
        return false;

    }


}
