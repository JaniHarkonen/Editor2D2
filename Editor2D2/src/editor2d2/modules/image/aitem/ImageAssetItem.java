package editor2d2.modules.image.aitem;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.body.assetpane.AssetItem;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.components.CImage;
import editor2d2.model.project.Asset;
import editor2d2.modules.image.asset.Image;

public class ImageAssetItem extends AssetItem {

	public ImageAssetItem(AssetPane host, Asset source) {
		super(host, source);
	}

	@Override
	protected JPanel drawIcon() {
		CImage icon = new CImage();
		Image src = (Image) this.source;		
		BufferedImage img = null;
		
		if( src != null )
		((Image) this.source).getImage();
		
		if( img == null )
		img = Application.resources.getGraphic("icon-null-texture");
		
		icon.setImage(img);
		
		return icon.render();
	}
}
