package editor2d2.model.app.actions;

/**
 * Actions are used by the ActionHistory to keep track 
 * of all the actions taken in the editor, such as 
 * when usint the "Place"-tool. Actions can be 
 * performed, undone or re-done. When calling the 
 * perform-method, the Action will be logged in the 
 * application's ActionHistory. In order to avoid 
 * logging the Action, the performImpl-method has to 
 * be used, however, this is NOT recommended.
 * <br/><br/>
 * 
 * Actions are typically coupled with Tools. Tools 
 * determine whether an Action can be carried out and 
 * provide the Action with the context in which they 
 * are to be performed. The Actions contain the actual 
 * implementation as well as the reverse- and re-
 * implementation of the action. Actions should also 
 * have fields that store all the relevant information 
 * that will be used by the undo- and redo-methods.
 * <br/><br/>
 * 
 * All Actions should implement this class.
 * <br/><br/>
 * 
 * See ActionHistory for more information on tracking 
 * Actions in the application.
 * <br/><br/>
 * 
 * See Tool for more information on editor tools.
 * 
 * @author User
 *
 */
public abstract class Action {
	
	/**
	 * Performs the Action within a given 
	 * ActionContext (typically provided by a Tool) 
	 * and logs it in the application's ActionHistory.
	 * <br/><br/>
	 * 
	 * If you want to avoid logging the Action in the 
	 * history, see the performImpl-method. <b>Notice: 
	 * </b>this method calls performImpl to execute 
	 * the Action logic, so all changes made to the 
	 * performImpl will also impact the overall 
	 * performance of the Action.
	 * <br/><br/>
	 * 
	 * See ActionContext for more information on 
	 * the contexts that Actions will be performed in.
	 * 
	 * @param c ActionContext containing all the 
	 * relevant information needed to carry out the 
	 * Action.
	 */
	public final void perform(ActionContext c) {
		performImpl(c);
		c.controller.getActionHistory().log(this);
	}
	
	/**
	 * Undoes the Action. This method does not 
	 * contact the ActionHistory, rather, the 
	 * ActionHistory calls it.
	 */
	public abstract void undo();
	
	/**
	 * Re-does the Action. This method does not 
	 * contact the ActionHistory, rather, the 
	 * ActionHistory calls it.
	 */
	public abstract void redo();
	
	/**
	 * The actual implementation of the Action's 
	 * performance. This method performs the 
	 * Action within a given ActionContext.
	 * <br/><br/>
	 * 
	 * See ActionContext for more information on 
	 * the contexts that Actions will be performed in.
	 * 
	 * @param c ActionContext containing all the 
	 * relevant information needed to carry out the 
	 * Action.
	 */
	public abstract void performImpl(ActionContext c);
}
