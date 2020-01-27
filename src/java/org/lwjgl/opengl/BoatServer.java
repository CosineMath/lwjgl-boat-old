package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
public class BoatServer {
	
	public static native void init();
	public static native ByteBuffer eglCreateContext(ByteBuffer shared_context_handle);
	public static native boolean eglMakeCurrent(ByteBuffer context_handle);
	public static native boolean eglSwapBuffers();
	public static native void eglSwapInterval(int value);
	public static native void eglDestroyContext(ByteBuffer context_handle);
	public static native boolean isCurrentContext(ByteBuffer context_handle);
	public static native void releaseCurrentContext();
	
	public static native int getWindowWidth();
	public static native int getWindowHeight();
	static {
		BoatServer.init();
	}
}
