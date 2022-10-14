package editor2d2.model.app.tool;

public abstract class Tool {
	
	public static final int PRIMARY_FUNCTION = 1;
	public static final int SECONDARY_FUNCTION = 2;
	public static final int TERTIARY_FUNCTION = 3;
	
	public static final int USE_FAILED = 1;
	public static final int USE_SUCCESSFUL = 2;
	public static final int USE_UNSPECIFIED = 3;
	public static final int USE_DEFAULT = 4;

		// Name of the Tool that will be shown in the UI
	protected String name;
	
		// Shortcut keyboard key
	protected String shortcutKey;
	
	
	protected Tool() {
		this.name = null;
		this.shortcutKey = "";
	}
	
	
		// Returns whether an outcome of a use-method was successful
	public static boolean checkSuccessfulUse(int outcome) {
		return (outcome == USE_SUCCESSFUL);
	}
	
	
		// Uses the Tool in a given ToolContext
		// CAN BE OVERRIDDEN IN CASE OF MORE ORDERS OF
		// FUNCTIONALITY
	public int use(ToolContext c) {
		int outcome = USE_UNSPECIFIED;
		
		switch( c.order )
		{
			case Tool.PRIMARY_FUNCTION:   outcome = usePrimary(c); break;
			case Tool.SECONDARY_FUNCTION: outcome = useSecondary(c); break;
			case Tool.TERTIARY_FUNCTION:  outcome = useTertiary(c); break;
		}
		
		return outcome;
	}
	
	protected int usePrimary(ToolContext c) {
		return USE_DEFAULT;
	}
	
	protected int useSecondary(ToolContext c) {
		return USE_DEFAULT;
	}
	
	protected int useTertiary(ToolContext c) {
		return USE_DEFAULT;
	}
	
		// Stops using the Tool in a given ToolContext
		// CAN BE OVERRIDDEN
	public int stop(ToolContext c) {
		return USE_DEFAULT;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	
	public void setName(String name) {
		if( name == null )
		return;
		
		this.name = name;
	}
}
