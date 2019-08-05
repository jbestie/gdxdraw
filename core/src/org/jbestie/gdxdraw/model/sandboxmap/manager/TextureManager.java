package org.jbestie.gdxdraw.model.sandboxmap.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import org.jbestie.gdxdraw.model.sandboxmap.GameContext;
import org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction;
import org.jbestie.gdxdraw.model.sandboxmap.model.station.StationType;
import org.jbestie.gdxdraw.model.sandboxmap.model.train.TrainType;

import java.util.HashMap;
import java.util.Map;

import static org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction.*;
import static org.jbestie.gdxdraw.model.sandboxmap.model.direction.Direction.NORTH_WEST;

public class TextureManager {

    private final Map<TrainType, Map<Direction, Sprite>> trainTextureMap = new HashMap<TrainType, Map<Direction, Sprite>>();
    private final Map<Direction, Sprite> railRoadTextureMap = new HashMap<Direction, Sprite>();
    private final Map<StationType, Sprite> railRoadStationTextureMap = new HashMap<StationType, Sprite>();

    public TextureManager() {
    }

    public void setup() {
        setupBaseTrainTextureMap("train_direction.png");
        setupRailRoadTextureMap("railroad_tiles.png");
        setupRailRoadStationsTextureMap("station.png");
    }

    private void setupRailRoadStationsTextureMap(String pathToTexture) {
        Texture texture = new Texture(Gdx.files.internal(pathToTexture));
        int tileHeight = texture.getHeight();
        int tileWidth = texture.getWidth();
        railRoadStationTextureMap.put(StationType.SMALL,   new Sprite(new TextureRegion(texture, 0, 0, tileWidth, tileHeight)));
        railRoadStationTextureMap.put(StationType.MEDIUM,  new Sprite(new TextureRegion(texture, 0, 0, tileWidth, tileHeight)));
        railRoadStationTextureMap.put(StationType.BIG,     new Sprite(new TextureRegion(texture, 0, 0, tileWidth, tileHeight)));
    }

    private void setupRailRoadTextureMap(String pathToTexture) {
        railRoadTextureMap.putAll(parseDirectionTextureMap(pathToTexture));
    }

    protected void setupBaseTrainTextureMap(String pathToTexture) {
        trainTextureMap.put(TrainType.BASE_TRAIN, parseDirectionTextureMap(pathToTexture));
    }

    public Sprite getTrainTextureRegion(TrainType trainType, Direction trainDirection) {
        return trainTextureMap.get(trainType).get(trainDirection);
    }

    public Sprite getRailRoadTextureRegion(Direction direction) {
        return railRoadTextureMap.get(direction);
    }

    private Map<Direction, Sprite> parseDirectionTextureMap(String pathToTexture) {
        Map<Direction, Sprite> result = new HashMap<Direction, Sprite>();

        Texture texture = new Texture(Gdx.files.internal(pathToTexture));
        int tileWidth = GameContext.engine().tileWidth();
        int tileHeight = GameContext.engine().tileHeight();

        result.put(NORTH,      new Sprite(new TextureRegion(texture, tileWidth * 0, tileHeight * 0, tileWidth, tileHeight)));
        result.put(NORTH_EAST, new Sprite(new TextureRegion(texture, tileWidth * 1, tileHeight * 0, tileWidth, tileHeight)));
        result.put(EAST,       new Sprite(new TextureRegion(texture, tileWidth * 2, tileHeight * 0, tileWidth, tileHeight)));
        result.put(SOUTH_EAST, new Sprite(new TextureRegion(texture, tileWidth * 0, tileHeight * 1, tileWidth, tileHeight)));
        result.put(SOUTH,      new Sprite(new TextureRegion(texture, tileWidth * 1, tileHeight * 1, tileWidth, tileHeight)));
        result.put(SOUTH_WEST, new Sprite(new TextureRegion(texture, tileWidth * 2, tileHeight * 1, tileWidth, tileHeight)));
        result.put(WEST ,      new Sprite(new TextureRegion(texture, tileWidth * 0, tileHeight * 2, tileWidth, tileHeight)));
        result.put(NORTH_WEST, new Sprite(new TextureRegion(texture, tileWidth * 1, tileHeight * 2, tileWidth, tileHeight)));

        return result;
    }

    public TextureRegion getRailRoadStationTextureRegion(StationType stationType) {
        return railRoadStationTextureMap.get(stationType);
    }
}
