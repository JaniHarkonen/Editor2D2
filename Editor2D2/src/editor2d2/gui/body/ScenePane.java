package editor2d2.gui.body;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.modal.views.ObjectModal;

public class ScenePane extends GUIComponent {

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("Scene "+Math.random()));
		container.add((new ObjectModal()).render());
		
		return container;
	}
}
