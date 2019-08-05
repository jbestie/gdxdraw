package org.jbestie.gdxdraw.model.shape;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public abstract class AbstractShape {
    public abstract void draw(ShapeRenderer shapeRenderer);
    public abstract List<Vector3> getCoordinates();
    public abstract void updateCoordinates(List<Vector3> coordinates);
}
