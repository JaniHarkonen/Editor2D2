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
	
	
		// Writes a given Project into a given file
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
	
		// Writes a given Project into a file with given
		// file path
	public int writeProject(String path, Project project) {
		return writeProject(new File(path), project);
	}
	
	
		// Utility method for writing a line without having to
		// insert \n at the end of each write-method
	private void writeLine(BufferedWriter bw, String line) throws IOException {
		bw.write(line + "\n");
	}
	
		// Writes a folder recursively
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
