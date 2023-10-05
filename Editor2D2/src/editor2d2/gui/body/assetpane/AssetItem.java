package editor2d2.gui.body.assetpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.Asset;

/**
 * This class is used to draw a clickable Asset item in the 
 * AssetPane. The Asset can be selected via left-click. 
 * Selected Assets can be placed in the currently active 
 * Scene, edited by double-clicking the left mouse button 
 * or renamed or deleted through the drop down menu opened 
 * via right-click. 
 * 
 * @author User
 *
 */
public abstract class AssetItem extends GUIComponent {
	
	/**
	 * Reference to the AssetPane instance that the 
	 * AssetItem is to be rendered in.
	 */
	protected AssetPane host;
	
	/**
	 * Source Asset that the AssetItem represents and 
	 * provides interface to.
	 */
	protected Asset source;
	
	/**
	 * Whether the mouse is currently hovering over the 
	 * AssetItem in the AssetPane.
	 */
	protected boolean isMouseOver;
	
	/**
	 * JPanel that functions as the container for the 
	 * AssetItem's visual representation. Unlike most 
	 * GUI-components extending GUIComponent, AssetItem's 
	 * container JPanel is constructed upon 
	 * instantiation. Whereas the draw-method is simply 
	 * used to make additional changes to the look of 
	 * the AssetPane when the mouse is hovering over it.
	 * <br/><br/>
	 * 
	 * This class is abstract because each module should 
	 * implement their own AssetItem. This is done 
	 * because the way that the AssetItems are displayed 
	 * by different Asset classes may vary greatly. 
	 * Ultimately only the drawIcon-method should be 
	 * overridden to draw the icon used by the AssetItem.
	 * <br/><br/>
	 * 
	 * See the 
	 * AssetItem(AssetPane, Asset, String)-constructor 
	 * for more information on rendering the AssetItem.
	 * <br/><br/>
	 * 
	 * See the draw-method for more information on 
	 * drawing the AssetItem.
	 */
	protected JPanel container;
	
