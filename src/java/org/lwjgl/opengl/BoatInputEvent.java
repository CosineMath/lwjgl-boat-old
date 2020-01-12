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

import java.nio.ByteBuffer;

/**
 * Wrapper class for X11 events.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2286 $
 * $Id: LinuxPeerInfo.java 2286 2006-03-23 19:32:21Z matzon $
 */
final class BoatInputEvent {
	public static final int KeyPress        = 2;
	public static final int KeyRelease      = 3;
	public static final int ButtonPress     = 4;
	public static final int ButtonRelease	= 5;
	public static final int MotionNotify	= 6;
	
	public static final int EVENT_SIZE  = 10;
	
	
	private long time;
	private byte type;
	private byte mouseButton;
	private int x;
	private int y;
	
	private int keyCode;
	private int keyChar;
	BoatInputEvent(byte msg[], long t) {
		time = t;
		type = msg[0];
		mouseButton = msg[1];
		x = BoatInputEvent.readInt(msg, 2);
		y = BoatInputEvent.readInt(msg, 6);
		
		
		keyCode = BoatInputEvent.readInt(msg, 2);
		keyChar = BoatInputEvent.readInt(msg, 6);
		
	}
	
	public long getTime(){
		return time;
	}
	public int getType(){
		return type;
	}
	
	/* Mouse methods */
	
	public int getMouseX(){
		return x;
	}
	public int getMouseY(){
		return y;
	}
	public int getMouseType(){
		return getType();
	}
	public int getMouseButton(){
		return mouseButton;
	}
	

	/* Key methods */
	public int getKeyType(){
		return getType();
	}
	public int getKeyCode(){
		return keyCode;
	}
	public int getKeyChar(){
		return keyChar;
	}
	

	public static int readInt(byte[] src, int offset) {
		return 
		    ((src[0 + offset] & 0x000000ff ) << (0 * 8)) | 
			((src[1 + offset] & 0x000000ff ) << (1 * 8)) | 
			((src[2 + offset] & 0x000000ff ) << (2 * 8)) | 
			((src[3 + offset] & 0x000000ff ) << (3 * 8)) ;
	}  
	
}
