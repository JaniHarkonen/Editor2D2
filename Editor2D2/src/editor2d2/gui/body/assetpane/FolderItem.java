package editor2d2.gui.body.assetpane;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.components.CImage;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;

public class FolderItem extends AssetItem {
	
	public FolderItem(AssetPane host, Asset source, String overrideName) {
		super(host, source, overrideName);
	}
	
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
	protected void actionSelect() {
		Application.controller.openFolder((Folder) this.source);
	}
}
