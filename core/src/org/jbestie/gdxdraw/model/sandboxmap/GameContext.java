package org.jbestie.gdxdraw.model.sandboxmap;

import org.jbestie.gdxdraw.model.sandboxmap.engine.GameEngine;

public final class GameContext {
    private static GameEngine gameEngine;

    private GameContext() {}

    public static GameEngine engine() {
        return gameEngine;
    }

    public static void setGameEngine(GameEngine gameEngine) {
        GameContext.gameEngine = gameEngine;
    }

    public static void setupObjects() {
        gameEngine.setup();
    }
}
