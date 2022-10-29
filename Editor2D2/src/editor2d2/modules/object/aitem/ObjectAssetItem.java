package editor2d2.modules.object.aitem;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.body.assetpane.AssetItem;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.components.CImage;
import editor2d2.model.project.Asset;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.object.asset.EObject;

public class ObjectAssetItem extends AssetItem {

	public ObjectAssetItem(AssetPane host, Asset source) {
		super(host, source);
	}

	@Override
	protected JPanel drawIcon() {
		CImage icon = new CImage();
		BufferedImage img = Application.resources.getGraphic("icon-asset-object-blank");
		Image spr = ((EObject) this.source).getSprite();
		BufferedImage sprimg = null;
		
		if( spr == null )
		sprimg = Application.resources.getGraphic("icon-null-object");
		else
		sprimg = spr.getImage();
			
		double	w = sprimg.getWidth(),
				h = sprimg.getHeight();
		double 	iconSize = 32;
		
		AffineTransform at = new AffineTransform();
		at.translate(64 / 4 - 1, 64 / 4 + 4);
		at.scale((iconSize + 1) / (double) w, iconSize / (double) h);
		((Graphics2D) img.getGraphics()).drawImage(sprimg, at, null);
	
		icon.setImage(img);
		
		return icon.render();
	}
}
