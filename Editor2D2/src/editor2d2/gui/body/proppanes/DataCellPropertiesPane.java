package editor2d2.gui.body.proppanes;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.ColorPreviewPanel;
import editor2d2.model.project.assets.Data;
import editor2d2.model.project.scene.placeables.DataCell;
import editor2d2.model.project.scene.placeables.Placeable;

public class DataCellPropertiesPane extends PropertiesPane {
	
	
	public DataCellPropertiesPane(Placeable source) {
		super(source);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("data cell properties"));
		container.add(new JLabel("Asset: " + this.source.getAsset().getName()));
		
		Data src = ((DataCell) source).getData();
		
		JPanel containerColor = new ColorPreviewPanel(src.getColor(), src.getValue());
		container.add(containerColor);
		
		return container;
	}
}
