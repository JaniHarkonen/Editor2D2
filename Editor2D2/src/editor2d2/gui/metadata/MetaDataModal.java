package editor2d2.gui.metadata;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import editor2d2.Application;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.model.project.PseudoAsset;
import editor2d2.model.project.scene.Scene;

/**
 * This ModalView is to be used to display the meta data 
 * of the currently active Scene. This class utilizes 
 * the PseudoAsset as a template because the ModalView 
 * isnt supposed to operate on an Asset.
 * <br/><br/>
 * The ModalView simply contains a JTextArea component 
 * that is used to display and edit the meta data of 
 * the Scene.
 * 
 * @author User
 *
 */
public class MetaDataModal extends ModalView<PseudoAsset> {

	/**
	 * This is a PseudoAsset that is used for 
	 * compatibility purposes.
	 * 
	 * @author User
	 *
	 */
	private class ContextAsset extends PseudoAsset {
		/**
		 * Constructs a default ContextAsset instance.
		 */
		public ContextAsset() {
			this.name = "Scene meta data";
		}
	}
	
	/**
	 * The JTextArea that is used to display and edit 
	 * the meta data of the currently active Scene.
	 */
	private JTextArea txtCompilationStatement;
	
	/**
	 * Constructs a MetaDataModal instance that is 
	 * hosted by a given ModalWindow instance.
	 * 
	 * @param host
	 */
	public MetaDataModal(ModalWindow host) {
		super(host);
		
		this.source = new ContextAsset();
		this.txtCompilationStatement = new JTextArea();
	}

	@Override
	protected JPanel draw() {
		Scene activeScene = Application.controller.getActiveScene();
		JPanel container = GUIUtilities.createDefaultPanel();
		
		if( activeScene == null )
		return container;
		
		this.txtCompilationStatement.setFont(new Font("Courier New", Font.PLAIN, 14));
		this.txtCompilationStatement.setText(activeScene.getCompilationStatement());
		
		container.add(this.txtCompilationStatement);
		container.setBorder(BorderFactory.createTitledBorder("Compilation statement"));
		
		JPanel controlsContainer = GUIUtilities.createDefaultPanel();
		controlsContainer.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		JPanel lineContainer = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		lineContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
		
			ClickableButton btnSaveChanges = new ClickableButton("Save", (e) -> { actionSaveChanges(); });
			lineContainer.add(btnSaveChanges);
			
			ClickableButton btnCancel = new ClickableButton("Cancel", (e) -> { actionCancel(); });
			lineContainer.add(btnCancel);
			
			controlsContainer.add(lineContainer);
		
		container.add(controlsContainer);
		return container;
	}

	@Override
	public Asset getReferencedAsset() {
		return null;
	}

	@Override
	public void setFactorySettings() { }
	
	/**
	 * Called upon saving the changes to the Scene's meta 
	 * data. The current meta data entered in the 
	 * JTextArea will be stored in the Scene and the 
	 * ModalWindow will be closed.
	 */
	private void actionSaveChanges() {
		Scene activeScene = Application.controller.getActiveScene();
		String compilationStatement = this.txtCompilationStatement.getText();
		
		activeScene.setCompilationStatement(compilationStatement);
		this.host.closeModalWindow();
	}
}
