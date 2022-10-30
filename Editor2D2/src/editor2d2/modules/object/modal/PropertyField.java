package editor2d2.modules.object.modal;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.modules.object.asset.ObjectProperty;
import editor2d2.modules.object.modal.requirements.RequirePropertyValueOrReference;
import editor2d2.modules.object.modal.requirements.RequireValidPropertyName;

public class PropertyField extends GUIComponent {
	
	private ObjectProperty source;
	
	private boolean isSelected;
	
	private boolean allowSelection;
	
	private CTextField txtPropName;
	
	private CTextField txtPropValue;
	
	private JCheckBox cbIsCompiled;
	
	
	public PropertyField(ObjectProperty source, boolean allowSelection) {
		this.source = source;
		this.isSelected = false;
		this.allowSelection = allowSelection;
		this.txtPropName = new CTextField("", new RequireValidPropertyName());
		this.txtPropValue = new CTextField("", new RequirePropertyValueOrReference());
		this.cbIsCompiled = new JCheckBox();
	}
	
	public PropertyField(ObjectProperty source) {
		this(source, true);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			// Selection checkbox
		if( allowSelection )
		{
			JCheckBox cbIsSelected = new JCheckBox();
			cbIsSelected.setSelected(this.isSelected);
			cbIsSelected.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					actionSelect();
				}
			});
		
			container.add(cbIsSelected);
		}
		
			// Property name field
		this.txtPropName.setText(this.source.name);
		
			// Value field
		this.txtPropValue.setText(this.source.value);
		
			// Compilation checkbox
		this.cbIsCompiled.setSelected(this.source.isCompiled);
		
		container.add(this.txtPropName.render());
		container.add(this.txtPropValue.render());
		container.add(this.cbIsCompiled);
		
		return container;
	}
	
	
	private void actionSelect() {
		this.isSelected = !this.isSelected;
	}
	
	
	public boolean getSelected() {
		return this.isSelected;
	}
	
	public String getName() {
		return this.txtPropName.getText();
	}
	
	public CTextField getNameField() {
		return this.txtPropName;
	}
	
	public String getValue() {
		return this.txtPropValue.getText();
	}
	
	public CTextField getValueField() {
		return this.txtPropValue;
	}
	
	public boolean checkCompiled() {
		return this.cbIsCompiled.isSelected();
	}
	
	public ObjectProperty getSource() {
		return this.source;
	}
}