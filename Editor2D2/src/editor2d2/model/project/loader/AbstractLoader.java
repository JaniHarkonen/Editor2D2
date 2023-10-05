package editor2d2.model.project.loader;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;
import johnnyutils.johnparser.parser.ParsedCommand;

/**
 * Loader instances are used by the ProjectLoader when 
 * reading and parsing a project file. Loaders should 
 * contain all the logic used by a module to generate 
 * and place the Asset and the Placeables of the Asset 
 * class. Each module MUST implement their own loader.
 * <br/><br/>
 * 
 * The template variable A determines the type of the 
 * Asset that the Loader produces.
 * 
 * @author User
 *
 * @param <A> Type of asset the Loader produces.
 */
public abstract class AbstractLoader<A extends Asset> {

	/**
	 * Takes a given ParsedCommand, typically passed by 
	 * the ProjectLoader, that contains command and the 
	 * arguments that contain the Asset data. This 
	 * data will be used to construct the Asset 
	 * instance which will then be returned.
	 * 
	 * @param pc Reference to the ParsedCommand 
	 * containing the Asset data.
	 * 
	 * @return Reference to the Asset instances 
	 * constructed based on the Asset data.
	 */
	public abstract A loadAsset(ParsedCommand pc);
	
	/**
	 * Takes a given ParsedCommand, typically passed by 
	 * the ProjectLoader, that contains command and the 
	 * arguments that are to be used to create the 
	 * Placeable. The Placeable should be constructed 
	 * using the createPlaceable method of a given 
	 * source Asset. Finally the Placeable will be 
	 * placed onto a given target Layer and a reference 
	 * to the created Placeable will be returned.
	 * 
	 * @param pc Reference to the ParsedCommand 
	 * containing the Placeable data.
	 * @param source Reference to the source Asset the 
	 * Placeable will be derived from.
	 * @param targetLayer Reference to the target Layer 
	 * that the Placeable will be placed onto.
	 * 
	 * @return Reference to the derived Placeable 
	 * instance.
	 */
	public abstract Placeable loadPlaceable(ParsedCommand pc, A source, Layer targetLayer);
}
