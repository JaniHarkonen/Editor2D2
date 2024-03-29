package editor2d2.modules.image.modal;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import editor2d2.Application;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CImage;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.modules.image.asset.Image;
import editor2d2.resources.ImageExtensions;

public class ImageModal extends ModalView<Image> {
	
		// Text field containing the file path of the image
	private CTextField txtFileSource;
	
	public ImageModal(ModalWindow host, boolean useFactorySettings) {
		super(host, useFactorySettings);
		
		this.txtFileSource = new CTextField("File source:", new RequireValidFile(ImageExtensions.withAllImageExtensions()));
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
		addEmptySpace(modal);
		modal.add(lPreview);
		lPreview.setMinimumSize(new Dimension(Integer.MAX_VALUE, 16));
		
				// Preview image
			CImage imgPreview = new CImage();
			imgPreview.setImage(image);
		
			// Wrap inside a scroll pane
		JPanel imageContainer = GUIUtilities.createDefaultPanel();
		JScrollPane scroller = new JScrollPane(imgPreview.render());
		
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JScrollBar verticalScrollBar = scroller.getVerticalScrollBar();
		verticalScrollBar.setUnitIncrement(16);
		
		imageContainer.add(scroller);
		modal.add(imageContainer);
		
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
	public int validateInputs() {
		int issues = super.validateInputs();
		
		issues += GUIUtilities.checkMultiple(
				this.txtFileSource.getRequirementFilter().checkValid()
		);
		
		return issues;
	}
	
	@Override
	public boolean saveChanges(boolean doChecks) {
		boolean successful = super.saveChanges(doChecks);
		
		if( !successful )
		return false;
		
		String fsource = ((File) this.txtFileSource.getRequirementFilter().getValue()).getPath();
		
		this.source.setFilePath(fsource);
		return true;
	}

	
	@Override
	public Asset getReferencedAsset() {
		return this.source;
	}
}
