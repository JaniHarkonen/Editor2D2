package editor2d2.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.modules.image.asset.Image;

@SuppressWarnings("serial")
public class SpritePopupMenu extends JPopupMenu {

	private Folder currentFolder;
	
	private Asset currentSelection;
	
	private ActionListener selectionListener;
	
	
	public SpritePopupMenu(Folder initialFolder) {
		this.currentFolder = initialFolder;
		this.currentSelection = null;
		generate();
	}

	
	public void regenerate() {
		removeAll();
		generate();
		revalidate();
		repaint();
		pack();
	}
	
	public void addSelectionListener(ActionListener al) {
		this.selectionListener = al;
	}
	
	public void removeSelectionListener() {
		this.selectionListener = null;
	}
	
	private void generate() {
		
		for( Asset a : this.currentFolder.getAllAssets() )
		{
			String name = a.getName();
			
			if( a instanceof Folder )
			{
				JMenu menuFolder = new JMenu(name);
				menuFolder.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						switchFolder((Folder) a);
					}
				});
				
				add(menuFolder);
			}
			else if( a instanceof Image )
			{
				JMenuItem itemAsset = new JMenuItem(new AbstractAction(name) {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						actionSelect(e, a);
					}
				});
				
				add(itemAsset);
			}
		}
	}
	
	private void actionSelect(ActionEvent e, Asset a) {
		this.currentSelection = a;
		this.selectionListener.actionPerformed(e);
	}
	
	
	public void switchFolder(Folder newFolder) {
		this.currentFolder = newFolder;
		regenerate();
	}
	
	public Asset getSelection() {
		return this.currentSelection;
	}
}
