package org.jbestie.gdxdraw.model.sandboxmap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.jbestie.gdxdraw.model.sandboxmap.controller.InputController;
import org.jbestie.gdxdraw.model.sandboxmap.controller.RailRoadPlacingController;
import org.jbestie.gdxdraw.model.sandboxmap.engine.GameEngine;
import org.jbestie.gdxdraw.model.sandboxmap.model.stage.MainScene;
import org.jbestie.gdxdraw.model.sandboxmap.model.train.BaseTrain;
import org.jbestie.gdxdraw.model.sandboxmap.path.AStarPathFinder;
import org.jbestie.gdxdraw.model.sandboxmap.path.RailRoadPathFinder;
import org.jbestie.gdxdraw.model.sandboxmap.renderer.TextureMapObjectRenderer;

import java.util.List;


public class SandboxGdxGame extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;

    private InputController inputController;
    private OrthogonalTiledMapRenderer mapRenderer;
    private SpriteBatch batch;
    private List<BaseTrain> trains;
    private final RailRoadPathFinder railRoadPathFinder = new RailRoadPathFinder();
    private MainScene mainScene;

    @Override
    public void create () {
        String mapFile = "rail_map.tmx";
        AssetManager assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());

        assetManager.load(mapFile, TiledMap.class);
        assetManager.finishLoading();
        TiledMap map = assetManager.get(mapFile);

        camera = new OrthographicCamera();
        camera.update();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);


        mapRenderer = new TextureMapObjectRenderer(map, 1.0f );
        viewport = new ExtendViewport((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight(), camera);
        viewport.apply();

        GameEngine gameEngine = new GameEngine(map, railRoadPathFinder, new AStarPathFinder());
        GameContext.setGameEngine(gameEngine);
        GameContext.setupObjects();
        inputController = new InputController(camera, new RailRoadPlacingController(mapRenderer), gameEngine);

        mainScene = new MainScene(new ScreenViewport());

        // multiple input controllers handling
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(mainScene);
        multiplexer.addProcessor(inputController);
        Gdx.input.setInputProcessor(multiplexer);

        batch = new SpriteBatch();
        trains = GameContext.engine().getTrains();


    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // handle camera move
        inputController.pollInputEvents();

        // Render map
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render();

        for (BaseTrain train : trains) {
            train.moveTrain();
        }

        batch.begin();
        for (BaseTrain train : trains) {
            train.getCurrentTrainSprite().draw(batch);
        }
        batch.end();

        // TODO jbestie : why it kills the train?
        mainScene.act();
        mainScene.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainScene.getViewport().update(width, height);
        viewport.update(width,height);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }
}
