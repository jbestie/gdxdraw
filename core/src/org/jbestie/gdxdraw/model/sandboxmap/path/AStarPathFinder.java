package org.jbestie.gdxdraw.model.sandboxmap.path;

import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailRoadCell;
import org.jbestie.gdxdraw.model.sandboxmap.model.map.cell.RailWayStationCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStarPathFinder implements IPathFinder {

    @Override
    public List<Direction> findPathBetweenTwoStations(String stationAName, String stationBName) {
        List<Direction> path = getOneWayPath(stationAName, stationBName);
        path.addAll(getOneWayPath(stationBName, stationAName));
        return path;
    }

    private List<Direction> getOneWayPath(String stationAName, String stationBName) {
        RailRoadCell[][] railRoadCells = GameContext.engine().getRailroadGrid();

        RailWayStationCell railWayStationACell = GameContext.engine().getRailwayStation(stationAName);
        RailWayStationCell railWayStationBCell = GameContext.engine().getRailwayStation(stationBName);

        char[][] matrix = generateGameMatrix(railRoadCells, railWayStationACell, railWayStationBCell);

        List<Node> result = getPath(matrix, railWayStationACell);

        List<Direction> directions = new ArrayList<Direction>();
        for (int i = 1; i < result.size(); i++) {
            Node previousNode = result.get(i - 1);
            Node currentNode = result.get(i);
            int xDelta = currentNode.x - previousNode.x;
            int yDelta = currentNode.y - previousNode.y;
            Direction direction = Direction.valueOf(new Direction(xDelta, yDelta, 0));
            directions.add(direction);
        }

        return directions;
    }

    private char[][] generateGameMatrix(RailRoadCell[][] railRoadCells, RailWayStationCell stationA, RailWayStationCell stationB) {
        char[][] result = new char[railRoadCells.length][railRoadCells[0].length];
        for (int i = 0; i < railRoadCells.length; i++) {
            for (int j = 0; j < railRoadCells[0].length; j++) {
                RailRoadCell cell = railRoadCells[i][j];
                if (cell != null) {
                    result[i][j] = '1';
                } else {
                    result[i][j] = '0';
                }
            }
        }

        result[stationA.getxCell()][stationA.getyCell()] = '0';
        result[stationB.getxCell()][stationB.getyCell()] = 'X';

        return result;
    }

    public List<Node> getPath(char[][] matrix, RailWayStationCell startCell) {

        List<Node> result = new ArrayList<Node>();
        List<Node> queue = new ArrayList<Node>();
        queue.add(new Node(startCell.getxCell(), startCell.getyCell()));

        while (!queue.isEmpty()) {
            Node current = queue.remove(0);
            if (matrix[current.x][current.y] == 'X') {
                Node startNode = current;
                while (startNode != null) {
                    result.add(startNode);
                    startNode = startNode.getCameFrom();
                }

                break;
            }

            matrix[current.x][current.y] = '0'; // mark as visited

            List<Node> neighbors = getNeighbors(matrix, current);
            queue.addAll(neighbors);
        }

        Collections.reverse(result);

        return result;
    }

    public List<Node> getNeighbors(char[][] matrix, Node node) {
        List<Node> neighbors = new ArrayList<Node>();

        if (isValidPoint(matrix, node.x - 1, node.y)) {
            neighbors.add(new Node(node.x - 1, node.y, node));
        }

        if (isValidPoint(matrix, node.x + 1, node.y)) {
            neighbors.add(new Node(node.x + 1, node.y, node));
        }

        if (isValidPoint(matrix, node.x, node.y - 1)) {
            neighbors.add(new Node(node.x, node.y - 1, node));
        }

        if (isValidPoint(matrix, node.x, node.y + 1)) {
            neighbors.add(new Node(node.x, node.y + 1, node));
        }

        return neighbors;
    }

    public boolean isValidPoint(char[][] matrix, int x, int y) {
        return !((x < 0) || (x >= matrix.length) || (y < 0) || (y >= matrix.length)) && (matrix[x][y] != '0');
    }


    static class Node {
        int x;
        int y;

        private Node cameFrom;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Node(int x, int y, Node cameFrom) {
            this.x = x;
            this.y = y;
            this.cameFrom = cameFrom;
        }

        public Node getCameFrom() {
            return cameFrom;
        }
    }
}