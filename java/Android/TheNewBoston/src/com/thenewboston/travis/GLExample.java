package com.thenewboston.travis;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

public class GLExample extends Activity{

	GLSurfaceView ourSurface;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ourSurface = new GLSurfaceView(this);
		ourSurface.setRenderer((Renderer) new GLRendererEx());
		setContentView(ourSurface);
	}
	
	protected void onPause() {
		super.onPause();
		ourSurface.onPause();
	}
	
	protected void onResume() {
		super.onResume();
		ourSurface.onResume();
	}
	
}
