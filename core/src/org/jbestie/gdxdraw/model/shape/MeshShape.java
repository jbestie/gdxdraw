package org.jbestie.gdxdraw.model.shape;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class MeshShape extends AbstractShape {
    private final Matrix4 matrix;
    protected Mesh internalMesh;
    protected float[] vertices;
    protected short[] indices;

    String vertexShader = "attribute vec4 a_position;    \n" +
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 v_color;" +
            "varying vec2 v_texCoords;" +
            "void main()                  \n" +
            "{                            \n" +
            "   v_color = vec4(1, 1, 1, 1); \n" +
            "   v_texCoords = a_texCoord0; \n" +
            "   gl_Position =  u_projTrans * a_position;  \n"      +
            "}                            \n" ;
    String fragmentShader = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "void main()                                  \n" +
            "{                                            \n" +
            "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
            "}";
    
    private final ShaderProgram shader;

    public MeshShape(float[] mainVertice, Matrix4 matrix) {
        vertices = calculateVertices(mainVertice);
        indices = createIndices(vertices);
        this.matrix = matrix;

        shader = new ShaderProgram(vertexShader, fragmentShader);
//        internalMesh = new Mesh(true,
//                vertices.length,
//                indices.length,
//                new VertexAttribute(VertexAttributes.Usage.Position, vertices.length, "a_position"));
//        internalMesh.setVertices(vertices);
//        internalMesh.setIndices(indices);

//        internalMesh = new Mesh(true, 3, 3, VertexAttribute.Position(), VertexAttribute.  ColorUnpacked(), VertexAttribute.TexCoords(0));
//        internalMesh.setVertices(new float[]
//                {-100f, -100f, 0,
//                        100f, -100f, 0,
//                        0, 100f, 0});
//        internalMesh.setIndices(new short[] {0, 1, 2});

        internalMesh = new Mesh(true, vertices.length, indices.length, VertexAttribute.Position());
        internalMesh.setVertices(vertices);
        internalMesh.setIndices(indices);
    }

    private short[] createIndices(float[] vertices) {
        return new short[]{
                0, 1, 2, 3, 4, 5
        };
    }

    private float[] calculateVertices(float[] mainVertice) {
        return new float[] {
                mainVertice[0],         mainVertice[1],       mainVertice[2],
                mainVertice[0],         mainVertice[1] - 100, mainVertice[2], // got to bottom
                mainVertice[0] + 100,   mainVertice[1],       mainVertice[2], // go to top right

                mainVertice[0] + 100,   mainVertice[1],       mainVertice[2], // go to top right
                mainVertice[0] + 100,   mainVertice[1] - 100, mainVertice[2], // go to bottom right
                mainVertice[0],         mainVertice[1] - 100, mainVertice[2]  // got to bottom left
        };
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        internalMesh.render(shader, GL20.GL_TRIANGLES, 0, 3);
        shader.begin();
        shader.setUniformMatrix("u_projTrans", matrix);
        shader.setUniformi("u_texture", 0);
        internalMesh.render(shader, GL20.GL_TRIANGLES);
        shader.end();
    }

    @Override
    public List<Vector3> getCoordinates() {
        return null;
    }

    @Override
    public void updateCoordinates(List<Vector3> coordinates) {
        float[] coords = calculateVertices(new float[] {coordinates.get(0).x, coordinates.get(0).y, coordinates.get(0).z});
        internalMesh.setVertices(coords);
    }
}
