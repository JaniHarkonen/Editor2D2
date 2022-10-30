package editor2d2.modules.image.modal;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import editor2d2.gui.components.RequirementFilter;

public class RequireValidFile extends RequirementFilter<File> {

	private FileNameExtensionFilter[] filters;
	
	
	public RequireValidFile(FileNameExtensionFilter[] filters) {
		this.filters = filters;
	}
	
	
	@Override
	protected boolean validateInput() {
		if( this.input == null || this.input.equals("") )
		return false;
		
		File file = new File(this.input);
		String ext = getExtension(file);
		
		for( FileNameExtensionFilter filter : this.filters )
		for( String e : filter.getExtensions() )
		{
			if( !ext.equals(e) )
			continue;
		
			if( !file.exists() )
			return false;
			
			this.value = file;
			return true;
		}
		
		return false;
	}

	private String getExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		
		if( index < 0 )
		return null;
		
		return name.substring(index + 1);
	}
}
