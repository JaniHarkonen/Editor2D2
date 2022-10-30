package editor2d2.gui;

import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUIUtilities {
	
    public static final int BOX_X_AXIS = 0;

    public static final int BOX_Y_AXIS = 1;

    public static final int BOX_LINE_AXIS = 2;

    public static final int BOX_PAGE_AXIS = 3;
    
    
    public static final int MB_LEFT = 1;
    
    public static final int MB_MIDDLE = 2;
    
    public static final int MB_RIGHT = 3;
    

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
	
		// Returns a copy of a string with the first character converted to
		// uppercase
	public static String getFirstLetterUppercase(String string) {
		return (""+string.charAt(0)).toUpperCase() + string.substring(1);
	}
	
		// Converts the first character of all the strings in a given array to
		// uppercase
	public static void convertFirstLetterUppercase(String[] strings) {
		for( int i = 0; i < strings.length; i++ )
		strings[i] = getFirstLetterUppercase(strings[i]);
	}
	
		// Returns whether a given MouseEvent was fired upon left-click
	public static boolean checkLeftClick(MouseEvent e) {
		return (e.getButton() == 1);
	}
	
		// Returns whether a given MouseEvent was fired upon middle-click
	public static boolean checkMiddleClick(MouseEvent e) {
		return (e.getButton() == 2);
	}
	
		// Returns whether a given MouseEvent was fired upon right-click
	public static boolean checkRightClick(MouseEvent e) {
		return (e.getButton() == 3);
	}
	
		// Checks if a given array of booleans representing input checks
		// is valid and displays an error message if not
	public static int showErrorIfInvalid(String title, int issues) {
		if( issues > 0 )
		{
			String message = "Error: " + issues + " invalid input" + (issues == 1 ? "" : "s") + " found!";
			int option = JOptionPane.DEFAULT_OPTION;
			int type = JOptionPane.ERROR_MESSAGE;
			
			JOptionPane.showConfirmDialog(null, message, title, option, type);
		}
		
		return issues;
	}
	
		// Takes an array of boolean values representing the input checks
		// and returns the number of checks failed
	public static int checkMultiple(boolean... checks) {
		int issues = 0;
		
		for( boolean check : checks )
		if( !check )
		issues++;
		
		return issues;
	}
}
