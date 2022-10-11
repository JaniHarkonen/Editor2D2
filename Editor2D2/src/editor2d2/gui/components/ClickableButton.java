package editor2d2.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;

/**
 * This class acts as a non-GUI extension of the JButton Swing-class.
 * Clickable buttons have a lambda-function that will be executed
 * upon user click.
 * @author User
 *
 */
@SuppressWarnings("serial")
public class ClickableButton extends JButton {
	
	/**
	 * The ActionListener that listens for user clicks
	 * and executes the click action.
	 */
	private ActionListener clickListener;
	
	
	public ClickableButton(String title, Consumer<ActionEvent> onClick) {
		super(title);
		
		changeClickAction(onClick);
	}
	
	public ClickableButton(String title) {
		this(title, null);
	}

	public ClickableButton() {
		this("", null);
	}
	
	
	/**
	 * Changes the lambda-function that will be executed upon user click
	 * by removing the previous ActionListener that is handling user clicks
	 * and replacing it with a new ActionListener with the updated lambda-
	 * function.
	 * 
	 * @param onClick The lambda-function that will be ran upon click.
	 */
	public void changeClickAction(Consumer<ActionEvent> onClick) {
		if( onClick == null )
		return;
		
			// Remove the previous click listener
		if( this.clickListener != null )
		this.removeActionListener(this.clickListener);
		
			// Add the new click listener
		this.clickListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				onClick.accept(e);
			}
		};
		
		this.addActionListener(this.clickListener);
	}
}
