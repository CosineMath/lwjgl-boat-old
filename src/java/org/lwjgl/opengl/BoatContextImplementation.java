
package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;


final class BoatContextImplementation implements ContextImplementation {

	public ByteBuffer create(PeerInfo peer_info, IntBuffer attribs, ByteBuffer shared_context_handle) throws LWJGLException {
		return BoatServer.eglCreateContext(shared_context_handle);
	}

	public void swapBuffers() throws LWJGLException {
		BoatServer.eglSwapBuffers();
	}

	public void releaseCurrentContext() throws LWJGLException {
	        BoatServer.releaseCurrentContext();
	}

	public void update(ByteBuffer context_handle) {
	        
	}

	public void releaseDrawable(ByteBuffer context_handle) throws LWJGLException {
	
	}
	public void makeCurrent(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {

		BoatServer.eglMakeCurrent(handle);
	}


	public boolean isCurrent(ByteBuffer handle) throws LWJGLException {

		return BoatServer.isCurrentContext(handle);

	}


	public void setSwapInterval(int value) {
		BoatServer.eglSwapInterval(value);
	}

	public void destroy(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
		BoatServer.eglDestroyContext(handle);
	}

}


