package org.jbestie.gdxdraw.model.sandboxmap.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector3;
import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.manager.TextureManager;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.GameMap;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailRoadCell;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailWayStationCell;
import org.jbestie.gdxdraw.model.sandboxmap.model.station.StationType;
import org.jbestie.gdxdraw.model.sandboxmap.model.train.BaseTrain;
import org.jbestie.gdxdraw.model.sandboxmap.model.train.TrainType;
import org.jbestie.gdxdraw.model.sandboxmap.path.AStarPathFinder;
import org.jbestie.gdxdraw.model.sandboxmap.path.RailRoadPathFinder;

import java.util.*;

import static org.jbestie.gdxdraw.model.sandboxmap.engine.GameMode.*;

public class GameEngine {
    private final TiledMap tiledMap;
    private final TextureManager textureManager;
    private final GameMap gameMap;
    private final RailRoadPathFinder pathFinder;
    private final AStarPathFinder aStarPathFinder;
    private int speed = 1;
    private GameMode gameMode = DEFAULT;
    private CellAction cellAction = CellAction.PLACE_RAILWAY;
    private final Map<BaseTrain, Set<String>> tranBindingMap = new HashMap<BaseTrain, Set<String>>();
    private final List<BaseTrain> trains = new ArrayList<BaseTrain>();
    private BaseTrain currentTrain;
    private String currentStation;

    public GameEngine(TiledMap tiledMap, RailRoadPathFinder pathFinder, AStarPathFinder aStarPathFinder) {
        this.tiledMap = tiledMap;
        textureManager = new TextureManager();
        this.pathFinder = pathFinder;
        this.aStarPathFinder  = aStarPathFinder;
        gameMap = new GameMap(tiledMap);
    }

    public TiledMapTileLayer getMapTileLayer(String layerName) {
        return (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
    }

    public TiledMapTileSet getMapTileSet(String tileSetName) {
        return tiledMap.getTileSets().getTileSet(tileSetName);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void zeroGameSpeed() {
        speed = 0;
    }

    public void ordinalGameSpeed() {
        speed = 1;
    }

    public void doubleGameSpeed() {
        speed = 2;
    }

    public void tripleGameSpeed() {
        speed = 3;
    }

    public float gameSpeed() {
        return speed;
    }

    public Sprite getTrainTextureForDirection(TrainType trainType, Direction trainDirection) {
        return textureManager.getTrainTextureRegion(trainType, trainDirection);
    }

    public int mapHeight() {
        return gameMap.mapHeight();
    }

    public int mapWidth() {
        return gameMap.mapWidth();
    }

    public int tileHeight() {
        return gameMap.tileHeight();
    }

    public int tileWidth() {
        return gameMap.tileWidth();
    }


    public void setup() {
        textureManager.setup();
    }

    public TextureRegion getRailRoadSlice(Direction direction) {
        return textureManager.getRailRoadTextureRegion(direction);
    }

    public void addRailRoadToMap(Direction direction, int xCell, int yCell) {
        gameMap.addRailRoad(direction, xCell, yCell);
    }

    public void addRailWayStation(String name, StationType stationType, int xCell, int yCell) {
        gameMap.addRailWayStation(name, stationType, xCell, yCell);
    }

    public void removeMapCell(int xCell, int yCell) {
        gameMap.removeMapCell(xCell, yCell);
    }

    public RailWayStationCell getRailwayStation(String name) {
        return gameMap.getRailwayStation(name);
    }

    public RailRoadCell[][] getRailroadGrid() {
        return gameMap.getRailroadGrid();
    }

    public TextureRegion getRailRoadStationSlice(StationType stationType) {
        return textureManager.getRailRoadStationTextureRegion(stationType);
    }

    public void startEdit() {
        gameMode = EDIT;
        speed = 0;
    }

    public void pauseGame() {
        gameMode = PAUSE;
        speed = 0;
    }

    public void resumeGame() {
        gameMode = DEFAULT;
        speed = 1;
        resetEditState();
    }

    private void resetEditState() {
        currentTrain = null;
        cellAction = CellAction.PLACE_RAILWAY;
    }

    public void selectRailWayToPlace() {
        if (isEditMode()) {
            cellAction = CellAction.PLACE_RAILWAY;
        }
    }

    public void selectStationToPlace() {
        if (isEditMode()) {
            cellAction = CellAction.PLACE_STATION;
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public boolean isEditMode() {
        return EDIT == gameMode;
    }

    public int screenHeight() {
        return Gdx.graphics.getHeight();
    }

    public boolean isGamePaused() {
        return PAUSE == gameMode;
    }

    public CellAction selectedObjectToPlace() {
        return cellAction;
    }

    public void selectRemoveCell() {
        if (isEditMode()) {
            cellAction = CellAction.CLEAR_CELL;
        }
    }

    public void addStationToRoute(int xCell, int yCell) {
        if (isEditMode()) {
            RailWayStationCell cell = gameMap.getRailwayStation(xCell, yCell);
            if (cell == null) {
                return;
            }

            if (currentTrain == null) {
                currentTrain = new BaseTrain(new Vector3(), new ArrayList<Direction>());

                Set<String> stations = new HashSet<String>();
                currentStation = cell.getStationName();
                stations.add(currentStation);
                tranBindingMap.put(currentTrain, stations);
            } else {
                Set<String> stations = tranBindingMap.get(currentTrain);
                String secondStation = cell.getStationName();
                stations.add(secondStation);
                if (stations.size() == 2) {
                    String stationA = currentStation;
                    String stationB = secondStation;

//                    List<Direction> directions = pathFinder.findPathBetweenTwoStations(stationA, stationB);
                    List<Direction> directions = aStarPathFinder.findPathBetweenTwoStations(stationA, stationB);

//                    compareTwoResults(directions, aStarDirection);

                    currentTrain.setPath(directions);

                    RailWayStationCell startingCell = gameMap.getRailwayStation(currentStation);

                    Vector3 startPoint = new Vector3(startingCell.getxCell() * GameContext.engine().tileWidth(), startingCell.getyCell() * GameContext.engine().tileHeight(), 0);
                    currentTrain.setPosition(startPoint);

                    trains.add(currentTrain);
                    tranBindingMap.put(currentTrain, stations);
                    currentTrain = null;
                    currentStation = null;
                }
            }
        }
    }

    private void compareTwoResults(List<Direction> directions, List<Direction> aStarDirection) {
        if (directions.size() != aStarDirection.size()) {
            System.out.println("directions has = " + directions.size() + " but aStarDirection has " + aStarDirection.size());
        }

        for (int i = 0; i < directions.size(); i++) {
            if (!directions.get(i).equals(aStarDirection.get(i))) {
                System.out.println("Have differences between direction = " + directions.get(i) + " and aStarDirection = " + aStarDirection.get(i));
            }
        }
    }

    public List<BaseTrain> getTrains() {
        return trains;
    }

    public void selectAddStationToRoute() {
        cellAction = CellAction.ADD_STATION;
    }
}
