package editor2d2.model.app.tool;

import java.awt.image.BufferedImage;

import editor2d2.Application;

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
	
		// Description on using the Tool
	protected String description;
	
		// Shortcut keyboard key
	protected String shortcutKey;
	
		// Icon representing the Tool in the Toolbar
	protected BufferedImage icon;
	
	
	protected Tool() {
		this.name = null;
		this.shortcutKey = "";
		this.icon = Application.resources.getGraphic("icon-null-object");
		this.description = "";
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
	
		// Use the primary functionality of the Tool
	protected int usePrimary(ToolContext c) {
		return USE_DEFAULT;
	}
	
		// Use the secondary functionality of the Tool
	protected int useSecondary(ToolContext c) {
		return USE_DEFAULT;
	}
	
		// Use the tertiary functionality of the Tool
	protected int useTertiary(ToolContext c) {
		return USE_DEFAULT;
	}
	
		// Stops using the Tool in a given ToolContext
		// CAN BE OVERRIDDEN
	public int stop(ToolContext c) {
		return USE_DEFAULT;
	}
	
	
		// Returns the name of the Tool
	public String getName() {
		return this.name;
	}
	
		// Returns the shortcut key that can be used
		// to quick-switch to the Tool
	public String getShortcutKey() {
		return this.shortcutKey;
	}
	
		// Returns the BufferedImage icon that represents
		// the Tool in the Toolbar
	public BufferedImage getIcon() {
		return this.icon;
	}
	
		// Returns the description of the Tool
	public String getDescription() {
		return this.description;
	}
	
	
		// Sets the name of the Tool
	public void setName(String name) {
		if( name == null )
		return;
		
		this.name = name;
	}
	
		// Sets the shortcut key that can be used to
		// quick-switch to the Tool
	public void setShortcutKey(String shortcut) {
		this.shortcutKey = shortcut;
	}
	
		// Sets the icon representing the Tool in the
		// toolbar
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
	
		// Sets the description of the Tool
	public void setDescription(String description) {
		this.description = description;
	}
}