	/**
	 * Constructs an AssetItem instance that is to be 
	 * contained in a given AssetPane and is set to 
	 * represent a given source Asset. This method also 
	 * specifies a name that will be used to override 
	 * the name that is displayed in the AssetPane.
	 * <br/><br/>
	 * 
	 * Unlike most GUI-components, the AssetItem's 
	 * container JPanel is created and assembled upon 
	 * construction. This method will create the 
	 * container JPanel as well as apply MouseListeners 
	 * to it to listen for user's clicks.
	 * <br/><br/>
	 * 
	 * If you want to construct an AssetItem without 
	 * overriding its name, see the 
	 * AssetItem(AssetPane, Asset)-constructor for 
	 * more information.
	 * 
	 * @param host Reference to the hosting AssetPane 
	 * that the AssetItem is to be contained in.
	 * @param source Reference to the Asset that the 
	 * AssetItem is representing.
	 * @param overrideName The name that will be 
	 * displayed in the AssetPane instead of the real 
	 * name of the source Asset.
	 */
	protected AssetItem(AssetPane host, Asset source, String overrideName) {
		this.host = host;
		this.source = source;
		this.isMouseOver = false;
		
			// Constructs the JPanel of the AssetItem and 
			// applies a MouseListener used to listen for 
			// clicks
		this.container = GUIUtilities.createDefaultPanel();
		
		this.container.add(drawIcon());
		this.container.add(new JLabel(overrideName));
		this.container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
					// Handle left click
				if( GUIUtilities.checkLeftClick(e) )
				{
						// Single click -> select
					if( e.getClickCount() == 1 )
					actionSelect(e);
					
						// Double-click -> primary function (typically edit)
					if( e.getClickCount() == 2 )
					actionPrimaryFunction(e);
				}
				else if( GUIUtilities.checkRightClick(e) )	// Handle right click
				host.openPopup(e);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				isMouseOver = true;
				update();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				isMouseOver = false;
				update();
			}
		});
		
		this.container.setPreferredSize(new Dimension(128, 128));
	}
	/**
	 * Constructs an AssetItem instance that is to be 
	 * contained in a given AssetPane and is set to 
	 * represent a given source Asset. This method also 
	 * specifies a name that will be used to override 
	 * the name that is displayed in the AssetPane.
	 * <br/><br/>
	 * 
	 * Unlike most GUI-components, the AssetItem's 
	 * container JPanel is created and assembled upon 
	 * construction. This method will create the 
	 * container JPanel as well as apply MouseListeners 
	 * to it to listen for user's clicks.
	 * <br/><br/>
	 * 
	 * If you want to construct an AssetItem without 
	 * overriding its name, see the 
	 * AssetItem(AssetPane, Asset)-constructor for 
	 * more information.
	 * 
	 * @param host Reference to the hosting AssetPane 
	 * that the AssetItem is to be contained in.
	 * @param source Reference to the Asset that the 
	 * AssetItem is representing.
	 * @param overrideName The name that will be 
	 * displayed in the AssetPane instead of the real 
	 * name of the source Asset.
	 */
	
	/**
	 * Constructs an AssetItem instance that is to be 
	 * contained in a given AssetPane and is set to 
	 * represent a given source Asset. This method 
	 * uses the actual name of the source Asset as the 
	 * one displayed in the AssetPane. If you wish to 
	 * override the visual name of the Asset, see the 
	 * AssetItem(AssetPane, Asset, String)-constructor 
	 * for more information.
	 * <br/><br/>
	 * 
	 * Unlike most GUI-components, the AssetItem's 
	 * container JPanel is created and assembled upon 
	 * construction. This method will create the 
	 * container JPanel as well as apply MouseListeners 
	 * to it to listen for user's clicks.
	 * <br/><br/>
	 * 
	 * @param host Reference to the hosting AssetPane 
	 * that the AssetItem is to be contained in.
	 * @param source Reference to the Asset that the 
	 * AssetItem is representing.
	 */
	protected AssetItem(AssetPane host, Asset source) {
		this(host, source, source.getName());
	}
	

	@Override
	protected JPanel draw() {
		if( this.isMouseOver )
		this.container.setBorder(BorderFactory.createEtchedBorder());
		else
		this.container.setBorder(BorderFactory.createEmptyBorder());
		
		if( this.host.checkSelected(this.source) )
		this.container.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		return this.container;
	}
	
	/**
	 * Called upon selecting the Asset of the AssetItem 
	 * via a single left-click. Takes a given MouseEvent 
	 * object containing information regarding the mouse 
	 * click and selects the source Asset. If the 
	 * control-button (CTRL) is being held down, the 
	 * selection of the Asset will be toggled. Control 
	 * allows the selection of multiple Assets. The 
	 * hosting AssetPane will be notified of the 
	 * selection and the Controller will be requested to 
	 * select the Asset.
	 * <br/><br/>
	 * 
	 * Once an Asset is selected it can operated on in 
	 * the editor by creating a Placeable of it and 
	 * inserting it to the Scene or by editing the 
	 * Placeable properties. Selected Assets can be 
	 * edited, renamed and deleted through the 
	 * right-click drop down menu.
	 * 
	 * @param e Reference to the MouseEvent object 
	 * emitted by the click event.
	 */
	protected void actionSelect(MouseEvent e) {
		AssetPane host = this.host;
		Asset src = this.source;
		boolean isSelected = host.checkSelected(src);
		
		if( e.isControlDown() )
		{
			if( isSelected )
			host.deselectAsset(src);
			else
			host.addSelection(src);
		}
		else
		{
			host.selectAsset(src);
			Application.controller.selectAsset(this.source);
		}
	}
	
	/**
	 * Called upon double-clicking the AssetItem. By 
	 * default, the primary function of an AssetItem is 
	 * the editing of the Asset. Editing an Asset will 
	 * pop it up in the ModalWindow. This method can be 
	 * overridden by extending classes, such as in the 
	 * case of FolderItem.
	 * <br/><br/>
	 * 
	 * The MouseEvent object passed into the method is 
	 * not used, however, it will contain information 
	 * regarding the mouse click event.
	 * 
	 * See the ModalWindow-class for more information on 
	 * displaying modals.
	 * <br/><br/>
	 * 
	 * See the FolderItem-class for more information on 
	 * how Folders are displayed by the AssetPane.
	 * <br/><br/>
	 * 
	 * @param e Reference to the MouseEvent object 
	 * emitted by the click event.
	 */
	protected void actionPrimaryFunction(MouseEvent e) {
		this.host.actionEdit();
	}

	/**
	 * This method draws the icon used by the AssetItem. 
	 * Each module should implement their own AssetItem 
	 * class that overrides the drawIcon-method. This 
	 * method should only draw the icon as the name of 
	 * the Asset is already rendered by the AssetItem 
	 * class itself.
	 * 
	 * @return Reference to the JPanel containing the 
	 * icon that is to be displayed in the AssetPane.
	 */
	protected abstract JPanel drawIcon();
}
