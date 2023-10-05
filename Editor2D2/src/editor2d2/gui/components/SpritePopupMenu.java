package editor2d2.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.modules.image.asset.Image;

/**
 * This class displays a drop-down menu that lists all 
 * the image assets in the project (Assets that extend 
 * Image, see Image for more information on Image-
 * assets). The component will cycle through all 
 * folders of the project and generate sub-menus when 
 * a folder is encountered. An instance of this class 
 * can be used whenever a drop-down menu of Images is 
 * required.
 * 
 * This is a pure Swing-component that extends a Swing-
 * component JPopupMenu rather than implementing Swing-
 * components through composition like GUIComponents. 
 * 
 * See GUIComponent for more information on non-Swing
 * GUI-components.
 * 
 * @author User
 *
 */
@SuppressWarnings("serial")
public class SpritePopupMenu extends JPopupMenu {

	/**
	 * The Asset folder that the navigation is 
	 * currently in.
	 */
	private Folder currentFolder;
	
	/**
	 * Latest Asset that was selected through the 
	 * drop-down menu.
	 */
	private Asset currentSelection;
	
	/**
	 * ActionListener object that is used to notify 
	 * the parent component once a selection has 
	 * been made.
	 */
	private ActionListener selectionListener;
	
	/**
	 * Constructs a SpritePopupMenu instances that 
	 * is displaying a given Folder from the Project 
	 * initially. The Image-asset search also begins 
	 * from this folder while Images higher up in 
	 * the folder hierarchy are ignored.
	 * 
	 * @param initialFolder The initial Folder that 
	 * will be shown to the user.
	 */
	public SpritePopupMenu(Folder initialFolder) {
		this.currentFolder = initialFolder;
		this.currentSelection = null;
		generate();
	}

	/**
	 * Re-renders the drop-down menu by clearing its 
	 * contents and repainting it.
	 */
	public void regenerate() {
		removeAll();
		generate();
		revalidate();
		repaint();
		pack();
	}
	
	/**
	 * Attaches an ActionListener object to the 
	 * SpritePopupMenu instance. This listener will be 
	 * notified when an Image-asset has been selected. 
	 * <b>Notice: </b>only a single ActionListener can 
	 * be added to the SpritePopupMenu. If a 
	 * ActionListener is added when there already is 
	 * a ActionListener associated with this instance, 
	 * the previous listener will be overridden.
	 * 
	 * @param al ActionListener typically passed on 
	 * by the parent GUI-component that handles Image 
	 * selection.
	 */
	public void addSelectionListener(ActionListener al) {
		this.selectionListener = al;
	}
	
	/**
	 * Sets the current ActionListener listening for 
	 * Image selection to NULL. When there is no 
	 * ActionListener, nothing happens upon selection; 
	 * the drop-down menu will simply be closed.
	 */
	public void removeSelectionListener() {
		this.selectionListener = null;
	}
	
	/**
	 * Generates the drop-down menu itself by using 
	 * JPopupMenu's add-method to attach JMenuItems 
	 * representing Image-assets or JMenus 
	 * representing Folders to the SpritePopupMenu.
	 * 
	 * This method is called each time the drop-down 
	 * menu is updated with regenerate.
	 * 
	 * See regenerate-method for more information 
	 * on component re-rendering.
	 */
	private void generate() {
		for( Asset a : this.currentFolder.getAllAssets() )
		{
			String name = a.getName();
			
				// Folder encountered -> create a sub-menu
			if( a instanceof Folder )
			{
				JMenu menuFolder = new JMenu(name);
				menuFolder.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						switchFolder((Folder) a);
					}
				});
				
				add(menuFolder);
			}
			else if( a instanceof Image )	// Image encountered -> create default item
			{
				JMenuItem itemAsset = new JMenuItem(new AbstractAction(name) {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						actionSelect(e, a);
					}
				});
				
				add(itemAsset);
			}
		}
	}
	
	/**
	 * Called upon selecting an Image-asset. Simply 
	 * updates the currentSelection and notifies 
	 * the ActionListener that is typically used 
	 * by the parent GUI-component to handle the 
	 * selection. If no selection listener has been 
	 * set, nothing happens; the selection will 
	 * simply be updated.
	 * 
	 * @param e Reference to the ActionEvent object 
	 * containing the details of the selection.
	 * @param a Reference to the Image asset that was 
	 * selected by the user.
	 */
	private void actionSelect(ActionEvent e, Asset a) {
		this.currentSelection = a;
		
		if( this.selectionListener != null )
		this.selectionListener.actionPerformed(e);
	}
	
	/**
	 * Called upon selecting a Folder. The current 
	 * folder that the navigation is in will simply 
	 * be changed and the menu will be re-rendered 
	 * inside the selected folder.
	 * 
	 * @param newFolder Reference to the folder that 
	 * was selected.
	 */
	public void switchFolder(Folder newFolder) {
		this.currentFolder = newFolder;
		regenerate();
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a reference to the Image-asset that 
	 * was selected last.
	 * 
	 * @return Reference to the selected 
	 * Image-asset.
	 */
	public Asset getSelection() {
		return this.currentSelection;
	}
}
