package editor2d2.model.project.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.FactoryService;

/**
 * This class is used to write a Project into an external 
 * project file (text file). ProjectWriter iterates over 
 * the Assets, Scenes, Layers and Placeables of the 
 * Project and utilizes writers of each module to 
 * generate the contents of the project file. The project 
 * will be written to a file at a given path.
 * <br/><br/>
 * 
 * See AbstractWriter for more information on writers.
 * 
 * @author User
 *
 */
public class ProjectWriter {

	/**
	 * The <b>Project</b> was written into a file successfully.
	 */
	public static final int WRITE_SUCCESSFUL = 1;
	
	/**
	 * There was an error trying to write the <b>Project</b> into
	 * a file.
	 */
	public static final int WRITE_FAILED = -1;
	
	/**
	 * Writes a given Project into a file that is represented 
	 * by a File object. This method uses a BufferedWriter with 
	 * a FileWriter to write into the project file.
	 * <br/><br/>
	 * First the Assets stored in the Project will be written. 
	 * The ProjectWriter will recursively iterate through the 
	 * Folders of the Project starting from the root Folder 
	 * using the writeFolder-method (see writeFolder for more 
	 * information).
	 * <br/><br/>
	 * 
	 * Next, the Scenes are written. Scenes don't require a 
	 * writer as they are always written in the same way. A 
	 * Scene declaration statement has the following form: 
	 * <br/>
	 * scene "SCENE_NAME" WIDTH HEIGHT statement 
	 * COMPILATION_STATEMENT
	 * <br/><br/>
	 * 
	 * Third, the Layers of the Scene currently being iterated 
	 * over will be written using the appropriate writer. The 
	 * writer is determined based on the Asset that the Layer 
	 * accepts using the FactoryService.
	 * <br/><br/>
	 * 
	 * Fourth, the Placeables stored in they Layer currently 
	 * being iterated over will be written. Once again, the 
	 * proper writer will be used determined in the previous 
	 * step. The Placeables will be iterated over by going 
	 * through the cells of the grid of the Layer and writing 
	 * each found Placeable.
	 * <br/><br/>
	 * 
	 * Finally, the method either returns a 
	 * ProjectWriter.WRITE_FAILED or 
	 * ProjectWriter.WRITE_SUCCESSFUL depending on the success 
	 * of the write. If the write was successful, the project 
	 * file will be updated.
	 * 
	 * See writeProject(String, Project) to use a string-based 
	 * file path instead of having to create a File object.
	 * <br/><br/>
	 * 
	 * See FactoryService for more information on the various 
	 * factories that can be used.
	 * 
	 * @param file Reference to the File object that 
	 * represents the project file.
	 * @param project Reference to the Project that is to be 
	 * written in the project file.
	 * 
	 * @return An integer representing the outcome of the 
	 * write.
	 * <br/>
	 * <b>-1</b> : Write failed.
	 * <br/>
	 * <b>1</b>  : Write successful.
	 */
	public int writeProject(File file, Project project) {
		if( file.isDirectory() )
		return WRITE_FAILED;
		
		try 
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
				// Write Assets
			writeLine(bw, "assets");
				writeFolder(bw, project.getRootFolder(), true);
			writeLine(bw, "/assets");
			
				// Write Scenes
			for( Scene s : project.getAllScenes() )
			{
				String compilationStatement = s.getCompilationStatement();
				int statementLines = 0;
				
					// Determine compilation statement length
				if( compilationStatement.length() > 0 )
				statementLines = compilationStatement.split("\n").length;
				
				writeLine(bw,
					"scene \"" +
					s.getName() + "\" " +
					s.getWidth() + " " + 
					s.getHeight() + " statement " + statementLines
				);
				
					// Write compilation statement if there is one
				if( statementLines > 0 )
				writeLine(bw, compilationStatement);
				
					// Write Layers
				for( Layer l : s.getLayers() )
				{
					AbstractWriter writer = FactoryService.getFactories(l.getReferencedAsset().getAssetClassName()).createWriter();
					writeLine(bw, writer.writeLayer(l));
					
						// Write Placeables
					Grid objGrid = l.getObjectGrid();
					int w = l.getObjecGridRowLength(),
						h = l.getObjectGridColumnLength();
					
					for( int x = 0; x < w; x++ )
					for( int y = 0; y < h; y++ )
					{
						Gridable g = objGrid.getFast(x, y);
						
						if( g != null )
						writeLine(bw, writer.writePlaceable(g));
					}
					
					writeLine(bw, "/layer");
				}
				
				writeLine(bw, "/scene");
			}
			
			bw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			return WRITE_FAILED;
		}
		
		project.setFilepath(file.getPath());
		return WRITE_SUCCESSFUL;
	}
	
	/**
	 * Writes a given project at a given file path by 
	 * calling the writeProject(File, Project)-method.
	 * <br/><br/>
	 * 
	 * See the writeProject(File, Project)-method for more 
	 * information on writing projects.
	 * 
	 * @param path The path of the project file that will be 
	 * written.
	 * @param project Reference to the Project that is to be 
	 * written in the project file.
	 * 
	 * @return An integer representing the outcome of the 
	 * write.
	 * <br/>
	 * <b>-1</b> : Write failed.
	 * <br/>
	 * <b>1</b>  : Write successful.
	 */
	public int writeProject(String path, Project project) {
		return writeProject(new File(path), project);
	}
	
	/**
	 * Utility method that writes a given line using a 
	 * BufferedWriter. The line will have a newline symbol, \n, 
	 * at the end. If an error writing to the BufferedWriter 
	 * is encountered, an IOException is thrown.
	 * 
	 * @param bw Reference to the BufferedWriter object that 
	 * will be used to write the line.
	 * @param line The line that is to be written as a string.
	 * 
	 * @throws IOException If an error occurs trying to write 
	 * to the BufferedWriter.
	 */
	private void writeLine(BufferedWriter bw, String line) throws IOException {
		bw.write(line + "\n");
	}
	
	/**
	 * Writes a Folder and its contents recursively. The 
	 * proper writer will be determined via the FactoryService.
	 * The Asset will be written using a given BufferedWriter 
	 * object. Whether the Folder is the root Folder should 
	 * also be provided as the root Folder will not be declared 
	 * as a Folder, rather its contents will simply be listed.
	 * If an error writing to the BufferedWriter is encountered, 
	 * an IOException is thrown.
	 * 
	 * @param bw BufferedWriter object that will be used to 
	 * write the Folder.
	 * @param folder Reference to the Folder whose contents are 
	 * to be written into the project file.
	 * @param isRoot Whether the Folder being written is the 
	 * root Folder.
	 * 
	 * @throws IOException If an error occurs trying to write 
	 * to the BufferedWriter.
	 */
	private void writeFolder(BufferedWriter bw, Folder folder, boolean isRoot) throws IOException {
		if( !isRoot )
		writeLine(bw, "folder \"" + folder.getName() + "\"");
		
		for( Asset a : folder.getAllAssets() )
		{
			if( a instanceof Folder )
			{
				writeFolder(bw, (Folder) a, false);
				continue;
			}
			
			writeLine(bw, FactoryService.getFactories(a.getAssetClassName()).createWriter().writeAsset(a));
		}
		
		if( !isRoot )
		writeLine(bw, "/folder");
	}
}
