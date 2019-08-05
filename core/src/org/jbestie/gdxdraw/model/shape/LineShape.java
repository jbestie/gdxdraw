package org.jbestie.gdxdraw.model.shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.List;

public class LineShape extends AbstractShape {
    protected Vector3 start;
    protected Vector3 end;
    protected Color color;

    public LineShape(Vector3 start, Vector3 end) {
        this(start, end, Color.BLACK);
    }

    public LineShape(Vector3 start, Vector3 end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.line(start.x, start.y, end.x, end.y);
        shapeRenderer.end();
    }

    @Override
    public List<Vector3> getCoordinates() {
        return Arrays.asList(start, end);
    }

    @Override
    public void updateCoordinates(List<Vector3> coordinates) {
        start = coordinates.get(0);
        end = coordinates.get(1);
    }
}

