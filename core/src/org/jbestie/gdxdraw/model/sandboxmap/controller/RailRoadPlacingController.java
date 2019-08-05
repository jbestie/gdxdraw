package org.jbestie.gdxdraw.model.sandboxmap.controller;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;
import org.jbestie.gdxdraw.model.sandboxmap.model.station.StationType;

public class RailRoadPlacingController {
    private final OrthogonalTiledMapRenderer mapRenderer;

    public RailRoadPlacingController(OrthogonalTiledMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    public void placeObjectToCell(int screenX, int screenY) {
        // how to calculate cell position?
        Rectangle rectangle = mapRenderer.getViewBounds();
        screenX += rectangle.x;
        screenY = (int) ((GameContext.engine().screenHeight() + rectangle.y) - screenY);

        int tileWidth = GameContext.engine().tileWidth();
        if (screenXOutOfBounds(screenX, tileWidth)){
            return;
        }

        int tileHeight = GameContext.engine().tileHeight();
        if (screenYOutOfBounds(screenY, tileHeight)) {
            return;
        }

        int xCell = screenX / tileWidth;
        int yCell = screenY / tileHeight;

        switch (GameContext.engine().selectedObjectToPlace()) {
            case PLACE_RAILWAY:
                placeRailWay(xCell, yCell);
                break;
            case PLACE_STATION:
                placeRailWayStation(xCell, yCell);
                break;
            case CLEAR_CELL:
                clearCell(xCell, yCell);
                break;
            case ADD_STATION:
                addStation(xCell, yCell);
            default:
                break;
        }
    }

    private void addStation(int xCell, int yCell) {
        GameContext.engine().addStationToRoute(xCell, yCell);
    }

    private boolean screenYOutOfBounds(int screenY, int tileHeight) {
        return (screenY > (GameContext.engine().mapHeight() * tileHeight)) || (screenY < 0);
    }

    private boolean screenXOutOfBounds(int screenX, int tileWidth) {
        return (screenX > (GameContext.engine().mapWidth() * tileWidth))  || (screenX < 0);
    }

    private void clearCell(int xCell, int yCell) {
        GameContext.engine().removeMapCell(xCell, yCell);
    }

    private void placeRailWayStation(int xCell, int yCell) {
        GameContext.engine().addRailWayStation("TestStation" + xCell + yCell, StationType.SMALL, xCell, yCell);
    }

    private void placeRailWay(int xCell, int yCell) {
        GameContext.engine().addRailRoadToMap(Direction.EAST, xCell, yCell);
    }
}
