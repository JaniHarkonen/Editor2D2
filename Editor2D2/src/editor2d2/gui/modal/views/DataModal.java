package editor2d2.gui.modal.views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.assets.Data;

public class DataModal extends ModalView<Data> {
	
		// Default color panel width
	public static final int DEFAULT_COLOR_PANEL_WIDTH = 64;
	
		// Default color panel height
	public static final int DEFAULT_COLOR_PANEL_HEIGHT = 64;
	
	
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
		
		this.txtDataValue = new CTextField("Data value:");
		this.txtColorRed = new CTextField("R:");
		this.txtColorGreen = new CTextField("G:");
		this.txtColorBlue = new CTextField("B:");
	}
	
	public DataModal(ModalWindow host) {
		super(host);
	}
	
	
	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		String srcValue = this.source.getValue();
		Color srcColor = source.getColor();
			int colr = srcColor.getRed(),
				colg = srcColor.getGreen(),
				colb = srcColor.getBlue();
		
			// Data value field
		this.txtDataValue.setText(srcValue);
		
			// Cell color settings
		JPanel containerCellColor = GUIUtilities.createDefaultPanel();
		containerCellColor.setBorder(BorderFactory.createTitledBorder("Cell color"));
			
			// Color preview panel
			@SuppressWarnings("serial")
			JPanel colorPanel = new JPanel() {
				
				@Override
				protected void paintComponent(Graphics g) {
					int pw = DEFAULT_COLOR_PANEL_WIDTH,
						ph = DEFAULT_COLOR_PANEL_HEIGHT;
					
						// Draw color panel
					g.setColor(srcColor);
					g.fillRect(0, 0, pw, ph);
					
						// Draw data data value
					Color invColor = Color.BLACK;
					float invColorBrightness = 1 - Color.RGBtoHSB(colr, colg, colb, null)[2];
					
					if( invColorBrightness < 0.45 || invColorBrightness > 0.55 )
					invColor = new Color(Color.HSBtoRGB(0, 0, invColorBrightness));
					
					g.setColor(invColor);
					
					FontMetrics fm = g.getFontMetrics();
					Rectangle2D textDimensions = fm.getStringBounds(srcValue, g);
					
					g.drawString(srcValue, (int) ((pw / 2) - textDimensions.getCenterX()), (int) ((ph / 2) - textDimensions.getCenterY()));
				}
			};
			
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
		source.setIdentifier("b");
		source.setName("Data " + System.currentTimeMillis());
		source.setValue("0");
		source.setColor(Color.WHITE);
		
		this.source = source;
	}
	
	@Override
	public void saveChanges(boolean doChecks) {
		super.saveChanges(doChecks);
		
		String val = this.txtDataValue.getText();
		String colr = this.txtColorRed.getText();
		String colg = this.txtColorGreen.getText();
		String colb = this.txtColorBlue.getText();
		
		if( doChecks && (val.equals("") || colr.equals("") || colg.equals("") || colb.equals("")) )
		return;
		
		this.source.setValue(val);
		this.source.setColor(new Color(Integer.parseInt(colr), Integer.parseInt(colg), Integer.parseInt(colb)));
	}

	@Override
	protected void actionCreate() {
		saveChanges(true);
		finalizeCreation();
	}
	
	private void actionColorPicker() {
		Color pick = JColorChooser.showDialog(host.getDialog(), "Choose cell color", source.getColor());
		
		if( pick == null )
		return;
		
		saveChanges(false);
		this.source.setColor(pick);
		update();
	}
}
