
package org.lwjgl.opengl;




import java.awt.Canvas;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.XRandR.Screen;
import org.lwjgl.opengles.EGL;

import org.lwjgl.input.*;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

public final class BoatDisplay implements DisplayImplementation {

	private int window_x;
	private int window_y;
	private int window_width;
	private int window_height;
	
	public boolean grab;
	private int last_x;
	private int last_y;
	private int accum_dx;
	private int accum_dy;
	private int accum_dz;
	private byte[] buttons;
	private long last_event_nanos;
	
	private BoatMouse mouse;
	private BoatKeyboard keyboard;
	
	
	public void createWindow(final DrawableLWJGL drawable, DisplayMode mode, Canvas parent, int x, int y) throws LWJGLException {
	    window_x = 0;
	    window_y = 0;
	    window_width = mode.getWidth();
	    window_height = mode.getHeight();
	    System.out.println("Window width: " + window_width + " Window height: " + window_height);
		BoatInputEventReceiver.receiver.display = this;
	}
	public void destroyWindow() {
		BoatInputEventReceiver.receiver.display = null;
	}

	public void switchDisplayMode(DisplayMode mode) throws LWJGLException {
	}

	public void resetDisplayMode() {
	}

	public int getGammaRampLength() {
		return 0;
	}

	public void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException {
		throw new LWJGLException("No gamma ramp support (Missing XF86VM extension)");
	}

	public String getAdapter() {
		return null;
	}

	public String getVersion() {
		return null;
	}

	public DisplayMode init() throws LWJGLException {
		return new DisplayMode(BoatServer.getWindowWidth(), BoatServer.getWindowHeight(), 24, 60);
	}

	private static DisplayMode getCurrentXRandrMode() throws LWJGLException {
		return null;
	}


	public void setTitle(String title) {
	}

	public boolean isCloseRequested() {
		return false;
	}

	public boolean isVisible() {
		return true;
	}

	public boolean isActive() {
		return true;
	}

	public boolean isDirty() {
		return false;
	}

	public PeerInfo createPeerInfo(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
		return null;
	}


	public void update() {
	}

	public void reshape(int x, int y, int width, int height) {
	}

	public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
		return null;
	}
	
	/* Mouse */
	public boolean hasWheel() {
		return true;
	}

	public int getButtonCount() {
		return mouse.getButtonCount();
	}

	public void createMouse() throws LWJGLException {
		mouse = new BoatMouse(window_width, window_height);
	}

	public void destroyMouse() {
		
	}

	public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons) {
		mouse.poll(grab, coord_buffer, buttons);

	}

	public void readMouse(ByteBuffer buffer) {
		mouse.read(buffer);
	}

	

	public void setCursorPosition(int x, int y) {
		
	}

	public void grabMouse(boolean new_grab) {
		if (grab != new_grab){
			grab = new_grab;
			BoatInputEventReceiver.receiver.changeGrab();
		}
		
		
	}

	private boolean shouldWarpPointer() {
		return false;
	}

	public int getNativeCursorCapabilities() {
		return 0;
	}

	public void setNativeCursor(Object handle) throws LWJGLException {
	}

	public int getMinCursorSize() {
		return 0;
	}

	public int getMaxCursorSize() {
		return 0;
	}

	

	/* Keyboard */
	public void createKeyboard() throws LWJGLException {
		keyboard = new BoatKeyboard();
	}

	public void destroyKeyboard() {
		//keyboard.destroy();
	}

	public void pollKeyboard(ByteBuffer keyDownBuffer) {
		keyboard.poll(keyDownBuffer);
	}

	public void readKeyboard(ByteBuffer buffer) {
		keyboard.read(buffer);
	}
    ////////
	public void handleEvent(byte msg[]){
		//System.out.println("Handling InputEvent:" + msg[0]);
		BoatInputEvent event = new BoatInputEvent(msg, System.nanoTime());
		if (mouse != null && mouse.filterEvent( grab, event)){
			return;
		}
		if (keyboard != null && keyboard.filterEvent(event)){
			return;
		}
	}
	
	////////

	public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
		return null;
	}

	private static long getCursorHandle(Object cursor_handle) {
		return 0;
	}

	public void destroyCursor(Object cursorHandle) {
	}

	public int getPbufferCapabilities() {
		return 0;
	}

	public boolean isBufferLost(PeerInfo handle) {
		return false;
	}

	public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format, ContextAttribs attribs,
								  IntBuffer pixelFormatCaps,
								  IntBuffer pBufferAttribs) throws LWJGLException {
		return new LinuxPbufferPeerInfo(width, height, pixel_format);
	}

	public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
		throw new UnsupportedOperationException();
	}

	public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
		throw new UnsupportedOperationException();
	}

	public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets one or more icons for the Display.
	 * <ul>
	 * <li>On Windows you should supply at least one 16x16 icon and one 32x32.</li>
	 * <li>Linux (and similar platforms) expect one 32x32 icon.</li>
	 * <li>Mac OS X should be supplied one 128x128 icon</li>
	 * </ul>
	 * The implementation will use the supplied ByteBuffers with image data in RGBA and perform any conversions necessary for the specific platform.
	 *
	 * @param icons Array of icons in RGBA mode
	 * @return number of icons used.
	 */
	public int setIcon(ByteBuffer[] icons) {
		return 0;
	}
	public int getX() {
		return window_x;
	}

	public int getY() {
		return window_y;
	}

	public int getWidth() {
		return window_width;
	}

	public int getHeight() {
		return window_height;
	}

	public boolean isInsideWindow() {
		return true;
	}

	public void setResizable(boolean resizable) {
	}

	public boolean wasResized() {
		return false;
	}

	public float getPixelScaleFactor() {
		return 1.0f;
	}

}

