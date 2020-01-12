/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengl;

/**
 * @author elias_naur
 */

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

final class BoatMouse {
	
	
	private static final int Button1 = 1;
	private static final int Button2 = 2;
	

	private int window_height;
	private int window_width;
	
	private int button_count;

	private final ByteBuffer event_buffer;

	private int last_x;
	private int last_y;
	private int accum_dx;
	private int accum_dy;
	private int accum_dz;
	private byte[] buttons;
	private EventQueue event_queue;
	private long last_event_nanos;

	BoatMouse(int w, int h) throws LWJGLException {
		window_width = w;
		window_height = h;
		event_buffer = ByteBuffer.allocate(Mouse.EVENT_SIZE);
		button_count = 7;
		buttons = new byte[button_count];
		
		reset(false);
	}

	private void reset(boolean grab) {
		event_queue = new EventQueue(event_buffer.capacity());
		accum_dx = accum_dy = 0;
		
		// Pretend that the cursor never moved
		//last_x = 0;
		//last_y = transformY(0);
		setCursorPos(grab, last_x, last_y, last_event_nanos);
	}

	public void read(ByteBuffer buffer) {
		event_queue.copyEvents(buffer);
	}

	public void poll(boolean grab, IntBuffer coord_buffer, ByteBuffer buttons_buffer) {
		if (grab) {
			coord_buffer.put(0, accum_dx);
			coord_buffer.put(1, accum_dy);
		} else {
			coord_buffer.put(0, last_x);
			coord_buffer.put(1, last_y);
		}
		coord_buffer.put(2, accum_dz);
		accum_dx = accum_dy = accum_dz = 0;
		for (int i = 0; i < buttons.length; i++){
			buttons_buffer.put(i, buttons[i]);
		}
	}

	private void putMouseEventWithCoords(byte button, byte state, int coord1, int coord2, int dz, long nanos) {
		event_buffer.clear();
		event_buffer.put(button).put(state).putInt(coord1).putInt(coord2).putInt(dz).putLong(nanos);
		event_buffer.flip();
		event_queue.putEvent(event_buffer);
		last_event_nanos = nanos;
	}

	private void setCursorPos(boolean grab, int x, int y, long nanos) {
		y = transformY(y);
		int dx = x - last_x;
		int dy = y - last_y;
		if (dx != 0 || dy != 0) {
			accum_dx += dx;
			accum_dy += dy;
			last_x = x;
			last_y = y;
			if (grab) {
				putMouseEventWithCoords((byte)-1, (byte)0, dx, dy, 0, nanos);
			} else {
				putMouseEventWithCoords((byte)-1, (byte)0, x, y, 0, nanos);
			}
		}
	}

	
	public void changeGrabbed(boolean grab) {
		
		
	}

	public int getButtonCount() {
		return buttons.length;
	}

	private int transformY(int y) {
		return window_height - 1 - y;
	}
	
	private void handleButton(boolean grab, int button, byte state, long nanos) {
		byte button_num;
		switch (button) {
			case Button1:
				button_num = (byte)0;
				break;
			case Button2:
				button_num = (byte)1;
				break;
			default:
				return;
		}
		buttons[button_num] = state;
		putMouseEvent(grab, button_num, state, 0, nanos);
	}

	private void putMouseEvent(boolean grab, byte button, byte state, int dz, long nanos) {
		if (grab)
			putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
		else
			putMouseEventWithCoords(button, state, last_x, last_y, dz, nanos);
	}


	long oldtime;
	private void handleButtonEvent(boolean grab, long nanos, int type, byte button) {
		switch (type) {
			case BoatInputEvent.ButtonRelease:
				
				handleButton(grab, button, (byte)0, nanos);
				break;
			case BoatInputEvent.ButtonPress:
				oldtime = nanos;
				handleButton(grab, button, (byte)1, nanos);
				
				break;
			default:
				break;
		}
		
	}

	private void handlePointerMotion(boolean grab ,long nanos, int x, int y) {
		setCursorPos(grab, x, y, nanos);
	}

	public boolean filterEvent(boolean grab, BoatInputEvent event) {
		switch (event.getType()) {
			
			case BoatInputEvent.ButtonPress: /* Fall through */
			case BoatInputEvent.ButtonRelease:
				handleButtonEvent(grab, event.getTime(), event.getMouseType(), (byte)event.getMouseButton());
				return true;
			case BoatInputEvent.MotionNotify:
				handlePointerMotion(grab, event.getTime(), event.getMouseX(), event.getMouseY());
				return true;
			default:
				break;
		}
		return false;
	}
}
