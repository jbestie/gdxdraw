package org.jbestie.gdxdraw.model.sandboxmap.model.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailRoadCell;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailWayStationCell;
import org.jbestie.gdxdraw.model.sandboxmap.model.station.StationType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 7945899844424383322L;

    private final transient TiledMap tiledMap;
    private final transient TiledMapTileLayer railRoadTileLayer;
    private final transient TiledMapTileLayer railWayStationTileLayer;

    private final RailRoadCell[][] railroadGrid;
    private final RailWayStationCell[][] railWayStationGrid;
    private final Map<String, RailWayStationCell> stationNameToStationMap = new HashMap<String, RailWayStationCell>();

    public GameMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        railroadGrid = new RailRoadCell[mapWidth()][mapHeight()];
        railWayStationGrid = new RailWayStationCell[mapWidth()][mapHeight()];
        railRoadTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("railroads");
        railWayStationTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("stations");
    }

    public void addRailWayStation(String name, StationType stationType, int xCell, int yCell) {
        if (railroadGrid[xCell][yCell] == null) {
            return;
        }

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTile tile = new StaticTiledMapTile(GameContext.engine().getRailRoadStationSlice(stationType));
        cell.setTile(tile);

        RailWayStationCell stationCell = new RailWayStationCell(name, xCell, yCell);
        stationNameToStationMap.put(name, stationCell);
        railWayStationGrid[xCell][yCell] = stationCell;
        railWayStationTileLayer.setCell(xCell, yCell, cell);
    }

    public void addRailRoad(Direction direction, int xCell, int yCell) {
        if (railroadGrid[xCell][yCell] != null) {
            return;
        }

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTile tile = new StaticTiledMapTile(GameContext.engine().getRailRoadSlice(direction));
        cell.setTile(tile);

        railroadGrid[xCell][yCell] = new RailRoadCell(direction);
        railRoadTileLayer.setCell(xCell, yCell, cell);
    }


    public void removeMapCell(int xCell, int yCell) {
        RailRoadCell railRoadCell = railroadGrid[xCell][yCell];
        if (railRoadCell != null) {
            railroadGrid[xCell][yCell] = null;
            railRoadTileLayer.setCell(xCell, yCell, null);
        }

        RailWayStationCell railWayStationCell = railWayStationGrid[xCell][yCell];
        if (railWayStationCell != null) {
            stationNameToStationMap.remove(railWayStationCell.getStationName());
            railWayStationGrid[xCell][yCell] = null;
            railWayStationTileLayer.setCell(xCell, yCell, null);
        }
    }

    public RailWayStationCell getRailwayStation(String name) {
        return stationNameToStationMap.get(name);
    }

    public int mapWidth() {
        return (Integer) tiledMap.getProperties().get("width");
    }

    public int mapHeight() {
        return (Integer) tiledMap.getProperties().get("height");
    }

    public int tileHeight() {
        return (Integer) tiledMap.getProperties().get("tileheight");
    }
    public int tileWidth() {
        return (Integer) tiledMap.getProperties().get("tilewidth");
    }

    public RailRoadCell[][] getRailroadGrid() {
        return railroadGrid;
    }

    public StaticTiledMapTile getRailWayStationTextureRegion(StationType stationType) {
        return null;
    }

    public RailWayStationCell getRailwayStation(int xCell, int yCell) {
        return railWayStationGrid[xCell][yCell];
    }
}
