package org.jbestie.gdxdraw.model.sandboxmap.model.map.cell;

public class RailWayStationCell {
    private String stationName;
    private int xCell;
    private int yCell;

    public RailWayStationCell(String stationName, int xCell, int yCell) {
        this.stationName = stationName;
        this.xCell = xCell;
        this.yCell = yCell;
    }

    public String getStationName() {
        return stationName;
    }

    public int getxCell() {
        return xCell;
    }

    public int getyCell() {
        return yCell;
    }
}
