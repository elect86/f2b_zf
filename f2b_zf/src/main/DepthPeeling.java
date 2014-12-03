package main;

import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import main.glsl.Blending;
import main.glsl.Initing;
import main.glsl.Peeling;

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
    private int currID;
    private int prevID;
    private int[] queryId;
    private int passes;
    private Initing initing;
    private Blending blending;
    private Peeling peeling;
    private Scene scene;

    public DepthPeeling(GL3 gl3) {

        String path = "/main/glsl/shaders/";

        initing = new Initing(gl3, path, "standard_VS.glsl", "initDepth_FS.glsl");

        blending = new Blending(gl3, path, "standard_VS.glsl", "blend_FS.glsl");

        peeling = new Peeling(gl3, path, "standard_VS.glsl", "peel_FS.glsl");

        scene = new Scene(gl3);
    }

    public void draw(GL3 gl3) {

        gl3.glDisable(GL3.GL_DEPTH_TEST);
        gl3.glDepthMask(false);
        /**
         * Rendering.
         */
        clearBuffers(gl3);

        passes = 0;

        while (true) {

            currID = passes % 2;
            prevID = 1 - currID;

            if (!peelDepth(gl3)) {

                passes++;

                blendingDepth(gl3);

            } else {

                break;
            }
        }
//        drawFinal(gl3);
    }

    private boolean peelDepth(GL3 gl3) {

        boolean s;

        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboPeel[0]);
        {
            gl3.glBlendEquation(GL3.GL_MAX);

            gl3.glBeginQuery(GL3.GL_ANY_SAMPLES_PASSED, queryId[0]);
            {
                if (passes == 0) {

                    gl3.glEnablei(GL3.GL_BLEND, 0);
                    {
                        gl3.glDrawBuffer(GL3.GL_COLOR_ATTACHMENT0);

                        gl3.glColorMaski(0, true, false, false, false);
                        {
                            gl3.glClearColor(-1f, 0f, 0f, 0f);
                            gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

                            initing.bind(gl3);
                            {
                                scene.draw(gl3);
                            }
                            initing.unbind(gl3);
                        }
                        gl3.glColorMaski(0, true, true, true, true);
                    }
                    gl3.glDisablei(GL3.GL_BLEND, 0);

                } else {

                    gl3.glEnablei(GL3.GL_BLEND, 0);
                    gl3.glEnablei(GL3.GL_BLEND, 1);
                    gl3.glEnablei(GL3.GL_BLEND, 2);
                    gl3.glEnablei(GL3.GL_BLEND, 3);
                    {
                        gl3.glDrawBuffer(GL3.GL_COLOR_ATTACHMENT0 + 2 * currID);
                        gl3.glClearColor(-1f, 0f, 0f, 0f);
                        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

                        gl3.glDrawBuffer(GL3.GL_COLOR_ATTACHMENT1 + 2 * currID);
                        gl3.glClearColor(0f, 0f, 0f, 0f);
                        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

                        int[] buffers = new int[]{GL3.GL_COLOR_ATTACHMENT0, GL3.GL_COLOR_ATTACHMENT1,
                            GL3.GL_COLOR_ATTACHMENT2, GL3.GL_COLOR_ATTACHMENT3};
                        gl3.glDrawBuffers(2, buffers, 2 * currID);

                        peeling.bind(gl3);
                        {
                            gl3.glActiveTexture(GL3.GL_TEXTURE0);
                            gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textDepth[prevID]);

                            gl3.glActiveTexture(GL3.GL_TEXTURE1);
                            gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textBlend[prevID]);

                            scene.draw(gl3);
                        }
                        peeling.unbind(gl3);
                    }
                    gl3.glDisablei(GL3.GL_BLEND, 0);
                    gl3.glDisablei(GL3.GL_BLEND, 1);
                    gl3.glDisablei(GL3.GL_BLEND, 2);
                    gl3.glDisablei(GL3.GL_BLEND, 3);
                }
            }
            gl3.glEndQuery(GL3.GL_ANY_SAMPLES_PASSED);
            int[] samplesPassed = new int[1];
            gl3.glGetQueryObjectuiv(queryId[0], GL3.GL_QUERY_RESULT, samplesPassed, 0);

            s = samplesPassed[0] == 0;
        }
        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);

        return s;
    }

    private void blendingDepth(GL3 gl3) {

        gl3.glEnablei(GL3.GL_BLEND, 0);
        gl3.glEnablei(GL3.GL_BLEND, 1);
        {
            gl3.glBlendFunc(GL3.GL_ONE, GL3.GL_ONE);
            gl3.glBlendEquationSeparate(GL3.GL_MAX, GL3.GL_FUNC_ADD);

            gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboBlend[0]);
            {
                gl3.glDrawBuffer(GL3.GL_COLOR_ATTACHMENT0 + currID);

                gl3.glColorMaski(currID, true, false, false, false);
                {
                    gl3.glClearColor(0f, 0f, 0f, 0f);
                    gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

                    blending.bind(gl3);
                    {
                        gl3.glActiveTexture(GL3.GL_TEXTURE0);
                        gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textDepth[currID]);

                        gl3.glActiveTexture(GL3.GL_TEXTURE1);
                        gl3.glBindTexture(GL3.GL_TEXTURE_RECTANGLE, textBlend[prevID]);

                        scene.draw(gl3);
                    }
                    blending.unbind(gl3);
                }
                gl3.glColorMaski(currID, true, true, true, true);
            }
            gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
        }
        gl3.glDisablei(GL3.GL_BLEND, 0);
        gl3.glDisablei(GL3.GL_BLEND, 1);
    }

    private void drawFinal(GL3 gl3) {

        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glDepthMask(true);
        {
            gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
            gl3.glDrawBuffer(GL3.GL_BACK);

        }
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
                    textBlend[1], 0);

            if (gl3.glCheckFramebufferStatus(fboPeel[0]) != GL3.GL_FRAMEBUFFER_COMPLETE) {

                System.out.println("fboBlend incomplete!");
            }
        }
        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);

        queryId = new int[1];
        gl3.glGenQueries(1, queryId, 0);
    }

    private void delete(GL3 gl3) {

        if (textDepth != null) {

            for (int i = 0; i < 2; i++) {

                gl3.glDeleteTextures(1, textDepth, i);
            }
        }
        if (textBlend != null) {

            for (int i = 0; i < 2; i++) {

                gl3.glDeleteTextures(1, textBlend, i);
            }
        }
        if (fboPeel != null) {

            gl3.glDeleteFramebuffers(1, fboPeel, 0);
        }
        if (textColor != null) {

            gl3.glDeleteTextures(1, textColor, 0);
        }
        if (fboBlend != null) {

            gl3.glDeleteFramebuffers(1, fboBlend, 0);
        }
        if (queryId != null) {

            gl3.glDeleteQueries(1, queryId, 0);
        }
    }

    private void clearBuffers(GL3 gl3) {

        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboBlend[0]);
        {
            gl3.glDrawBuffer(GL3.GL_COLOR_ATTACHMENT1);

            gl3.glColorMaski(1, true, false, false, true);
            {
                gl3.glClearColor(0f, 0f, 0f, 0f);

                gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);
            }
            gl3.glColorMaski(1, true, true, true, true);
        }
        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
    }

    public enum textUnits {

        depth,
        blend
    }
}
