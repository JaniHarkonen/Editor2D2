package editor2d2.gui.body.layermgrpane;

import javax.swing.JButton;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;

public class LayerManagerPane extends GUIComponent {

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createTitledPanel("Layers", GUIUtilities.BOX_PAGE_AXIS);
		
			// Controls area
		JPanel containerControls = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		/*containerControls.add(new JButton("+"));
		containerControls.add(new JButton("-"));
		containerControls.add(new JButton("..."));
		
		container.add(containerControls);
		container.add((new LayerPane()).render());
		container.add((new LayerPane()).render());
		container.add((new LayerPane()).render());*/
		
		container.add((new LayerPropertiesPane()).render());
		
		
		return container;
	}
}
