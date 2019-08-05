package org.jbestie.gdxdraw.model.shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Collections;
import java.util.List;

public class PointShape extends AbstractShape {
    private Vector3 point2D;
    private final Color color;

    public PointShape(Vector3 point2D) {
        this(point2D, Color.BLACK);
    }

    public PointShape(Vector3 point2D, Color color) {
        this.point2D = point2D;
        this.color = color;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
                shapeRenderer.setColor(color);
                shapeRenderer.point(point2D.x, point2D.y, 0.0f);
                shapeRenderer.end();
    }

    @Override
    public List<Vector3> getCoordinates() {
        return Collections.singletonList(point2D);
    }

    @Override
    public void updateCoordinates(List<Vector3> coordinates) {
        point2D = coordinates.get(0);
    }
}
