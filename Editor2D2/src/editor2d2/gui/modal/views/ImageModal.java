package editor2d2.gui.modal.views;

import javax.swing.JButton;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.assets.Image;

public class ImageModal extends ModalView<Image> {

	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		
			// File source field
		CTextField txtFileSource = new CTextField("File source:");
		
		modal.add(txtFileSource.render());
		modal.add(new JButton("Load"));
		
		return this.createDefaultModalView(modal);
	}
}
