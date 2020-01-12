package org.lwjgl.opengl;

public class BoatServer {
	
	public static native boolean eglMakeCurrent();
	public static native boolean eglSwapBuffers();
	public static native void init();
	public static native int getWindowWidth();
	public static native int getWindowHeight();
	static {
		BoatServer.init();
	}
}
