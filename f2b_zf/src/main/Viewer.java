package main;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gbarbieri
 */
public class Viewer implements GLEventListener {

    public static Viewer instance;
    
    public static void main(String[] args) {

        final Viewer viewer = new Viewer();

        instance = viewer;
        
        final Frame frame = new Frame("f2b_zf");

        frame.add(viewer.getNewtCanvasAWT());

        frame.setSize(viewer.getGlWindow().getWidth(), viewer.getGlWindow().getHeight());

        frame.setLocation(100, 100);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                viewer.getGlWindow().destroy();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    private GLWindow glWindow;
    private NewtCanvasAWT newtCanvasAWT;
    private int imageWidth;
    private int imageHeight;
    private int[] vbo;
    private int[] ibo;
    private DepthPeeling depthPeeling;

    public Viewer() {

        imageWidth = 1024;
        imageHeight = 768;

        initGL();
    }

    private void initGL() {
        GLProfile gLProfile = GLProfile.getDefault();

        GLCapabilities gLCapabilities = new GLCapabilities(gLProfile);

        glWindow = GLWindow.create(gLCapabilities);

        newtCanvasAWT = new NewtCanvasAWT(glWindow);

        glWindow.setSize(imageWidth, imageHeight);

        glWindow.addGLEventListener(this);

        Animator animator = new Animator(glWindow);
        animator.setRunAsFastAsPossible(true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable glad) {
        
        GL3 gl3 = glad.getGL().getGL3();
        
        depthPeeling = new DepthPeeling(gl3);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
    
    }

    @Override
    public void display(GLAutoDrawable glad) {
    
//        depthPeeling = 
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    
    }

    public GLWindow getGlWindow() {
        return glWindow;
    }

    public NewtCanvasAWT getNewtCanvasAWT() {
        return newtCanvasAWT;
    }
}
