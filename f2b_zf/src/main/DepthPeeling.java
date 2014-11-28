package main;

import javax.media.opengl.GL2;
import javax.media.opengl.GL3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gbarbieri
 */
public class DepthPeeling {

    private int[] textColor;
    private int[] textDepth;
    private int[] textBlend;
    private int[] fboPeel;
    private int[] fboBlend;

    public DepthPeeling() {

    }

    public void initBuffers(GL3 gl3) {
        /**
         * Create Color Texture.
         */
        textColor = new int[1];
        gl3.glGenTextures(1, textColor, 0);

        gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textColor[0]);
        {
            gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
            gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
            gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_NEAREST);
            gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);

            gl3.glTexImage2D(GL3.GL_TEXTURE_RECTANGLE, 0, GL3.GL_RGBA, Viewer.instance.getGlWindow().getWidth(),
                    Viewer.instance.getGlWindow().getHeight(), 0, GL3.GL_RGBA, GL3.GL_UNSIGNED_BYTE, null);
        }
        gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, 0);
        /**
         * Create depth and blend textures.
         */
        textDepth = new int[2];
        textBlend = new int[2];

        for (int i = 0; i < 2; i++) {

            gl3.glGenTextures(1, textDepth, i);

            gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textDepth[i]);
            {
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_NEAREST);
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);

                gl3.glTexImage2D(GL3.GL_TEXTURE_RECTANGLE, 0, GL3.GL_R32F, Viewer.instance.getGlWindow().getWidth(),
                        Viewer.instance.getGlWindow().getHeight(), 0, GL3.GL_RED, GL3.GL_FLOAT, null);
            }
            gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, 0);
            /**
             * .r = max primitive id .a = number of fragments at Zi.
             */
            gl3.glGenTextures(1, textBlend, i);

            gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textBlend[i]);
            {
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_NEAREST);
                gl3.glTexParameteri(GL3.GL_TEXTURE_RECTANGLE, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);

                gl3.glTexImage2D(GL3.GL_TEXTURE_RECTANGLE, 0, GL3.GL_RGBA32F, Viewer.instance.getGlWindow().getWidth(),
                        Viewer.instance.getGlWindow().getHeight(), 0, GL3.GL_RGBA, GL3.GL_FLOAT, null);
            }
            gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, 0);
        }
        /**
         * Create Depth FrameBuffer.
         */
        fboPeel = new int[1];
        gl3.glGenFramebuffers(1, fboPeel, 0);

        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboPeel[0]);
        {
            gl3.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT0, GL3.GL_TEXTURE_RECTANGLE,
                    textDepth[0], 0);
            gl3.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT1, GL3.GL_TEXTURE_RECTANGLE,
                    textColor[0], 0);
            gl3.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT2, GL3.GL_TEXTURE_RECTANGLE,
                    textDepth[1], 0);
            gl3.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT3, GL3.GL_TEXTURE_RECTANGLE,
                    textColor[0], 0);

            if (gl3.glCheckFramebufferStatus(fboPeel[0]) != GL3.GL_FRAMEBUFFER_COMPLETE) {

                System.out.println("fboPeel incomplete!");
            }
        }
        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
        /**
         * Create Framebuffer.
         */
        fboBlend = new int[1];
        gl3.glGenFramebuffers(1, fboBlend, 0);

        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboBlend[0]);
        {
            gl3.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT0, GL3.GL_TEXTURE_RECTANGLE,
                    textBlend[0], 0);
            gl3.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT1, GL3.GL_TEXTURE_RECTANGLE,
                    textBlend[0], 0);
            
            if (gl3.glCheckFramebufferStatus(fboPeel[0]) != GL3.GL_FRAMEBUFFER_COMPLETE) {

                System.out.println("fboBlend incomplete!");
            }
        }
        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
    }
}
