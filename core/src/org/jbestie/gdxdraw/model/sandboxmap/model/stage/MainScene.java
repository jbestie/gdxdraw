package org.jbestie.gdxdraw.model.sandboxmap.model.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScene extends Stage {

    public MainScene(Viewport viewport) {
        super(viewport);
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
//        table.setFillParent(true);
        table.setSize(128, 128);
        table.setPosition(Gdx.graphics.getWidth() - table.getWidth(), 0);
        addActor(table);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button = new TextButton("Click me!", skin);
        table.add(button);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Clicked! Is checked: " + button.isChecked());
                button.setText("Good job!");
            }
        });

        // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
        table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
    }
}
