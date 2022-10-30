package editor2d2.modules.data.modal;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ColorPreviewPanel;
import editor2d2.gui.components.requirements.RequireIntegerBetween;
import editor2d2.gui.components.requirements.RequireStringNonEmpty;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.modules.data.asset.Data;

public class DataModal extends ModalView<Data> {
	
		// Data value text field
	private CTextField txtDataValue;
	
		// Cell color text field - RED
	private CTextField txtColorRed;
	
		// Cell color text field - GREEN
	private CTextField txtColorGreen;
	
		// Cell color text field - BLUE
	private CTextField txtColorBlue;
	

	public DataModal(ModalWindow host, boolean useFactorySettings) {
		super(host, useFactorySettings);
		
		this.txtDataValue = new CTextField("Data value:", new RequireStringNonEmpty());
		this.txtColorRed = new CTextField("R:", new RequireIntegerBetween(0, 255));
		this.txtColorGreen = new CTextField("G:", new RequireIntegerBetween(0, 255));
		this.txtColorBlue = new CTextField("B:", new RequireIntegerBetween(0, 255));
	}
	
	public DataModal(ModalWindow host) {
		super(host);
	}
	
	
	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		String srcValue = this.source.getValue();
		
		Color srcColor = this.source.getColor();
		int colr = srcColor.getRed(),
			colg = srcColor.getGreen(),
			colb = srcColor.getBlue();
		
			// Data value field
		this.txtDataValue.setText(srcValue);
		
			// Cell color settings
		JPanel containerCellColor = GUIUtilities.createDefaultPanel();
		containerCellColor.setBorder(BorderFactory.createTitledBorder("Cell color"));
			
			JPanel colorPanel = new ColorPreviewPanel(srcColor, srcValue);
			colorPanel.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					actionColorPicker();
				}
			});
		
			// RGB fields
		this.txtColorRed.setText(""+colr);
		this.txtColorGreen.setText(""+colg);
		this.txtColorBlue.setText(""+colb);
		
		containerCellColor.add(colorPanel);
		containerCellColor.add(this.txtColorRed.render());
		containerCellColor.add(this.txtColorGreen.render());
		containerCellColor.add(this.txtColorBlue.render());
		
		modal.add(this.txtDataValue.render());
		modal.add(containerCellColor);
		
		return this.createDefaultModalView(modal);
	}

	@Override
	public void setFactorySettings() {
		Data source = new Data();
		long currms = System.currentTimeMillis();
		
		source.setIdentifier("DATA" + currms);
		source.setName("Data " + currms);
		
		this.source = source;
	}
	
	@Override
	public int validateInputs() {
		int issues = super.validateInputs();
		issues += GUIUtilities.checkMultiple(
				this.txtDataValue.getRequirementFilter().checkValid(),
				this.txtColorRed.getRequirementFilter().checkValid(),
				this.txtColorGreen.getRequirementFilter().checkValid(),
				this.txtColorBlue.getRequirementFilter().checkValid()
		);
		
		return issues;
	}
	
	@Override
	public boolean saveChanges(boolean doChecks) {
		boolean successful = super.saveChanges(doChecks);
		
		if( !successful )
		return false;
		
		String val = (String) this.txtDataValue.getRequirementFilter().getValue();
		int colr = (Integer) this.txtColorRed.getRequirementFilter().getValue();
		int colg = (Integer) this.txtColorGreen.getRequirementFilter().getValue();
		int colb = (Integer) this.txtColorBlue.getRequirementFilter().getValue();
		
		this.source.setValue(val);
		this.source.setColor(new Color(colr, colg, colb));
		
		return true;
	}
	
	private void actionColorPicker() {
		Color pick = JColorChooser.showDialog(host.getDialog(), "Choose cell color", this.source.getColor());
		
		if( pick == null )
		return;
		
		saveChanges(false);
		this.source.setColor(pick);
		update();
	}
	
	
	@Override
	public Asset getReferencedAsset() {
		return this.source;
	}
}
