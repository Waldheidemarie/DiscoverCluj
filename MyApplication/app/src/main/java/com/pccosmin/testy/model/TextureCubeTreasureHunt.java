package com.pccosmin.testy.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.pccosmin.testy.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by LenovoM on 11/12/2016.
 */


public class TextureCubeTreasureHunt {

    /**
     * The buffer holding the vertices
     */
    private FloatBuffer vertexBuffer;
    /**
     * The buffer holding the texture coordinates
     */
    private FloatBuffer textureBuffer;
    /**
     * The buffer holding the indices
     */
    private ByteBuffer indexBuffer;

    /**
     * Our texture pointer
     */
    private int[] textures = new int[6];

    /**
     * Textures
     */
    private int[] resourceIds = new int[]{
            R.raw.logo,
            R.raw.logo,
            R.raw.logo,
            R.raw.logo,
            R.raw.logo,
            R.raw.indiciu};

    /**
     * The initial vertex definition
     * <p>
     * Note that each face is defined, even
     * if indices are available, because
     * of the texturing we want to achieve
     */
    private float vertices[] = {
            //Vertices according to faces
            -1.0f, -1.0f, 1.0f, //Vertex 0
            1.0f, -1.0f, 1.0f,  //v1
            -1.0f, 1.0f, 1.0f,  //v2
            1.0f, 1.0f, 1.0f,   //v3

            1.0f, -1.0f, 1.0f,  //...
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,

            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,

            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
    };

    /**
     * The initial texture coordinates (u, v)
     */
    private float texture[] = {
            //Mapping coordinates for the vertices

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

    };

    /**
     * The initial indices definition
     */
    private byte indices[] = {
            //Faces definition
            0, 1, 3, 0, 3, 2,           //Face front
            4, 5, 7, 4, 7, 6,           //Face right
            8, 9, 11, 8, 11, 10,        //...
            12, 13, 15, 12, 15, 14,
            16, 17, 19, 16, 19, 18,
            20, 21, 23, 20, 23, 22,
    };

    /**
     * The Cube constructor.
     * <p>
     * Initiate the buffers.
     */
    public TextureCubeTreasureHunt() {
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //
        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);

        //
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl - The GL Context
     */
    public void draw(GL10 gl) {

        //Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //Set the face rotation
        gl.glFrontFace(GL10.GL_CCW);

        //Enable the vertex and texture state
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        for (int i = 0; i < 6; i++) {
            //Bind our only previously generated texture in this case
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
            indexBuffer.position(6 * i);
            //Draw the vertices as triangles, based on the Index Buffer information
            gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, indexBuffer);
        }

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    /**
     * Load the textures
     *
     * @param gl      - The GL Context
     * @param context - The Activity context
     */
    public void loadTexture(GL10 gl, Context context) {

        //Generate a 6 texture pointer...
        gl.glGenTextures(6, textures, 0);

        // Bitmap bitmap = null;

        for (int i = 0; i < 6; i++) {
            // Create a bitmap
            // bitmap = BitmapFactory.decodeResource(context.getResources(), resourceIds[i]);

            //...and bind it to our array
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);

            //Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            InputStream istream = context.getResources().openRawResource(resourceIds[i]);
            Bitmap bitmap;

            try {
                // Read and decode input as bitmap
                bitmap = BitmapFactory.decodeStream(istream);
            } finally {
                try {
                    istream.close();
                } catch (IOException e) {
                }
            }


            //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            //Clean up
            bitmap = null;
        }
    }
}
