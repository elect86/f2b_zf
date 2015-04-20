/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;

/**
 *
 * @author gbarbieri
 */
public class FullscreenQuad {

    private int[] objects;

    public FullscreenQuad(GL3 gl3) {

        objects = new int[Objects.size.ordinal()];

        initFullScreenQuad(gl3);
    }

    private void initFullScreenQuad(GL3 gl3) {

        initVbo(gl3);

        initVao(gl3);
    }

    private void initVbo(GL3 gl3) {

        float[] vertexAttributes = new float[]{
            -1f, -1f,
            -1f, 1f,
            1f, 1f,
            1f, -1f};

        gl3.glGenBuffers(1, objects, Objects.vbo.ordinal());

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertexAttributes);

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertexAttributes.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    private void initVao(GL3 gl3) {

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);

        gl3.glGenVertexArrays(1, objects, Objects.vao.ordinal());
        gl3.glBindVertexArray(objects[Objects.vao.ordinal()]);
        {
            gl3.glEnableVertexAttribArray(0);
            {
                gl3.glVertexAttribPointer(0, 2, GL3.GL_FLOAT, false, 0, 0);
            }
        }
        gl3.glBindVertexArray(0);
    }

//    public static void main(String[] args){
//        
//        Mat4 modelToClipMatrix = Jglm.orthographic(-1, 1, -1, 1, 1, -1);
//        
//        modelToClipMatrix.print("modelToClipMatrix");
//    }
    private enum Objects {

        vbo,
        vao,
        size
    }
}
