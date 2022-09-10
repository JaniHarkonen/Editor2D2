package editor2d2.gui.body.layermgrpane;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;

public class LayerPane extends GUIComponent {

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		container.add(new JLabel("Layer1"));
		
		return container;
	}
}
