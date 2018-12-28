package com.thenewboston.travis;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.SystemClock;
import android.opengl.GLSurfaceView.Renderer;

public class GLRendererEx implements Renderer{

	private GLTriangleEx tri;
	private GLCube cube;
	
	public GLRendererEx() {	
		tri = new GLTriangleEx();
		cube = new GLCube();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(.8f, 0f, .2f, 1f);
		gl.glClearDepthf(1f);
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, -5, 0, 0, 0, 0, 2, 0);
		
		long time = SystemClock.uptimeMillis() % 4000L;
		float angle = .090f * ((int) time);
		
		tri.draw(gl);
		gl.glRotatef(angle, 1, 0, 2);
		cube.draw(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 25);
	}	
	
}
