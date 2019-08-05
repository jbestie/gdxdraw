package org.jbestie.gdxdraw.model.shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class BezierLineShape extends LineShape {
    public static final float STEP_VALUE = 0.01f;
    public static final int SAMPLES_QTY = 100;
    private final Bezier<Vector3> bezierPath;
    private final Vector3 firstWeightPoint = new Vector3();
    private final Vector3 secondWeightPoint = new Vector3();

    public BezierLineShape(Vector3 start, Vector3 end) {
        this(start, end, Color.BLACK);
    }

    public BezierLineShape(Vector3 start, Vector3 end, Color color) {
        super(start, end, color);
        bezierPath = new Bezier<Vector3>(start, end);
    }


    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for(int i = 0; i < SAMPLES_QTY; ++i){
            float t = i / (float)SAMPLES_QTY;
            Vector3 p1 = new Vector3();
            Vector3 p2 = new Vector3();

            // get the start point of this curve section
            bezierPath.valueAt(p1,t);

            // get the next start point(this point's end)
            bezierPath.valueAt(p2, t - STEP_VALUE);

            // getCurrentTrainSprite the curve
            shapeRenderer.line(p1.x, p1.y, p2.x, p2.y);
        }
        shapeRenderer.end();
        shapeRenderer.setAutoShapeType(false);
    }

    @Override
    public void updateCoordinates(List<Vector3> coordinates) {
        super.updateCoordinates(coordinates);

        int firstPointY = (int) (start.y + (int) ((end.y - start.y) / 4));
        firstWeightPoint.set(start.x, firstPointY, 0);

        int secondPointY = (int) (start.y +  (int) ((end.y - start.y) * 0.75));
        secondWeightPoint.set(start.x, secondPointY, 0);

        bezierPath.set(start, firstWeightPoint, secondWeightPoint, end);
    }
}
