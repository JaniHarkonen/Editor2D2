package editor2d2.modules.data.proppane;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.body.proppane.PropertiesPane;
import editor2d2.gui.components.ColorPreviewPanel;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.placeable.DataCell;

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


	@Override
	public Asset getReferencedAsset() {
		return this.source.getAsset();
	}
}
