package editor2d2.gui.body.proppanes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.assets.Asset;
import editor2d2.model.project.assets.Data;
import editor2d2.model.project.assets.EObject;
import editor2d2.model.project.assets.Image;
import editor2d2.model.project.scene.placeables.Placeable;

public abstract class PropertiesPane extends GUIComponent {
	
		// Target placeable that has been configured by the properties pane
	//protected Placeable target;
	
		// Source Placeable whose properties the pane is representing
	//protected Asset source;
	protected Placeable source;
	
	
	protected PropertiesPane(Placeable source) {
		this.source = source;
		//this.target = null;
	}
	
	
	public static PropertiesPane createPropertiesPane(Placeable source) {
		if( source == null )
		return null;
		
		Asset srcAsset = source.getAsset();
		
		if( srcAsset instanceof Image )
		return new TilePropertiesPane(source);
		
		if( srcAsset instanceof EObject )
		return new InstancePropertiesPane(source);
		
		if( srcAsset instanceof Data )
		return new DataCellPropertiesPane(source);
		
		return null;
	}
	
	
	/**
	 * Called upon clicking the "Apply"-button. Applies the current
	 * configuration to the source Placeable.
	 * <br />
	 * By default, does nothing.
	 * <br /><br />
	 * CAN BE OVERRIDDEN!
	 * 
	 * @param ae ActionEvent-object that was created as a result of
	 * the button being clicked. (Most likely wont be used)
	 */
	public void actionApply(ActionEvent ae) { }
	
	
	/**
	 * Creates a JPanel of a default properties pane wrapping a given body
	 * within. This can be used in the draw-methods of the subclasses
	 * to include the default features of a properties pane. These features
	 * include: <br /><br />
	 * - a top bar with a title <br />
	 * - the name of the asset the Placeable is based on <br />
	 * - controls for applying changes ("Apply"-button)
	 * 
	 * @param title Title of the properties pane.
	 * 
	 * @param body JPanel representing the body of the properties pane. That
	 * is, the part that actually differs.
	 * 
	 * @return Returns a reference to the generated JPanel representing the
	 * properties pane. 
	 */
	protected JPanel createDefaultPropertiesPaneContainer(String title, JPanel body) {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel(title));
		container.add(new JLabel("Asset: " + this.source.getAsset().getName()));
		
		container.add(body);
		
		JButton btnApplyChanges = new JButton("Apply");
		btnApplyChanges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionApply(e);
			}
		});
		
		container.add(btnApplyChanges);
		
		return container;
	}
}
