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


        // Player Health Constants
        public static final int FULL_HEALTH = 0;
        public static final int HIT1 = 1;
        public static final int HIT2 = 2;
        public static final int HIT3 = 3;
        public static final int HIT4 = 4;
        public static final int DEAD = 5;

        public static int getHealthSprite(int hit) {
            return switch (hit) {
                case 1 -> HIT1;
                case 2 -> HIT2;
                case 3 -> HIT3;
                case 4 -> HIT4;
                case 5 -> DEAD;
                default -> FULL_HEALTH;
            };
        }


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
