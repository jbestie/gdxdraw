package org.jbestie.gdxdraw.model.sandboxmap.model.direction;

import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.List;

public class Direction extends Vector3 {
    public static Direction ZERO       = new Direction(0, 0, 0);
    public static Direction NORTH      = new Direction(0, 1, 0);
    public static Direction NORTH_EAST = new Direction(1, 1, 0);
    public static Direction EAST       = new Direction(1, 0, 0);
    public static Direction SOUTH_EAST = new Direction(1, -1, 0);
    public static Direction SOUTH      = new Direction(0, -1, 0);
    public static Direction SOUTH_WEST = new Direction(-1, -1, 0);
    public static Direction WEST       = new Direction(-1, 0, 0);
    public static Direction NORTH_WEST = new Direction(-1, 1, 0);

    private final static List<Direction> DIRECTIONS = Arrays.asList(
            ZERO,
            NORTH,
            NORTH_EAST,
            EAST,
            SOUTH_EAST,
            SOUTH,
            SOUTH_WEST,
            WEST,
            NORTH_WEST
    );

    public Direction(int x, int y, int z) {
        super(x, y, z);
    }

    public static Direction valueOf(Direction vector3) {
        for (Direction direction : DIRECTIONS) {
            if (direction.equals(vector3)) {
                return direction;
            }
        }

        throw new IllegalArgumentException("Cannot find the direction for Vector3=" + vector3);
    }
}
