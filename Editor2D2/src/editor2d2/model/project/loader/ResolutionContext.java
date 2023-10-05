package editor2d2.model.project.loader;

import editor2d2.model.project.Project;

/**
 * Instances of this class will be passed onto UnresolvedAsset
 * wrappers by the ProjectLoader. The ResolutionContext should 
 * contain all relevant information neede to solve the 
 * dependencies.
 * 
 * See UnresolvedAsset for more information Asset 
 * interdependency resolution.
 * 
 * See ProjectLoader for more information on parsing and 
 * loading project files.
 * 
 * @author User
 *
 */
public class ResolutionContext {

	/**
	 * Reference to the Project that the resolved Asset will 
	 * belong to. Often only a reference to the Project is 
	 * enough as its getAsset-method can be called to load 
	 * get references to existing Assets.
	 */
	public Project hostProject;
}
