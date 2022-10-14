package editor2d2.model.app;

import java.util.ArrayList;

import editor2d2.model.app.actions.Action;

public class ActionHistory {

		// List of Action that make up the action history
	private ArrayList<Action> actionHistory;
	
		// Index inside the action history list
	private int cursor;
	
	
	public ActionHistory() {
		this.actionHistory = new ArrayList<Action>();
		this.cursor = 0;
	}
	
	
		// Logs an Action into the action history and advances the cursor.
		// If the cursor position is NOT at the final position, the actions
		// after the cursor will be removed and overridden.
	public void log(Action action) {
		if( this.cursor < this.actionHistory.size() )
		{
			int s = this.actionHistory.size() - this.cursor;
			for( int i = 0; i < s; i++ )
			this.actionHistory.remove(this.cursor);
		}
		
		this.actionHistory.add(action);
		this.cursor++;
	}
	
		// Undoes the latest action in the action history and backtracks the
		// cursor
	public void undo() {
		if( this.cursor <= 0 )
		return;
		
		this.actionHistory.get(this.cursor).undo();
		this.cursor--;
	}
	
		// Re-does the next action and advances the cursor
	public void redo() {
		if( this.cursor >= this.actionHistory.size() )
		return;
		
		this.actionHistory.get(this.cursor).redo();
		this.cursor++;
	}
	
		// Re-does all actions that were undone
	public void redoAll() {
		while( this.cursor < this.actionHistory.size() )
		redo();
	}
	
		// Undoes all actions in the action history
	public void undoAll() {
		while( this.cursor >= 0 )
		undo();
		
		this.cursor = 0;
	}
	
		// Resets the action history by removing all actions
		// and resetting the cursor position
	public void reset() {
		this.actionHistory.clear();
		this.cursor = 0;
	}
}
