package editor2d2.gui.body;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;

public class Toolbar extends GUIComponent {

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("toolbarbell"));
		container.add(new JLabel("google"));
		container.add(new JLabel("shit"));
		container.add(new JLabel("toolbarbell"));
		container.add(new JLabel("google"));
		container.add(new JLabel("shit"));
		container.add(new JLabel("toolbarbell"));
		container.add(new JLabel("google"));
		container.add(new JLabel("shit"));
		container.add(new JLabel("toolbarbell"));
		container.add(new JLabel("google"));
		container.add(new JLabel("shit"));
		
		return container;
	}
}
