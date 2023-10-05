package editor2d2.model.project;

import editor2d2.model.project.scene.placeable.Placeable;

/**
 * Assets are sort of like classes of the things that 
 * can be placed into Scenes. When a Placeable is 
 * placed into a Scene, it is derived from an Asset. 
 * By default there are three types of Assets: Images, 
 * EObjects (objects) and Datas (data objects). More 
 * Assets can be created by extending this class, 
 * however, developers should try to extend the other 
 * existing Asset-classes first.
 * <br/><br/>
 * 
 * Each asset should have a unique identifier that will 
 * be used to reference the class inside the Project 
 * as well as a name that is a more human-readable 
 * identifier for the Asset. Each Asset-class should 
 * also have a name so that they can be referenced 
 * when dealing with factories. <b>Notice: </b> the 
 * name of the Asset MUST BE set in the constructor 
 * as the Asset name is often resolved by referencing 
 * an instance of the Asset while its exact type is 
 * ambiguous.
 * <br/><br/>
 * 
 * Each Asset whose instances (Pleaceables) are to 
 * be placed in a Scene should override the 
 * createPlaceable-method, which will be used by the 
 * application to spawn instances of the Asset. There 
 * are, however, some "Assets" that may not be placed 
 * in the Scene at all. These are called PseudoAssets, 
 * and they serve various purposes in the application.
 * <br/><br/>
 * 
 * See the "modules"-package for more information 
 * on specific Assets. 
 * <br/><br/>
 * 
 * See Scene for more information on Scenes.
 * <br/><br/>
 * 
 * See Placeable for more information on Placeables.
 * <br/><br/>
 * 
 * See FactoryService for more information on 
 * factories.
 * <br/><br/>
 * 
 * See PseudoAsset for more information on Assets 
 * that do not function in the typical way.
 * 
 * @author User
 *
 */
public abstract class Asset {
	
	/**
	 * Name of the Asset-class that will be displayed in the 
	 * editor. The class name should be set in the constructor 
	 * of each class as the name is often resolved through 
	 * an instance of the Asset where its exact type is 
	 * ambiguous.
	 */
	protected final String assetClassName;
	
	/**
	 * Name of the Asset as displayed in the resource panel.
	 * <b>Notice: </b>the Asset name is not the same as the 
	 * Asset-class name. Asset-class name referes to the 
	 * general name of the Asset, such as Image or Object, 
	 * whereas the Asset name referes to the name of the 
	 * instance of the class, which can be anything, for 
	 * example, "plant001".
	 */
	protected String name;

	/**
	 * A unique identifier associated with the Asset that 
	 * can be used to reference it later inside the 
	 * Project.
	 */
	protected String identifier;
	
	/**
	 * Constructs an Asset instance using a given Asset-class 
	 * name. The Asset-class name will be used to reference 
	 * the type of the Asset in the editor.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>This constructor is only to be called 
	 * by the classes extending Asset. These classes should 
	 * not let the assetClassName be accessed by anyone 
	 * outside of the class.
	 * <br/><br/>
	 * 
	 * See assetClassName-field for more information on 
	 * Asset-class names.
	 * 
	 * @param assetClassName Class name of the Asset type 
	 * that will be used in the editor when listing Asset
	 * types.
	 */
	protected Asset(String assetClassName) {
		this.assetClassName = assetClassName;
		this.name = null;
		this.identifier = null;
	}
	
	/**
	 * Creates a Placeable derived from the Asset that 
	 * can be placed into the Scene.
	 * 
	 * @return Returns a reference to the created Placeable.
	 */
	public Placeable createPlaceable() {
		return null;
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns the Asset-class name of the Asset that 
	 * is displayed in the editor when listing Asset 
	 * types.
	 * 
	 * @return Returns the Asset-class name of the 
	 * Asset.
	 */
	public String getAssetClassName() {
		return this.assetClassName;
	}
	
	/**
	 * Returns the name of the Asset instance as shown 
	 * in the editor.
	 * 
	 * @return Returns the name of the Asset instance.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the unique identifier of the Asset that 
	 * will be used to reference it inside the Project.
	 * 
	 * @return Returns the unique identifier of the 
	 * Asset.
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Sets the name of the Asset as shown in the editor.
	 * 
	 * @param name The new name of the Asset.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the unique identifier of the Asset that will 
	 * be used to reference it inside the Project.
	 * <b>Notice: </b>this will not affect the identifier 
	 * found inside the Project, it only impacts the 
	 * identifier found in this instance. Calling this on 
	 * an Asset that has already been added to the Project 
	 * may lead to undefined behavior and should be 
	 * avoided. In general, this method should only be called 
	 * immediately upon instantiating the Asset. 
	 * 
	 * @param identifier The new unique identifier of the 
	 * Asset.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
