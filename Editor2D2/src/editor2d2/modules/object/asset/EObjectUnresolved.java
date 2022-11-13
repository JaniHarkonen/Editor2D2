package editor2d2.modules.object.asset;

import editor2d2.model.project.Asset;
import editor2d2.model.project.loader.ResolutionContext;
import editor2d2.model.project.loader.UnresolvedAsset;
import editor2d2.modules.image.asset.Image;

public class EObjectUnresolved extends EObject implements UnresolvedAsset {

	public EObject object;
	public String spriteIdentifier;

	@Override
	public boolean resolve(ResolutionContext c) {
		Image spr = (Image) c.hostProject.getAsset(this.spriteIdentifier);
		
		if( spr == null )
		return false;
		
		this.object.setSprite(spr);
		return true;
	}
	
	@Override
	public Asset getUnresolvedAsset() {
		return this.object;
	}
}
