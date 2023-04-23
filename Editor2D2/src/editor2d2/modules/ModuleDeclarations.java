package editor2d2.modules;

/**
 * 
 * This class is used to declare all the modules used by 
 * the editor by calling the declare-method of each one.
 * The declare-methods of modules are found in their 
 * Factories-classes.
 * <br/><br/>
 * 
 * Basically, each module that wishes to be available 
 * in the editor MUST be declared here. To access the 
 * modules Factories.declare-method, the full path of 
 * the class, including the packages, should be used.
 * See the bootstrap-method to see how to exactly do 
 * this.
 * <br/><br/>
 * 
 * When the application starts up, the Application-
 * class initializes the FactoryService by calling its 
 * initialize-method. The FactoryService will in turn 
 * call the bootstrap-method of this class to declare 
 * the modules so that they can be used in the 
 * application.
 * <br/><br/>
 * 
 * This class should not be instantiated.
 * <br/><br/>
 * 
 * See the Factories-class explanation for more 
 * information on module delcarations.
 * <br/><br/>
 * 
 * See FactoryService for more information on its 
 * initialization via the initialize-method.
 * 
 * @author User
 *
 */
class ModuleDeclarations {
	
		// Do NOT instantiate
	private ModuleDeclarations() { }

	/**
	 * This method will delcare all the modules of the 
	 * application by calling the declare-method of 
	 * each one's Factories-class. All modules that 
	 * are to be available in the editor should be 
	 * declared here.
	 * 
	 * See Factories-class explanation for more 
	 * information on module declarations.
	 */
	public static void bootstrap() {
			// Declares Image-assets
		editor2d2.modules.image.Factories.declare();
		
			// Declares Data-assets
		editor2d2.modules.data.Factories.declare();
		
			// Declares Object-assets
		editor2d2.modules.object.Factories.declare();
	}
}
