package org.jbestie.gdxdraw;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import org.jbestie.gdxdraw.model.shape.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GdxDraw extends ApplicationAdapter {
    protected Logger logger = new Logger(getClass().getName(), Logger.INFO);
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected AbstractShape previousPoint;
    protected AbstractShape currentLine;
    protected AbstractShape currentMesh;
    protected List<AbstractShape> shapesToRender = new ArrayList<AbstractShape>();
    protected Camera camera;


    @Override
    public void create() {
        camera = new OrthographicCamera(640, 480);
        camera.translate(0, 0, 0);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(new MouseInputProcessor());

        // setup ShapeRenderer
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // mass drawing of shapes
        for (AbstractShape shape : shapesToRender) {
            shape.draw(shapeRenderer);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    /**
     * Manages the mouse's input
     */
    private class MouseInputProcessor extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == Input.Buttons.LEFT) {
//                drawLineOnLeftMouseButtonClick(screenX, screenY);
                drawMeshOnLeftMouseButtonClick(screenX, screenY);
            } else if (button == Input.Buttons.RIGHT){ // stop the drawing of the path
                logger.info("Right button clicked");
                if (currentLine != null) {
                    shapesToRender.remove(currentLine);
                }
                previousPoint = null;
                currentLine = null;
                currentMesh = null;
            }

            return super.touchDown(screenX, screenY, pointer, button);
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
//            updateLineCoordinatesOnMouseMove(screenX, screenY);
            updateMeshCoordinatesOnMouseMove(screenX, screenY);
            return super.mouseMoved(screenX, screenY);
        }
    }

    private void updateMeshCoordinatesOnMouseMove(int screenX, int screenY) {
        if (currentMesh != null) { // handles mouse moving and updates line's coordinates
            currentMesh.updateCoordinates(Collections.singletonList(camera.unproject(new Vector3(screenX, screenY, 0))));
        }
    }

    private void drawMeshOnLeftMouseButtonClick(int screenX, int screenY) {
        if (currentMesh == null) {
            Vector3 vector3 = camera.unproject(new Vector3(screenX, screenY, 0));
            float[] coords = new float[]{vector3.x, vector3.y, vector3.z};
            currentMesh = new MeshShape(coords, camera.combined);
            shapesToRender.add(currentMesh);
        } else {
            currentMesh = null;
        }
    }


    private void updateLineCoordinatesOnMouseMove(int screenX, int screenY) {
        if (previousPoint != null) { // handles mouse moving and updates line's coordinates
            previousPoint.updateCoordinates(Collections.singletonList(camera.unproject(new Vector3(screenX, screenY, 0))));
            currentLine.updateCoordinates(Arrays.asList(currentLine.getCoordinates().get(0), previousPoint.getCoordinates().get(0)));
        }
    }

    private void drawLineOnLeftMouseButtonClick(int screenX, int screenY) {
        logger.info("Left button clicked");
        // if this left click is first one then create 2 points and create line between them
        if (previousPoint == null) {
            // create first point
            PointShape firstPoint = new PointShape(camera.unproject(new Vector3(screenX, screenY, 0)));
            shapesToRender.add(firstPoint);

            // create second point
            PointShape secondPoint = new PointShape(camera.unproject(new Vector3(screenX, screenY, 0)));

            // create line from newly created points
            currentLine = new BezierLineShape(firstPoint.getCoordinates().get(0), secondPoint.getCoordinates().get(0));
            shapesToRender.add(currentLine);

            // keep newly created second point as previous one
            previousPoint = secondPoint;
        } else { // this next left click so we create line from previous point and newly created one
            //
            PointShape secondPoint = new PointShape(camera.unproject(new Vector3(screenX, screenY, 0)));
            currentLine = new BezierLineShape(previousPoint.getCoordinates().get(0), secondPoint.getCoordinates().get(0));
            shapesToRender.add(currentLine);

            // keep newly created point as previous one
            previousPoint = secondPoint;
        }
    }
}
