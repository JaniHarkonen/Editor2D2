package editor2d2.gui.body.proppanes;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;

public class TilePropertiesPane extends GUIComponent {

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("tile properties"));
		
		return container;
	}
}
