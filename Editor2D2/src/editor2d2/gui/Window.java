package editor2d2.gui;

import javax.swing.JFrame;

public class Window {

		// This class is a singleton, only instantiate if not already
	public static boolean isInstantiated = false;
	
		// Default window width
	public static final int DEFAULT_WINDOW_WIDTH = 640;
	
		// Default window height
	public static final int DEFAULT_WINDOW_HEIGHT = 480;
	
		// Default window title
	public static final String DEFAULT_TITLE = "Editor2D v.2.0.0";
	
		// JFrame representing the window
	private JFrame window;
	
	
	private Window(int width, int height, String title) {
		this.window = new JFrame();
		this.window.setSize(width, height);
		this.window.setTitle(title);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.window.setJMenuBar((new WindowToolbar()).get());
		this.window.add((new Root()).render());
		
		this.window.setVisible(true);
	}
	
	
		// Returns a reference to the JFrame of the window
	public JFrame getFrame() {
		return this.window;
	}
	
		// Instantiates the window, if it's not been already
		// with default settings
	public static Window instantiate() {
		return instantiate(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, DEFAULT_TITLE);
	}
	
		// Instantiates the window with given settings
	public static Window instantiate(int width, int height, String title) {
		if( isInstantiated )
		return null;
		
		isInstantiated = true;
		
		return new Window(width, height, title);
	}
}
