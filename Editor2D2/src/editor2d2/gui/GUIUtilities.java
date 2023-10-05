package editor2d2.gui;

import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * This is a utility class that contains commonly used 
 * methods for dealing with GUI-components extending 
 * GUIComponent.
 * 
 * This is a final class and is not to be extended 
 * as it only contains - and should only contain - 
 * static methods.
 * 
 * @author User
 *
 */
public final class GUIUtilities {
	
	/**
	 * Shortcut for BoxLayout.X_AXIS.
	 */
    public static final int BOX_X_AXIS = 0;

    /**
     * Shortcut for BoxLayout.Y_AXIS.
     */
    public static final int BOX_Y_AXIS = 1;

    /**
     * Shortcut for BoxLayout.LINE_AXIS.
     */
    public static final int BOX_LINE_AXIS = 2;

    /**
     * Shortcut for BoxLayout.PAGE_AXIS.
     */
    public static final int BOX_PAGE_AXIS = 3;
    
    /**
     * Shortcut for left mouse button.
     */
    public static final int MB_LEFT = 1;
    
    /**
     * Shortcut for middle mouse button.
     */
    public static final int MB_MIDDLE = 2;
    
    /**
     * Shortcut for right mouse button.
     */
    public static final int MB_RIGHT = 3;
    

		// Do not instantiate, utility methods only
	private GUIUtilities() { }
	
	
	/**
	 * Creates a default JPanel container element with 
	 * a BoxLayout and given orientation.
	 * 
	 * @param axis Orientation of the BoxLayout used by 
	 * the JPanel.
	 * 
	 * @return Returns a default JPanel with BoxLayout.
	 */
	public static JPanel createDefaultPanel(int axis) {
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, axis));
		
		return box;
	}
	
	/**
	 * Creates a default JPanel container element with 
	 * BoxLayout and BoxLayout.PAGE_AXIS orientation.
	 * 
	 * @return Returns a default JPanel with PAGE_AXIS 
	 * BoxLayout.
	 */
	public static JPanel createDefaultPanel() {
		return createDefaultPanel(BoxLayout.PAGE_AXIS);
	}
	
	/**
	 * Creates a JPanel container element with a given 
	 * titled border and BoxLayout orientation. The 
	 * JPanel will use BoxLayout.
	 * 
	 * @param title Title of the border that the JPanel 
	 * should have.
	 * @param axis Orientation of the BoxLayout used by 
	 * the JPanel.
	 * 
	 * @return Returns a default JPanel with BoxLayout 
	 * and a titled border.
	 */
	public static JPanel createTitledPanel(String title, int axis) {
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, axis));
		box.setBorder(BorderFactory.createTitledBorder(title));
		
		return box;
	}
	
	/**
	 * Returns a copy of a string with the first 
	 * character converted into uppercase.
	 * 
	 * @param string String whose first letter should 
	 * be converted.
	 * 
	 * @return Returns a copy of the string with the 
	 * first letter in uppercase.
	 */
	public static String getFirstLetterUppercase(String string) {
		return (""+string.charAt(0)).toUpperCase() + string.substring(1);
	}
	
	/**
	 * Converts the first letter of each string in an 
	 * array into uppercase. The method operates on a 
	 * given array and does not return anything.
	 * 
	 * @param strings Array of strings whose first 
	 * letters should be converted to uppercase.
	 */
	public static void convertFirstLetterUppercase(String[] strings) {
		for( int i = 0; i < strings.length; i++ )
		strings[i] = getFirstLetterUppercase(strings[i]);
	}
	
	/**
	 * Returns whether a given MouseEvent was a left-
	 * click.
	 * 
	 * Can be used to avoid having to import MouseEvent 
	 * in each component using them.
	 * 
	 * @param e MouseEvent object to check.
	 * 
	 * @return Returns whether the MouseEvent was due 
	 * to a left-click.
	 */
	public static boolean checkLeftClick(MouseEvent e) {
		return (e.getButton() == 1);
	}
	
	/**
	 * Returns whether a given MouseEvent was a 
	 * middle-click.
	 * 
	 * Can be used to avoid having to import MouseEvent 
	 * in each component using them.
	 * 
	 * @param e MouseEvent object to check.
	 * 
	 * @return Returns whether the MouseEvent was due 
	 * to a middle-click.
	 */
	public static boolean checkMiddleClick(MouseEvent e) {
		return (e.getButton() == 2);
	}
	
	/**
	 * Returns whether a given MouseEvent was a 
	 * right-click.
	 * 
	 * Can be used to avoid having to import MouseEvent 
	 * in each component using them.
	 * 
	 * @param e MouseEvent object to check.
	 * 
	 * @return Returns whether the MouseEvent was due 
	 * to a right-click.
	 */
	public static boolean checkRightClick(MouseEvent e) {
		return (e.getButton() == 3);
	}
	
	/**
	 * Displays an error message with a given title 
	 * using JOptionPane when the number of issues is 
	 * greater than 0. 
	 * 
	 * This method is useful when dealing with inputs 
	 * of GUI-components that should display an error 
	 * messages when they contain input fields with 
	 * invalid inputs.
	 * 
	 * @param title The title of the error message 
	 * prompt.
	 * @param issues Number of invalid inputs in a 
	 * GUI-component wishing to display an error 
	 * message.
	 * 
	 * @return Returns the number of issues that was 
	 * passed into the method.
	 */
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
	
	/**
	 * Counts the number of invalid inputs based on 
	 * an array of boolean values representing input 
	 * checks where true stands for valid input and 
	 * false stands for invalid input.
	 * 
	 * This method can be used by GUI-components with 
	 * multiple input fields whose validity must be 
	 * checked and counted for the showErrorIfInvalid-
	 * method. This method also accepts a variable 
	 * number of boolean values making it very 
	 * versatile.
	 * 
	 * @param checks Boolean values representing 
	 * validity checks that should be counted for the 
	 * number of invalid inputs.
	 * 
	 * @return Returns the number of invalid inputs 
	 * passed into the method as arguments.
	 */
	public static int checkMultiple(boolean... checks) {
		int issues = 0;
		
		for( boolean check : checks )
		if( !check )
		issues++;
		
		return issues;
	}
}
