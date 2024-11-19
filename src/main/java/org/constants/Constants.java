package org.constants;

public class Constants {
    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int MOVING_LEFT = 3;
        public static final int MOVING_RIGHT = 2;
        public static final int ACCELERATE = 1;
        public static final int DECELERATE = 4;
        public static final int THRUST = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_0 = 7;


        public static int getSpriteAmount(int player_action) {
            switch (player_action) {
                case IDLE, ACCELERATE:
                    return 4;
                default:
                    return 0;
            }
        }

        public static int getAction(int player_action) {
            switch (player_action) {
                case ATTACK_1:
                    return 7;
                case ATTACK_0:
                    return 0;
                default:
                    return 0;
            }
        }


    }
}
