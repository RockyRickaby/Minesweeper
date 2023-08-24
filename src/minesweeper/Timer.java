package minesweeper;

import javax.swing.JButton;

public class Timer {
	private int counter;
	private JButton clock;
	private Thread t; 
	
	public Timer(JButton clock) {
		this.clock = clock;
		this.counter = 0;
		t = new Thread(start(this.clock));
	}
	
	public void reset() {
		t.interrupt();
		counter = 0;
		t.start();
	}
	
	public void start() {
		t.start();
	}
	
	public void stop() {
		t.interrupt();
	}
	
	private Runnable start(JButton clock) {
		return new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch(InterruptedException e) {}
					clock.setText(String.valueOf(counter++));
				}
			}
		};
	}
}
