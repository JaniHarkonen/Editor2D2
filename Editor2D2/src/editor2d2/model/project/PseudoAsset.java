package editor2d2.model.project;

/**
 * PseudoAssets can be used when an object needs to 
 * treated like an Asset, yet do not need the full 
 * features of an Asset, such as the ability to 
 * create Placeables.
 * <br/><br/>
 * 
 * PseudoAsset do not have Asset-class names, thus, 
 * the constructor will call the super constructor 
 * (Asset constructor) with NULL.
 * <br/><br/>
 * 
 * See Asset for more information on Asset-class 
 * names.
 * <br/><br/>
 * 
 * See Folder for an example of a PseudoAsset.
 * 
 * @author User
 *
 */
public abstract class PseudoAsset extends Asset {

	/**
	 * Constructs a PseudoAsset instance and nulls 
	 * the Asset-class name.
	 */
	public PseudoAsset() {
		super(null);
		
	}

}
