package org.jbestie.gdxdraw.model.sandboxmap.path;

import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailRoadCell;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailWayStationCell;

import java.util.ArrayList;
import java.util.List;

import static org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction.EAST;
import static org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction.WEST;

public class RailRoadPathFinder implements IPathFinder{

    @Override
    public List<Direction> findPathBetweenTwoStations(String stationAName, String stationBName) {
        List<Direction> listOfDirections = getOneWayDirection(stationAName, stationBName);
        listOfDirections.addAll(getOneWayDirection(stationBName, stationAName));
        return listOfDirections;
    }

    private List<Direction> getOneWayDirection(String stationAName, String stationBName) {
        RailRoadCell[][] railRoadCells = GameContext.engine().getRailroadGrid();

        RailWayStationCell railWayStationACell = GameContext.engine().getRailwayStation(stationAName);
        int stationAX = railWayStationACell.getxCell();
        int stationAY = railWayStationACell.getyCell();
        RailRoadCell stationACell = railRoadCells[stationAX][stationAY];

        RailWayStationCell railWayStationBCell = GameContext.engine().getRailwayStation(stationBName);
        int stationBX = railWayStationBCell.getxCell();
        int stationBY = railWayStationBCell.getyCell();
        RailRoadCell stationBCell = railRoadCells[stationBX][stationBY];

        RailRoadCell currentCell = stationACell;
        RailRoadCell endCell = stationBCell;

        List<Direction> listOfDirections = new ArrayList<Direction>();
        int xDelta = stationBX - stationAX;
        int xDirection = (xDelta != 0) ? (xDelta / Math.abs(xDelta)) : 0;
        int yDelta = stationBY - stationAY;
        int yDirection = ((yDelta) != 0) ? (yDelta / Math.abs(yDelta)) : 0;

        int currentXCell = stationAX;
        int currentYCell = stationAY;

        int mapWidth = railRoadCells.length;
        int mapHeight = railRoadCells[0].length;

        while ((currentCell != endCell) && (currentXCell < mapWidth) && (currentYCell < mapHeight) && (currentXCell >= 0) && (currentYCell >= 0)) {
            RailRoadCell nextCell = railRoadCells[currentXCell + xDirection][currentYCell];
            if (nextCell != null) {
                currentXCell += xDirection;
                currentCell = nextCell;
                listOfDirections.add((xDirection > 0) ? EAST : WEST);
                continue;
            }
        }

        return listOfDirections;
    }
}
