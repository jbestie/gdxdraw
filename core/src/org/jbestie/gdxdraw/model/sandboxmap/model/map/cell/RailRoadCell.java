package org.jbestie.gdxdraw.model.sandboxmap.model.map.cell;

import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;

import java.io.Serializable;

public class RailRoadCell implements Serializable {
    private static final long serialVersionUID = -1597998150578377912L;

    private final Direction direction;

    public RailRoadCell(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }


}
