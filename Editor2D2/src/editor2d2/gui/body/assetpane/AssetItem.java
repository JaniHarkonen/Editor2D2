package editor2d2.gui.body.assetpane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.Asset;

public class AssetItem extends GUIComponent {
	
		// Source Asset the item is based on
	private Asset source;
	
	
	public AssetItem(Asset source) {
		this.source = source;
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel(source.getName()));
		
		container.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if( e.getButton() == 1 )
				Application.controller.selectAsset(source);
			}
		});
		
		return container;
	}
}
