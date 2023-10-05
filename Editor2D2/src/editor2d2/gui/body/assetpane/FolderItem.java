package editor2d2.gui.body.assetpane;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.components.CImage;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;

/**
 * This class represents a clickable folder that is 
 * shown in the AssetPane. Folders may contain 
 * Assets and other folders. Unlike AssetItems, the 
 * FolderItems will open the folder in the AssetPane 
 * upon double-click.
 * 
 * @author User
 *
 */
public class FolderItem extends AssetItem {
	
	/**
	 * Constructs a FolderItem instance that is 
	 * contained in a given host AssetPane and 
	 * represents a given Folder. This constructor 
	 * will also override the name displayed on the 
	 * FolderItem. 
	 * <br/><br/>
	 * 
	 * See the FolderItem(AssetPane, Asset)-method 
	 * if you don't need to override the name of the 
	 * folder.
	 * 
	 * @param host Reference to the AssetPane that 
	 * the FolderItem is to be rendered in.
	 * @param source Reference to the source Folder 
	 * that the FolderItem represents.
	 * @param overrideName The new name of the 
	 * folder.
	 */
	public FolderItem(AssetPane host, Asset source, String overrideName) {
		super(host, source, overrideName);
	}
	
	/**
	 * Constructs a FolderItem instance that is 
	 * contained in a given host AssetPane and 
	 * represents a given Folder.
	 * <br/><br/>
	 * 
	 * If you need to override the name of the 
	 * folder - such as when rendering a FolderItem 
	 * named using two dots .. - call the 
	 * FolderItem(AssetPane, Asset, String)-
	 * constructor instead.
	 * 
	 * @param host Reference to the AssetPane that 
	 * the FolderItem is to be rendered in.
	 * @param source Reference to the source Folder 
	 * that the FolderItem represents.
	 */
	public FolderItem(AssetPane host, Asset source) {
		super(host, source, source.getName());
	}

	
	@Override
	protected JPanel drawIcon() {
		CImage icon = new CImage();
		icon.setImage(Application.resources.getGraphic("icon-asset-folder"));
		
		return icon.render();
	}
	
	@Override
	protected void actionPrimaryFunction(MouseEvent e) {
		Application.controller.openFolder((Folder) this.source);
	}
}
