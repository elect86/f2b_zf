/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import javax.media.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Scene {
    
    private int[] objects;
    private float[] vertexData = new float[]{
        0f, 0f, 0f,
        0f, 1f, 0f,
        1f, 0f, 0f
    };
    
    public Scene(GL3 gl3) {
        
        objects = new int[Objects.size.ordinal()];
        
        initVBO(gl3);
        
        initVAO(gl3);
    }
    
    public void draw(GL3 gl3) {
        
        gl3.glBindVertexArray(objects[Objects.vao.ordinal()]);
        {
            gl3.glDrawArrays(GL3.GL_TRIANGLES, 0, vertexData.length / 3);
        }
        gl3.glBindVertexArray(0);
    }
    
    private void initVBO(GL3 gl3) {
        
        gl3.glGenBuffers(1, objects, Objects.vbo.ordinal());
        
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);
        {
            FloatBuffer floatBuffer = GLBuffers.newDirectFloatBuffer(vertexData);
            
            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertexData.length * 4, floatBuffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }
    
    private void initVAO(GL3 gl3) {
        
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);
        {
            gl3.glGenVertexArrays(1, objects, Objects.vao.ordinal());
            
            gl3.glBindVertexArray(objects[Objects.vao.ordinal()]);
            {
                gl3.glEnableVertexAttribArray(0);
                
                gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);
            }
            gl3.glBindVertexArray(0);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }
    
    private enum Objects {
        
        vbo,
        vao,
        size
    }
}
