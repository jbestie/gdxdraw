package org.jbestie.gdxdraw.model.sandboxmap.path;

import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;

import java.util.List;

public interface IPathFinder {
    List<Direction> findPathBetweenTwoStations(String stationAName, String stationBName);
}
