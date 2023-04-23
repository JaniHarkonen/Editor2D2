package editor2d2.model.project.loader;

import editor2d2.model.project.Asset;

/**
 * Some of the Assets loaded by the ProjectLoader contain 
 * interdependencies, however, because they are loaded at 
 * different times some Assets will reference 
 * not-yet-existing Assets. For example, an EObject may 
 * be listed in the project file earlier than an Image-
 * asset used by that EObject. In this case, the EObject 
 * cannot be coupled with its appropriate sprite.
 * <br/><br/>
 * 
 * UnresolvedAssets can be used to wrap the Asset and 
 * implement a method for resolving the dependencies 
 * later, once all the Asset dependencies have been 
 * loaded in. UnresolvedAsset wrappers should implement 
 * this interface which provides the framework for each 
 * UnresolvedAsset wrapper class. An UnresolvedAsset 
 * should always extend the Asset that it is functioning 
 * as a wrapper for. The UnresolvedAsset should hold a 
 * reference to the wrapped Asset as well as fields for 
 * all the dependencies that are to be resolved. 
 * Typically a dependency field is a String that holds 
 * the unique identifier of the Asset that will be loaded 
 * in later.
 * <br/><br/>
 * 
 * When all of the Assets have been loaded in, 
 * ProjectLoader will call the resolve-method on each 
 * unresolved Asset wrapper instance to fix the 
 * dependencies. The ProjectLoader will provide a 
 * ResolutionContext object that will contain all the 
 * relevant data and dependencies used by the resolution 
 * process.
 * <br/><br/>
 * 
 * See ProjectLoader for more information on loading 
 * and parsing project files.
 * <br/><br/>
 * 
 * See ResolutionContext for more information on data 
 * passed to the resolve-method.
 * 
 * @author User
 *
 */
public interface UnresolvedAsset {

	/**
	 * This method will be called by the ProjectLoader to 
	 * fix the Asset interdependencies. A given 
	 * ResolutionContext object will be passed by the 
	 * ProjectLoader to provide all the relevant 
	 * information needed to fix the dependencies.
	 * 
	 * @param c ResolutionContext object passed by the 
	 * ProjectLoader to fix the dependencies.
	 * 
	 * @return Returns a whether the resolution was 
	 * successful.
	 */
	public boolean resolve(ResolutionContext c);
	
	/**
	 * Returns a reference to the target Asset instance 
	 * that is to be resolved by the resolve-method.
	 * 
	 * @return Reference to the unresolved Asset.
	 */
	public Asset getUnresolvedAsset();
}
