package editor2d2.gui.modal.views;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.assets.Data;

public class DataModal extends ModalView<Data> {

	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		
			// Data value field
		CTextField txtFileSource = new CTextField("Data value:");
		
			// Cell color settings
		JPanel containerCellColor = GUIUtilities.createDefaultPanel();
		containerCellColor.setBorder(BorderFactory.createTitledBorder("Cell color"));
		
			CTextField txtRed = new CTextField("R:");
			CTextField txtGreen = new CTextField("G:");
			CTextField txtBlue = new CTextField("B:");
			
		containerCellColor.add(txtRed.render());
		containerCellColor.add(txtGreen.render());
		containerCellColor.add(txtBlue.render());
		containerCellColor.add(new JButton("Color picker"));
		
		modal.add(txtFileSource.render());
		modal.add(containerCellColor);
		modal.add(new JButton("Load"));
		
		return this.createDefaultModalView(modal);
	}
}
