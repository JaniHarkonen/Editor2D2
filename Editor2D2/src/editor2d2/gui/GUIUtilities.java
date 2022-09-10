package editor2d2.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GUIUtilities {
	
    public static final int BOX_X_AXIS = 0;

    public static final int BOX_Y_AXIS = 1;

    public static final int BOX_LINE_AXIS = 2;

    public static final int BOX_PAGE_AXIS = 3;

		// Do not instantiate, utility methods only
	private GUIUtilities() { }
	
	
		// Creates a default JPanel container element with a given orientation
		// with a BoxLayout
	public static JPanel createDefaultPanel(int axis) {
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, axis));
		
		return box;
	}
	
		// Creates a default JPanel container element with a default PAGE_AXIS
		// orientation with a BoxLayout
	public static JPanel createDefaultPanel() {
		return createDefaultPanel(BoxLayout.PAGE_AXIS);
	}
	
		// Creates a JPanel container element with a given title and a given
		// BoxLayout orientation
	public static JPanel createTitledPanel(String title, int axis) {
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, axis));
		box.setBorder(BorderFactory.createTitledBorder(title));
		
		return box;
	}
}
