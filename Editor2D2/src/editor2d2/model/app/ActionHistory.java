package editor2d2.model.app;

import java.util.ArrayList;

import editor2d2.model.app.actions.Action;

public class ActionHistory {
	
	/**
	 * Default maximum number of <b>Actions</b> that will be stored
	 * in the action history. Once the action history this size it
	 * will begin to override the earliest <b>Actions</b>.
	 */
	public static final int DEFAULT_MAX_SIZE = 50;
	

		// List of Action that make up the action history
	private ArrayList<Action> actionHistory;
	
		// Index inside the action history list
	private int cursor;
	
		// Maximum number of Actions that will be stored in the
		// ActionHistory
	private int size;
	
	
	public ActionHistory(int size) {
		this.actionHistory = new ArrayList<Action>();
		this.cursor = -1;
		this.size = size;
	}
	
	public ActionHistory() {
		this(DEFAULT_MAX_SIZE);
	}
	
	
		// Logs an Action into the action history and advances the cursor.
		// If the cursor position is NOT at the final position, the actions
		// after the cursor will be removed and overridden.
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
	
		// Undoes the latest action in the action history and backtracks the
		// cursor
	public void undo() {
		if( this.cursor < 0 )
		return;
		
		this.actionHistory.get(this.cursor).undo();
		this.cursor--;
	}
	
		// Re-does the next action and advances the cursor
	public void redo() {
		if( this.cursor >= this.actionHistory.size() - 1 )
		return;
		
		this.cursor++;
		this.actionHistory.get(this.cursor).redo();
	}
	
		// Re-does all actions that were undone
	public void redoAll() {
		while( this.cursor < this.actionHistory.size() - 1 )
		redo();
	}
	
		// Undoes all actions in the action history
	public void undoAll() {
		while( this.cursor >= 0 )
		undo();
		
		this.cursor = -1;
	}
	
		// Resets the action history by removing all actions
		// and resetting the cursor position
	public void reset() {
		this.actionHistory.clear();
		this.cursor = -1;
	}
}
