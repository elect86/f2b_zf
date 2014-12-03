/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.glsl;

import javax.media.opengl.GL3;
import main.DepthPeeling;

/**
 *
 * @author gbarbieri
 */
public class Peeling extends glsl.GLSLProgramObject {

    private int cameraUL;

    public Peeling(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {

        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        cameraUL = gl3.glGetUniformLocation(getProgramId(), "camera");
        
        int tex_depthUL = gl3.glGetUniformLocation(getProgramId(), "tex_depth");
        bind(gl3);
        {
            gl3.glUniform1i(tex_depthUL, DepthPeeling.textUnits.depth.ordinal());
        }
        unbind(gl3);
        
        int tex_count_idUL = gl3.glGetUniformLocation(getProgramId(), "tex_count_id");
        bind(gl3);
        {
            gl3.glUniform1i(tex_count_idUL, DepthPeeling.textUnits.blend.ordinal());
        }
        unbind(gl3);
    }

    public int getCameraUL() {
        return cameraUL;
    }
}
