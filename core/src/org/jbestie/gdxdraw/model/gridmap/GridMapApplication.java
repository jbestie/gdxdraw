package org.jbestie.gdxdraw.model.gridmap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class GridMapApplication extends ApplicationAdapter {

    public static final String VERTEX_SHADER =
//            "#version 120\n" +
//            "varying vec4 v_uv;\n" +
//            " \n" +
//            "void main()\n" +
//            "{\n" +
//            "  gl_Position = gl_Vertex;\n" +
//            "  v_uv = gl_MultiTexCoord0;\n" +
//            "}";
            "attribute vec4 a_position;\n" +
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans; \n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "void main() {\n" +
            " v_color = a_color;\n" +
            " v_color.a = v_color.a * (256.0/255.0);\n" +
            " v_texCoords = a_texCoord0;\n" +
            " gl_Position =  u_projTrans * a_position;\n" +
            "}\n";

    public static final String FRAGMENT_SHADER = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform vec2 u_resolution;\n" +
            "float grid(vec2 st, float res){\n" +
            "    vec2 grid = fract(st*res);\n" +
            "    return 1.-(TILE_SIZE(res,grid.x) * TILE_SIZE(res,grid.y));\n" +
            "}\n" +
            "void main(){\n" +
            "    vec2 st = gl_FragCoord.st/u_resolution.xy;\n" +
            "    st.x *= u_resolution.x/u_resolution.y;\n" +
            "    vec3 color = vec3(0.0);\n" +
            "    // Grid\n" +
            "    vec2 grid_st = st*300.;\n" +
            "    color += vec3(0.5,0.,0.)*grid(grid_st,0.01);\n" +
            "    color += vec3(0.2,0.,0.)*grid(grid_st,0.02);\n" +
            "    color += vec3(0.2)*grid(grid_st,0.1);\n" +
            "    gl_FragColor = vec4( color , 1.0);\n" +
            "}\n";

//        "#version 120\n" +
//        "varying vec4 v_uv;\n" +
//        "uniform vec4 params;\n" +
//        "float grid(vec2 st, float res)\n" +
//        "{\n" +
//        "  vec2 grid = fract(st*res);\n" +
//        "  return (TILE_SIZE(res,grid.x) * TILE_SIZE(res,grid.y));\n" +
//        "}\n" +
//        "void main()\n" +
//        "{\n" +
//        "  vec2 grid_uv = v_uv.xy * params.x; // scale\n" +
//        "  float x = grid(grid_uv, params.y); // resolution\n" +
//        "  gl_FragColor.rgb = 0xFF;  \n" +
//        "  gl_FragColor.a = 1.0;\n" +
//        "}";
//            "#ifdef GL_ES\n" +
//            "precision mediump float; \n" +
//            "#endif\n" +
//            "varying vec4 v_color;\n" +
//            "varying vec2 v_texCoords; \n" +
//            "uniform sampler2D u_texture;\n" +
//            "void main() {\n" +
//            " gl_FragColor = v_color;\n" +
//            "}";

    protected SpriteBatch batch;
    protected Camera camera;
    private Texture texture;
    private Sprite sprite;
    protected ShapeRenderer shapeRenderer;
    private Mesh mapBackground;
    private ShaderProgram shaderProgram;


    @Override
    public void create() {
        camera = new OrthographicCamera(640, 480);
        camera.translate(0, 0, 0);
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));
        sprite = new Sprite(texture);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        shaderProgram = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        ShaderProgram.pedantic = false;

        //u,v
        mapBackground = new Mesh(true, 4, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"), //x,y
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, "a_color") //color 4 positions

                //new VertexAttribute(VertexAttributes.Usage.TextureCoordinates,2,"a_texCoord" + 0)
        );

        mapBackground.setVertices(new float[]{
                -400f,  400f,  1, 1, 0.5f, 1, //0f, 0f, //upper left
                -400f, -400f,  1, 1, 0.5f, 1, //0f, 1f, //lower left
                 400f,  400f,  1, 1, 0.5f, 1, //1f, 0f, //upper right
                 400f, -400f,  1, 1, 0.5f, 1  //1f, 1f, //lower right
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkForInput();

        // getCurrentTrainSprite map
//        texture.bind();
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined);
        shaderProgram.setUniformf("u_resolution", camera.viewportWidth, camera.viewportHeight);
        mapBackground.render(shaderProgram, GL20.GL_TRIANGLE_STRIP, 0, 4);
        shaderProgram.end();
    }

    private void checkForInput() {
        camera.update();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= 3;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += 3;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += 3;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= 3;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
