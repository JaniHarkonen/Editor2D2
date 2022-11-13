package editor2d2.model.project.loader;

import editor2d2.model.project.Asset;

public interface UnresolvedAsset {

	public boolean resolve(ResolutionContext c);
	
	public Asset getUnresolvedAsset();
}
