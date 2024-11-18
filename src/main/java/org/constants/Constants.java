package org.constants;

public class Constants {
    public static class PlayerConstants{
        public static final int IDLE = 0;
        public static final int MOVING_LEFT = 1;
        public static final int MOVING_RIGHT = 2;
        public static final int ACCELERATE = 1;
        public static final int DECELERATE = 4;
        public static final int THRUST = 5;
        public static final int FIRE = 6;
        public static final int SHIELD = 7;
        public static final int ROCKET = 8;
        public static final int HIT = 9;

        public static int getSpriteAmount(int player_action) {
            switch (player_action){
                case IDLE:
                    return 0;
                case ACCELERATE:
                    return 1;
                default:
                    return 0;

            }
        }


    }
}
