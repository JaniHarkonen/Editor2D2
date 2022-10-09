package editor2d2.modules.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CImage;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.resources.ImageExtensions;

public class ImageModal extends ModalView<Image> {
	
		// Text field containing the file path of the image
	private CTextField txtFileSource;
	
	public ImageModal(ModalWindow host, boolean useFactorySettings) {
		super(host, useFactorySettings);
		
		this.txtFileSource = new CTextField("File source:");
	}
	
	public ImageModal(ModalWindow host) {
		super(host);
	}
	
	
	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		BufferedImage image = this.source.getImage();
		
		if( image == null )
		image = Application.resources.getGraphic("icon-null-texture");
		
			// File source field
		this.txtFileSource.setText(this.source.getFilePath());
		this.txtFileSource.textField.setEditable(false);
		
		modal.add(this.txtFileSource.render());
		
			// Preview
		JLabel lPreview = new JLabel("Preview: ");
		lPreview.setMinimumSize(new Dimension(Integer.MAX_VALUE, lPreview.getHeight()));
		modal.add(lPreview);
		
				// Preview image
			CImage previewImage = new CImage();
			previewImage.setImage(image);
			
		modal.add(previewImage.render());
		
			// Load image button
		modal.add(new ClickableButton("Load", (e) -> { actionLoadFile(); }));
		
		return this.createDefaultModalView(modal);
	}
	
	@Override
	public void setFactorySettings() {
		Image source = new Image();
		long currms = System.currentTimeMillis();
		
		source.setIdentifier("IMG" + currms);
		source.setName("Image " + currms);
		
		this.source = source;
	}
	
	
		// Loads a new image file upon clicking "Load"
	private void actionLoadFile() {
		
			// Determine the allowed image extensions that will be displayed by the
			// file system dialog prompt
		FileSystemDialogSettings settings = new FileSystemDialogSettings();
		settings.filters = ImageExtensions.withAllImageExtensions();
		
			// Open file system dialog prompt and wait for response
		FileSystemDialogResponse res = Application.window.showOpenDialog(settings);
		
			// Prompt was not approved
		if( !res.isApproved )
		return;
		
			// Load the image
		String path = res.filepaths[0].getPath();
		BufferedImage img = Application.resources.loadExternalGraphic(path);
		
			// Image load failed
		if( img == null )
		return;
		
		this.source.setImage(img);
		this.txtFileSource.setText(path);
		
			// Save changes
		saveChanges(false);
		update();
	}
	
	@Override
	public void saveChanges(boolean doChecks) {
		super.saveChanges(doChecks);
		
		String fsource = this.txtFileSource.getText();
		
		if( doChecks && fsource.equals("") )
		return;
		
		this.source.setFilePath(fsource);
	}
}
