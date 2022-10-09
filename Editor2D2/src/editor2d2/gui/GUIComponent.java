package editor2d2.gui;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class GUIComponent {

		// The GUI element that represents the component
	protected JPanel element;
	
	
	public GUIComponent() {
		this.element = new JPanel();
		this.element.setLayout(new BoxLayout(this.element, BoxLayout.PAGE_AXIS));
	}
	
	public GUIComponent(LayoutManager layoutManager) {
		this.element = new JPanel();
		this.element.setLayout(layoutManager);
	}
	
	
		// Creates a given number of empty JLabels that can be used to
		// offset elements
	public static void addEmptySpace(JPanel jpanel, int n) {
		for( int i = 0; i < n; i++ )
		jpanel.add(new JLabel(" "));
	}
	
		// Creates an empty space using an empty JLabel
	public static void addEmptySpace(JPanel jpanel) {
		addEmptySpace(jpanel, 1);
	}
	
	
		// Repaints the element and returns the resulting JPanel
	public final JPanel render() {
		element.removeAll();
		element.add(draw());
		element.revalidate();
		element.repaint();
		
		return element;
	}
	
		// Re-renders the component (same as render but doesn't return
		// the resulting JPanel)
	public final void update() {
		render();
	}
	
		// Draws the element and returns the resulting JPanel
		// (TO BE OVERRIDDEN)
	protected abstract JPanel draw();
}
