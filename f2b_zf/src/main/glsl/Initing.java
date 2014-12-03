/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.glsl;

import javax.media.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Initing extends glsl.GLSLProgramObject {

    private int cameraUL;

    public Initing(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {

        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        cameraUL = gl3.glGetUniformLocation(getProgramId(), "camera");
    }

    public int getCameraUL() {
        return cameraUL;
    }
}
