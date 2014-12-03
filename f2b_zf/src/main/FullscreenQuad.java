/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL3;
import jglm.Jglm;
import jglm.Mat4;

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

//        Mat4 modelToClipMatrix = Jglm.orthographic2D(0, 1, 0, 1);

//        dpFinal.bind(gl3);
//        {
//            gl3.glUniformMatrix4fv(dpFinal.getModelToClipMatrixUL(), 1, false, modelToClipMatrix.toFloatArray(), 0);
//        }
//        dpFinal.unbind(gl3);
//
//        dpBlend.bind(gl3);
//        {
//            gl3.glUniformMatrix4fv(dpBlend.getModelToClipMatrixUL(), 1, false, modelToClipMatrix.toFloatArray(), 0);
//        }
//        dpBlend.unbind(gl3);
    }
    private void initVbo(GL3 gl3) {

        float[] vertexAttributes = new float[]{
            -1f, -1f,
            -1f, 1f,
            1f, 1f,
            1f, -1f};

//        quadVBO = new int[1];
//
//        gl3.glGenBuffers(1, IntBuffer.wrap(quadVBO));
//
//        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, quadVBO[0]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertexAttributes);

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertexAttributes.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    private void initVao(GL3 gl3) {

//        quadVAO = new int[1];
//
//        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, quadVBO[0]);
//
//        gl3.glGenVertexArrays(1, IntBuffer.wrap(quadVAO));
//        gl3.glBindVertexArray(quadVAO[0]);
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
