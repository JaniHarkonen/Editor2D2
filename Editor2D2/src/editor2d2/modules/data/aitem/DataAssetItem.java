package editor2d2.modules.data.aitem;

import javax.swing.JPanel;

import editor2d2.gui.body.assetpane.AssetItem;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.components.ColorPreviewPanel;
import editor2d2.model.project.Asset;
import editor2d2.modules.data.asset.Data;

public class DataAssetItem extends AssetItem {

	public DataAssetItem(AssetPane host, Asset source) {
		super(host, source);
	}

	@Override
	protected JPanel drawIcon() {
		Data src = (Data) source;
		
		return new ColorPreviewPanel(src.getColor(), src.getValue(), 64, 64);
	}
}
