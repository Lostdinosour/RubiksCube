package me.rubik.rubikscube.scrambler;

import java.util.ArrayList;
import java.util.Random;

public class Scrambler  {

    public static String genScramble() {
        ArrayList<Move> moveList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            moveList.add(Move.getRandom());
        }
        return compact(moveList);
    }

    private static String compact(ArrayList<Move> moveList) {
        StringBuilder scrambleString = new StringBuilder();
        for (int i = 0; i < moveList.size() - 2; i++) {
            Move currentMove = moveList.get(i);
            Move nextMove = moveList.get(i + 1);

            if (currentMove.type().equals(nextMove.type())) {
                int value = currentMove.value() + nextMove.value();
                if (Math.abs(value) == 2) {
                    scrambleString.append(Move.fromTypeAndValue(currentMove.type(), Math.abs(value)).name().replace("a", "'")).append(" ");
                    i++;
                } else if (Math.abs(value) == 3) {
                    scrambleString.append(currentMove.name().replace("a", "'")).append(" ");
                } else if (value != 0 && value != 4) {
                    scrambleString.append(currentMove.name().replace("a", "'")).append(" ");
                } else {
                    scrambleString.append(currentMove.name().replace("a", "'")).append(" ");
                    i++;
                }
            } else {
                scrambleString.append(currentMove.name().replace("a", "'")).append(" ");
            }
        }

        return scrambleString.toString();
    }

    private enum Move {
        U("U", 1),
        D("D", 1),
        R("R", 1),
        L("L", 1),
        F("F", 1),
        B("B", 1),
        Ua("U", -1),
        Da("D", -1),
        Ra("R", -1),
        La("L", -1),
        Fa("F", -1),
        Ba("B", -1),
        U2("U", 2),
        D2("D", 2),
        R2("R", 2),
        L2("L", 2),
        F2("F", 2),
        B2("B", 2);

        private final String type;
        private final int value;

        Move(String type, int value) {
            this.type = type;
            this.value = value;
        }

        String type() { return type; }
        int value() { return value; }

        public static Move getRandom() {
            return Move.values()[new Random().nextInt(18)];
        }

        public static Move fromTypeAndValue(String type, int value) {
            for (Move move : Move.values()) {
                if (move.type.equals(type) && move.value == value) {
                    return move;
                }
            }

            return null;
        }

    }


}
