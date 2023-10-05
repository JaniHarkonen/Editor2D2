package editor2d2.model.project.writer;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

/**
 * Writers are used by the ProjectWriter to write an 
 * Asset, its Placeable or the Asset's Layer into a 
 * project file. Whenever one of these aspects is to 
 * be written into a file, the ProjectWriter creates 
 * an instance of the appropriate Writer and calls 
 * the proper method. Instead of writing directly 
 * into a file, each method returns a String that 
 * the ProjectWriter will then write to the file. 
 * Each module MUST implement their own writer.
 * <br/><br/>
 * 
 * See ProjectWriter for more information on writing 
 * project files.
 * 
 * @author User
 *
 */
public abstract class AbstractWriter {

	/**
	 * Produces a string that represents an Asset 
	 * declaration for a given Asset. By default, 
	 * the string produced has the following form:
	 * <br/>
	 * ASSET_CLASS_NAME "ASSET_NAME" UNIQUE_ID
	 * <br/><br/>
	 * 
	 * This method can be overridden by the 
	 * module's writer.
	 * 
	 * @param a Reference to the Asset whose 
	 * declaration is to be returned.
	 * 
	 * @return String containing the Asset 
	 * declaration.
	 */
	public String writeAsset(Asset a) {
		return a.getAssetClassName() + " \"" + a.getName() + "\" " + a.getIdentifier();
	}
	
	/**
	 * Produces a string that represents a Layer 
	 * declaration for a Layer that holds Placeables 
	 * derived from the Asset of this class 
	 * functions as a writer for. By default, the 
	 * string produced has the following form:
	 * <br/>
	 * layer ASSET_CLASS_NAME "LAYER_NAME" CELL_WIDTH 
	 * CELL_HEIGHT OPACITY IS_VISIBLE
	 * <br/><br/>
	 * 
	 * This method can be overridden by the 
	 * module's writer.
	 * 
	 * @param l Reference to the Layer whose 
	 * declaration is to be returned.
	 * 
	 * @return String containing the Layer 
	 * declaration.
	 */
	public String writeLayer(Layer l) {
		Grid ogrid = l.getObjectGrid();
		
		return (
			"layer " + l.getReferencedAsset().getAssetClassName() + " \"" +
			l.getName() + "\" " +
			ogrid.getCellWidth() + " " +
			ogrid.getCellHeight() + " " +
			l.getOpacity() + " " +
			(l.checkVisible() ? "true" : "false")
		);
	}
	
	/**
	 * Produces a string that represents the 
	 * placement fo a Placeable derived from the 
	 * Asset thatthis class functions as a writer
	 * to.
	 * <br/><br/>
	 * 
	 * This method has to be overridden by the 
	 * module's writer. No default functionality 
	 * has been provided by the AbstractWriter as 
	 * the implementations will be completely 
	 * different.
	 * 
	 * @param p Reference to the Placeable whose 
	 * placement is to be returned.
	 * 
	 * @return String containing the Placeable 
	 * placement instructions.
	 */
	public abstract String writePlaceable(Placeable p);
	
	/**
	 * Simply calls the writePlaceable-method 
	 * but instead of accepting a Placeable, a 
	 * Gridable is accepted and cast down to a 
	 * Placeable.
	 * <br/><br/>
	 * This method is useful when dealing with 
	 * objects with more ambiguous types. It 
	 * avoids the need for having to cast each 
	 * time.
	 * 
	 * @param g Reference to the Placeable 
	 * that is currently cast as a Gridable.
	 * 
	 * @return String containing the Gridable 
	 * placement instructions.
	 */
	public String writePlaceable(Gridable g) {
		return writePlaceable((Placeable) g);
	}
}
