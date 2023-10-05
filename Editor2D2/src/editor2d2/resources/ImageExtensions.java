package editor2d2.resources;

import java.util.ArrayList;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This is a wrapper class for all file extensions considered
 * images.
 * 
 * @author User
 *
 */
public final class ImageExtensions {
	
	/**
	 * An array of all image file extensions supported as FileNameExtensionFilters.
	 */
	public static final FileNameExtensionFilter[] SUPPORTED_IMAGE_EXTENSIONS =
	{
		new FileNameExtensionFilter("PNG", "png"),
		new FileNameExtensionFilter("Bitmap", "bmp"),
		new FileNameExtensionFilter("GIF", "gif"),
		new FileNameExtensionFilter("JPEG", "jpg", "jpeg", "jfif", "jpe"),
		new FileNameExtensionFilter("TIFF", "tiff", "tif"),
		new FileNameExtensionFilter("TGA", "tga"),
		new FileNameExtensionFilter("DirectDraw Surface", "dds"),
		new FileNameExtensionFilter("WebP", "webp")
	};
	
	/**
	 * A File Name Extension Filter for all image extensions.
	 */
	public static final FileNameExtensionFilter ALL_IMAGE_EXTENSIONS = generateAllImageExtensions("All image files");

	
		// Do not instantiate
	private ImageExtensions() { }

	
	/**
	 * Generates a File Name Extension Filter encompassing all supported image 
	 * extension filters and assigns it a given title.
	 * 
	 * @param title Description of the filter in the file system dialog window.
	 * 
	 * @return A File Name Extension Filter that encompasses all the other image
	 * filters.
	 */
	private static FileNameExtensionFilter generateAllImageExtensions(String title) {
		ArrayList<String> allExtensions = new ArrayList<String>();
		
		for( FileNameExtensionFilter f : SUPPORTED_IMAGE_EXTENSIONS )
		for( String ext : f.getExtensions() )
		allExtensions.add(ext);
		
		String[] strAllExtensions = new String[allExtensions.size()];
		
		for( int i = 0; i < allExtensions.size(); i++ )
		strAllExtensions[i] = allExtensions.get(i);
		
		return new FileNameExtensionFilter(title, strAllExtensions);
	}
	
	
	/**
	 * Returns an array of all supported image extension filters with the filter
	 * encompassing all the image extensions added.
	 * 
	 * @return An array of all supported image extension filters as well as the
	 * all-encompassing filter.
	 */
	public static FileNameExtensionFilter[] withAllImageExtensions() {
		FileNameExtensionFilter[] exts = new FileNameExtensionFilter[SUPPORTED_IMAGE_EXTENSIONS.length + 1];
		exts[0] = ALL_IMAGE_EXTENSIONS;
		
		for( int i = 0; i < SUPPORTED_IMAGE_EXTENSIONS.length; i++ )
		exts[i + 1] = SUPPORTED_IMAGE_EXTENSIONS[i];
		
		return exts;
	}
}
