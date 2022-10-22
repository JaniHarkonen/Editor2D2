package editor2d2.modules.object.aitem;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.body.assetpane.AssetItem;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.components.CImage;
import editor2d2.model.project.Asset;

public class ObjectAssetItem extends AssetItem {

	public ObjectAssetItem(AssetPane host, Asset source) {
		super(host, source);
	}

	@Override
	protected JPanel drawIcon() {
		CImage icon = new CImage();
		icon.setImage(Application.resources.getGraphic("icon-null-object"));
		
		return icon.render();
	}
}
