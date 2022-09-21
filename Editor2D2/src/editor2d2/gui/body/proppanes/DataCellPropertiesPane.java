package editor2d2.gui.body.proppanes;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.assets.Asset;

public class DataCellPropertiesPane extends PropertiesPane {
	
	public DataCellPropertiesPane(Asset source) {
		this.source = source;
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("data cell properties"));
		
		return container;
	}
}
