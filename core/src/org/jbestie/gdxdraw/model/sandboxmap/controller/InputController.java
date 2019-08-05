package org.jbestie.gdxdraw.model.sandboxmap.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.engine.GameEngine;

import static com.badlogic.gdx.Input.Keys.*;

public class InputController extends InputAdapter {
    private final OrthographicCamera camera;
    private final RailRoadPlacingController railRoadPlacingController;
    private final GameEngine engine;

    public InputController(OrthographicCamera camera, RailRoadPlacingController railRoadPlacingController, GameEngine engine) {
        this.camera = camera;
        this.railRoadPlacingController = railRoadPlacingController;
        this.engine = engine;
    }

    public void pollInputEvents() {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                camera.update();
                camera.position.x -= 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                camera.update();
                camera.position.x += 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.update();
                camera.position.y += 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.update();
                camera.position.y -= 1;
            }
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case P:
                if (!engine.isGamePaused()) {
                    engine.pauseGame();
                } else {
                    engine.resumeGame();
                }
                return true;
            case R:
                if (engine.isEditMode()) {
                    engine.selectRailWayToPlace();
                } else {
                    engine.resumeGame();
                }
                return true;
            case E:
                if (!engine.isEditMode()) {
                    engine.startEdit();
                } else {
                    engine.resumeGame();
                }
                return true;
            case S:
                if (engine.isEditMode()) {
                    engine.selectStationToPlace();
                }
                return true;
            case X:
                if (engine.isEditMode()) {
                    engine.selectRemoveCell();
                }
                return true;
            case A:
                if (engine.isEditMode()) {
                    engine.selectAddStationToRoute();
                }
                break;
            default:
                break;
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            if (GameContext.engine().isEditMode()) {
                railRoadPlacingController.placeObjectToCell(screenX, screenY);
            }
            return true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }
}
