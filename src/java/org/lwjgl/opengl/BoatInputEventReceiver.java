package org.lwjgl.opengl;

import java.net.Socket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BoatInputEventReceiver {
	
	private Socket sock;
	private InputStream is;
	private OutputStream os;
	public static BoatInputEventReceiver receiver;
	public BoatDisplay display;
	public void init(int port) throws Exception {
		display = null;
		sock = new Socket("127.0.0.1", port);
		is = sock.getInputStream();
		os = sock.getOutputStream();
		Thread r = new Thread(new Receiver(), "BoatInputEventReceiver");
		r.setDaemon(true);
		r.start();
	}
	public void changeGrab(){
		try {
			os.write(display.grab ? (byte)1 : (byte)0);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		
		
	}
	private class Sender implements Runnable{
		public void run(){
		}
	}
	
	private class Receiver implements Runnable{
		public void run() {
			try {
				byte[] msg = new byte[BoatInputEvent.EVENT_SIZE];
				while (true) {
					is.read(msg, 0, BoatInputEvent.EVENT_SIZE);
					if (display != null){
						display.handleEvent(msg);
					}
					

				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	static {
		try{
			receiver = new BoatInputEventReceiver();
			receiver.init(Integer.parseInt(System.getenv("BOAT_INPUT_PORT")));

		}
		catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("BoatInputEventReceiver is created!The port is :" + Integer.parseInt(System.getenv("BOAT_INPUT_PORT")));
		
	}
	
}
