package editor2d2.gui.body.layermgrpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor2d2.Application;
import editor2d2.common.grid.Grid;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.gui.body.scene.SceneView;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.components.requirements.Require;
import editor2d2.gui.components.requirements.RequireDoubleBetween;
import editor2d2.gui.components.requirements.RequireIntegerBetween;
import editor2d2.gui.components.requirements.RequireStringName;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class LayerPropertiesPane extends GUIComponent implements Vendor, Subscriber {
	
		// Reference to the source layer that the pane is representing
	protected Layer source;
	
		// Layer name text field
	protected CTextField txtName;
	
		// Layer opacity text field
	protected CTextField txtOpacity;
	
		// Layer grid cell width
	protected CTextField txtGridCellWidth;
	
		// Layer grid cell height
	protected CTextField txtGridCellHeight;
	
		// Whether a new Layer is being created
	protected boolean isNew;
	
		// Hosting LayerPropertiesPane
	protected LayerManagerPane host;
	
		// Visibility check box
	protected JCheckBox cbIsVisible;
	
	
	public LayerPropertiesPane(LayerManagerPane host, Layer source, boolean isNew) {
		this.source = source;
		this.host = host;
		
			// Layer name field
		this.txtName = new CTextField("Name: ", new RequireStringName());
		this.txtName.orientation = BoxLayout.PAGE_AXIS;
		
			// Layer grid cell width
		this.txtGridCellWidth = new CTextField("Width:", new RequireIntegerBetween(Require.MIN_ONLY, 1));
		this.txtGridCellWidth.orientation = GUIUtilities.BOX_PAGE_AXIS;
		
			// Layer grid cell width
		this.txtGridCellHeight = new CTextField("Height:", new RequireIntegerBetween(Require.MIN_ONLY, 1));
		this.txtGridCellHeight.orientation = GUIUtilities.BOX_PAGE_AXIS;
		
			// Layer opacity field
		this.txtOpacity = new CTextField("", new RequireDoubleBetween(0, 255));
		
			// Layer visibility checkbox
		this.cbIsVisible = new JCheckBox();
		
		this.isNew = isNew;
	}
	
	public LayerPropertiesPane() {
		this(null, null, true);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) { }
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createTitledPanel("Properties", GUIUtilities.BOX_PAGE_AXIS);
		
			// Layer name field
		this.txtName.setText(this.source.getName());
		
			// Type area
		JPanel containerType = GUIUtilities.createDefaultPanel();
		
			JLabel labTypeTitle = new JLabel("Type:");
			String[] typeChoices = FactoryService.getClassTypes();
			
			for( int i = 0; i < typeChoices.length; i++ )
			typeChoices[i] = FactoryService.getPlaceableClass(typeChoices[i]);
			
			GUIUtilities.convertFirstLetterUppercase(typeChoices);
			
			JComboBox<String> dmType = new JComboBox<String>(typeChoices);
			String selectedLayerType = FactoryService.getPlaceableClass(this.source.getReferencedAsset().getAssetClassName());
			dmType.setSelectedItem(GUIUtilities.getFirstLetterUppercase(selectedLayerType));
			
			if( this.isNew )
			{
				dmType.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						onLayerChange(e);
					}
				});
			}
			else
			dmType.setEnabled(false);
			
		containerType.add(labTypeTitle);
		containerType.add(dmType);
		
			// Grid cell dimensions area
		JPanel containerDimensions = GUIUtilities.createTitledPanel("Grid dimensions", GUIUtilities.BOX_LINE_AXIS);
		containerDimensions.add(this.txtGridCellWidth.render());
		this.txtGridCellWidth.setText(""+this.source.getObjectGrid().getCellWidth());
		
		addEmptySpace(containerDimensions, 2);
		
		containerDimensions.add(this.txtGridCellHeight.render());
		this.txtGridCellHeight.setText(""+this.source.getObjectGrid().getCellHeight());
		
		
			// Opacity area
		double opacity = this.source.getOpacity();
		JPanel containerOpacity = GUIUtilities.createTitledPanel("Visibility", GUIUtilities.BOX_PAGE_AXIS);
		
		final Layer otherSource = this.source;
		final LayerPropertiesPane self = this;
		
				// Opacity slider
			JSlider sldOpacity = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (100 * opacity));
			sldOpacity.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					
					Vendor v = Application.window.subscriptionService.get(Handles.SCENE_VIEW, "LayerPropertiesPane", self);
					SceneView sv = (SceneView) v;
					sv.update();
					
					double sldValue = source.getValue();
					otherSource.setOpacity(sldValue / 100d);
					
					if( !source.getValueIsAdjusting() )
					{
						txtOpacity.setText("" + Layer.opacityPercentageTo255(sldValue));
						update();
					}
				}
			});
			
				// Opacity field
			this.txtOpacity.setText(""+((int) Layer.opacityPercentageTo255(opacity)));
			
				// Visibility checkbox 
			JPanel containerVisibility = GUIUtilities.createDefaultPanel();
			
			this.cbIsVisible.setSelected(this.source.checkVisible());
				
			JLabel labVisibilityTitle = new JLabel("Visible:");
				
			containerVisibility.add(labVisibilityTitle);
			containerVisibility.add(this.cbIsVisible);
			
		containerOpacity.add(new JLabel("Opacity:"));
		containerOpacity.add(sldOpacity);
		containerOpacity.add(this.txtOpacity.render());
		addEmptySpace(containerOpacity);
		containerOpacity.add(containerVisibility);
		
		container.add(this.txtName.render());
		container.add(containerType);
		container.add(containerDimensions);
		container.add(containerOpacity);
		
			// Controls
		container.add(new ClickableButton("Apply", (e) -> { onApply(); }));
		
		return container;
	}
	
	
		// Called upon clicking "Apply", applies changes to the source layer
	protected void onApply() {
		RequireStringName rfName = (RequireStringName) this.txtName.getRequirementFilter();
		RequireIntegerBetween rfWidth = (RequireIntegerBetween) this.txtGridCellWidth.getRequirementFilter();
		RequireIntegerBetween rfHeight = (RequireIntegerBetween) this.txtGridCellHeight.getRequirementFilter();
		RequireDoubleBetween rfOpacity = (RequireDoubleBetween) this.txtOpacity.getRequirementFilter();
		
		if( GUIUtilities.checkMultiple
			(
				rfName.checkValid(),
				rfWidth.checkValid(),
				rfHeight.checkValid(),
				rfOpacity.checkValid()
			) > 0
		)
		return;
		
		String name = rfName.getValue();
		double opacity = Layer.opacity255ToPercentage(rfOpacity.getValue());
		int gridCellWidth = rfWidth.getValue(),
			gridCellHeight = rfHeight.getValue();
		
		Grid ogrid = this.source.getObjectGrid();
		int currentGridCellWidth = ogrid.getCellWidth(),
			currentGridCellHeight = ogrid.getCellHeight();
		
		boolean gridCellDimensionChanged = (currentGridCellWidth != gridCellWidth || currentGridCellHeight != gridCellHeight);
		
		if( gridCellDimensionChanged )
		{
			int result = JOptionPane.showConfirmDialog(
				  null,
				  "WARNING! Resizing the layer cells does not affect the cellular coordinates\n"
				+ "of already placed objects. This may result in the misplacement and even deletion\n"
				+ "of some objects.\n\nAre you sure you want to continue?",
				  "Grid cell resize confirmation",
				  JOptionPane.OK_CANCEL_OPTION
			);
			
			if( result != JOptionPane.OK_OPTION )
			return;
		}
		
		this.source.setName(name);
		this.source.setOpacity(opacity);
		
		if( gridCellDimensionChanged )
		this.source.resizeObjectGrid(gridCellWidth, gridCellHeight);
		
		if( this.isNew )
		this.source.getScene().addLayer(this.source);
		
		this.source.setVisible(this.cbIsVisible.isSelected());
		
		String handle = editor2d2.model.Handles.LAYER_VISIBILITY;
		Application.controller.subscriptionService.register(handle, this);
		
		this.host.closeProperties();
	}
	
		// Changes the type of the Layer
	@SuppressWarnings("unchecked")
	protected void onLayerChange(ActionEvent e) {
		int selectionIndex = ((JComboBox<String>) e.getSource()).getSelectedIndex();
		String assetClass = FactoryService.getClassTypes()[selectionIndex];
		
		Scene scene = this.source.getScene();
		this.source = FactoryService.getFactories(assetClass).createLayer(scene, 32, 32);
	}
	
		// Returns a reference to the source Layer
	public Layer getSourceLayer() {
		return this.source;
	}
}
