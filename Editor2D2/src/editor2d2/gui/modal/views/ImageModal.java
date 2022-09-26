package editor2d2.gui.modal.views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.assets.Image;
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
		BufferedImage image = source.getImage();
		
		if( image == null )
		image = Application.resources.getGraphic("icon-null-texture");
		
			// File source field
		this.txtFileSource.setText(source.getFilePath());
		this.txtFileSource.textField.setEditable(false);
		
		modal.add(this.txtFileSource.render());
		
			// Preview
		JLabel lPreview = new JLabel("Preview: ");
		lPreview.setMinimumSize(new Dimension(Integer.MAX_VALUE, lPreview.getHeight()));
		modal.add(lPreview);
		
		final BufferedImage finalImage = image;	// Local variables that are passed into classes must be de-facto final
		
				// Preview image
			@SuppressWarnings("serial")
			JPanel preview = new JPanel() {
				
				@Override
				protected void paintComponent(Graphics g) {
					g.drawImage(finalImage, 0, 0, null);
				}
			};
			
		preview.setMinimumSize(new Dimension(finalImage.getWidth(), finalImage.getHeight()));
		modal.add(preview);
		
			// Load image button
		JButton btnLoadImage = new JButton("Load");
		btnLoadImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionLoadFile();
			}
		});
		
		modal.add(btnLoadImage);
		
		return this.createDefaultModalView(modal);
	}
	
	@Override
	public void setFactorySettings() {
		Image source = new Image();
		source.setIdentifier("a");
		source.setName("Image " + System.currentTimeMillis());
		
		this.source = source;
	}
	
	
		// Loads a new image file upon clicking "Load"
	private void actionLoadFile() {
		FileSystemDialogSettings settings = new FileSystemDialogSettings();
		settings.filters = ImageExtensions.withAllImageExtensions();
		
		FileSystemDialogResponse res = Application.window.showOpenDialog(settings);
		
		if( !res.isApproved )
		return;
		
		String path = res.filepaths[0].getPath();
		BufferedImage img = Application.resources.loadExternalGraphic(path);
		
		if( img == null )
		return;
		
		this.source.setImage(img);
		this.txtFileSource.setText(path);
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

	@Override
	protected void actionCreate() {
		saveChanges(true);
		finalizeCreation();
	}
}
