package org.jbestie.gdxdraw.model.sandboxmap.model.train;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class BaseTrain implements Serializable {
    private static final long serialVersionUID = -630502721332030302L;

    protected Vector3 position;
    protected List<Direction> path;
    protected int currentPosition;
    protected int counter;
    protected TrainType trainType;

    public BaseTrain(Vector3 position, List<Direction> path) {
        this.position = position;
        this.path = path;
        trainType = TrainType.BASE_TRAIN;
    }

    public void moveTrain() {
        float gameSpeed = GameContext.engine().gameSpeed();
        if (gameSpeed != 0) {
            if ((counter / gameSpeed) >= (GameContext.engine().tileWidth() - 1)) {
                currentPosition++;
                counter = 0;
            } else {
                counter++;
            }
        }

        if (currentPosition >= path.size()) {
            currentPosition = 0;
        }

        Direction direction = path.get(currentPosition);

        // get next direction from path
        position.x += direction.x * gameSpeed;
        position.y += direction.y * gameSpeed;
        position.z += direction.z * gameSpeed;
    }

    public Sprite getCurrentTrainSprite() {

        // get related sprite
        Direction direction = path.get(currentPosition);
        Sprite region = GameContext.engine().getTrainTextureForDirection(trainType, direction);
        region.setPosition(position.x, position.y);
        return region;

    }

    public void setPath(List<Direction> directions) {
        path = directions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        BaseTrain train = (BaseTrain) o;
        return (currentPosition == train.currentPosition) &&
                (counter == train.counter) &&
                Objects.equals(position, train.position) &&
                Objects.equals(path, train.path) &&
                (trainType == train.trainType);
    }

    public void setPosition(Vector3 startPoint) {
        position = startPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, path, currentPosition, counter, trainType);
    }

    @Override
    public String toString() {
        return "BaseTrain{" +
                "position=" + position +
                ", path=" + path +
                ", currentPosition=" + currentPosition +
                ", counter=" + counter +
                ", trainType=" + trainType +
                '}';
    }
}
