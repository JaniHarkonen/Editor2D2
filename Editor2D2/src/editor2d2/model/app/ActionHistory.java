package editor2d2.model.app;

import java.util.ArrayList;

import editor2d2.model.app.actions.Action;

/**
 * ActionHistory is a manager-type class that keeps 
 * track of all the Actions taken in the application.
 * ActionHistory should not be accessible to the 
 * application components, rather, the Controller 
 * should function as an interface that takes in 
 * requests that may result in Actions.
 * 
 * When an Action needs to be tracked it can be 
 * logged into the ActionHistory by calling the log-
 * method of this class. If Actions were undone 
 * upon logging a new one all Actions after the 
 * new one will be overridden. Actions can be undone 
 * via the undo-method or redone via the redo-method.
 * 
 * All Actions are stored in an ArrayList whose size 
 * is capped. By default, the ActionHistory tracks 50 
 * Actions. When the Action cap is exceeded oldest 
 * Actions start getting removed as new Actions are 
 * logged.
 * 
 * See Action for more information on application 
 * Actions.
 * 
 * @author User
 *
 */
public class ActionHistory {
	
	/**
	 * Default maximum number of <b>Actions</b> that will be stored
	 * in the action history. Once the action history this size it
	 * will begin to override the earliest <b>Actions</b>.
	 */
	public static final int DEFAULT_MAX_SIZE = 50;
	
	/**
	 * ArrayList containing the list of Actions taken. <b>Notice: 
	 * </b>the size of this list is capped. See 
	 * DEFAULT_MAX_SIZE-field and size-field for more information 
	 * on size caps.
	 */
	private ArrayList<Action> actionHistory;
	
	/**
	 * Cursor that keeps track of the position in history that 
	 * the ActionHistory is currently at. The cursor moves lower 
	 * through the indices when undoing Actions and advances 
	 * when redoing of logging new Actions.
	 */
	private int cursor;

	/**
	 * Maximum number of Actions that can be logged in the 
	 * Action ArrayList. When this number is exceeded oldest 
	 * Actions in the list start getting removed as newer 
	 * Actions are logged.
	 */
	private int size;
	
	/**
	 * Constructs an ActionHistory instance that is capped at 
	 * the given number of Actions. Once the Action history 
	 * exceeds the cap, oldest Actions start getting removed 
	 * as new Actions are logged.
	 * 
	 * @param size Maximum number of Actions that can be stored 
	 * in the Action history.
	 */
	public ActionHistory(int size) {
		this.actionHistory = new ArrayList<Action>();
		this.cursor = -1;
		this.size = size;
	}
	
	/**
	 * Constructs an ActionHistory instance with default 
	 * settings whose size is capped at the 
	 * ActionHistory.DEFAULT_MAX_SIZE. See the 
	 * DEFAULT_MAX_SIZE-field and size-field for more 
	 * information on size caps.
	 */
	public ActionHistory() {
		this(DEFAULT_MAX_SIZE);
	}
	
	/**
	 * Logs a given Action into the Action history list and 
	 * advances the history cursor. If the cursor is not at the 
	 * final position (i.e. Actions were undone before the log 
	 * call), all Actions after the cursor will be overridden 
	 * and, subsequently, lost.
	 * 
	 * See cursor-field for more information on the history 
	 * cursror.
	 * 
	 * @param action Action that is to be logged in the Action 
	 * history.
	 */
	public void log(Action action) {
		if( this.cursor < this.actionHistory.size() - 1 )
		{
			int s = this.actionHistory.size() - this.cursor - 1;
			for( int i = 0; i < s; i++ )
			this.actionHistory.remove(this.cursor + 1);
		}
		
		this.actionHistory.add(action);
		this.cursor++;
		
			// Override earliest Actions if the history exceeds the maximum
			// allowed size
		while( this.actionHistory.size() > size )
		{
			this.actionHistory.remove(0);
			this.cursor--;
		}
	}
	
	/**
	 * Undoes the Action currently being pointed at by the 
	 * history cursor and moves the cursor back by one index. 
	 * If no Actions were taken before the history cursor, 
	 * nothing happens.
	 */
	public void undo() {
		if( this.cursor < 0 )
		return;
		
		this.actionHistory.get(this.cursor).undo();
		this.cursor--;
	}
	
	/**
	 * Re-does the Action immediately after the one currently 
	 * pointed at by the history cursor and advances the 
	 * cursor by one index. If the cursor is already at the 
	 * final position in the history, nothing happens.
	 */
	public void redo() {
		if( this.cursor >= this.actionHistory.size() - 1 )
		return;
		
		this.cursor++;
		this.actionHistory.get(this.cursor).redo();
	}
	
	/**
	 * Re-does all Actions after the history cursor and 
	 * advances the history cursor to the final position.
	 */
	public void redoAll() {
		while( this.cursor < this.actionHistory.size() - 1 )
		redo();
	}
	
	/**
	 * Undoes all Actions available in the history starting 
	 * at the current history cursor position and moves the 
	 * cursor back to the first position.
	 */
	public void undoAll() {
		while( this.cursor >= 0 )
		undo();
		
		this.cursor = -1;
	}
	
	/**
	 * Resets the Action history by clearing the Action 
	 * ArrayList and resetting the history cursor position.
	 */
	public void reset() {
		this.actionHistory.clear();
		this.cursor = -1;
	}
}
