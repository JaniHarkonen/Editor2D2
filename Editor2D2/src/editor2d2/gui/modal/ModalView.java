package editor2d2.gui.modal;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.components.requirements.RequireStringName;
import editor2d2.model.project.Asset;
import editor2d2.model.project.HasAsset;

/**
 * ModalViews are components displayed by the ModalWindow. 
 * ModalViews are typically used to render the properties 
 * of an Asset so that they can be edited. This is why the 
 * ModalView has a template variable that determines the 
 * type of the Asset whose properties the view will 
 * render.
 * <br/><br/>
 * 
 * The ModalView has a flag that determines whether it is 
 * rendering an Asset that is being edited or one that is 
 * being created (thus, it doesn't exist in the Project 
 * yet).
 * <br/><br/>
 * 
 * Each module MUST implement their own ModalView so the 
 * Assets of that class can be edited in the editor.
 * <br/><br/>
 * 
 * See ModalWindow for more information on displaying the 
 * modal.
 * 
 * @author User
 *
 * @param <A> Determines the type of Asset that this 
 * ModalView provides interface to.
 */
public abstract class ModalView<A extends Asset> extends GUIComponent implements HasAsset {
	
	/**
	 * The host ModalWindow that draws the ModalView.
	 */
	protected ModalWindow host;
	
	/**
	 * The source Asset whose properties are to be 
	 * rendered.
	 */
	protected A source;

	/**
	 * The CTextField instance used to enter the name 
	 * of the Asset.
	 * <br/><br/>
	 * 
	 * This field is provided and rendered by the 
	 * abstract class as an Asset of any type should 
	 * have a name.
	 */
	protected CTextField txtName;
	
	/**
	 * The CTextField instance used to enter the 
	 * unique identifier of the Asset.
	 * <br/><br/>
	 * 
	 * This field is provided and rendered by the 
	 * abstract class as an Asset of any type should 
	 * have a name.
	 */
	protected CTextField txtIdentifier;
	
	/**
	 * Whether the source Asset is being edited. 
	 * If the Asset is a new one being created, this 
	 * flag should be set to false.
	 * <br/><br/>
	 * 
	 * When this flag is set to true, the approval 
	 * option on the ModalView will be "Save". 
	 * Otherwise, the option will be "Create".
	 */
	protected boolean isEdited;
	
	/**
	 * Constructs a ModalView instance tied to a given 
	 * ModalWindow instance. Here the source Asset 
	 * can also be configured to "factory settings". 
	 * This means that the Asset in question is a new 
	 * one and, thus, has to be created using the 
	 * setFactorySettings-method.
	 * <br/><br/>
	 * 
	 * See the setFactorySettings-method for more 
	 * information on factory settings.
	 * 
	 * @param host Reference to the ModalWindow 
	 * instance that hosts this ModalView.
	 * @param useFactorySettings Whether the source 
	 * Asset should be set to the default settings.
	 */
	public ModalView(ModalWindow host, boolean useFactorySettings) {
		if( useFactorySettings == true )
		setFactorySettings();
		
		this.host = host;
		this.txtName = new CTextField("Name:", new RequireStringName());
		this.txtIdentifier = new CTextField("Identifier:", new RequireUnusedIdentifier(Application.controller.getActiveProject()));
		this.isEdited = false;
	}
	
	/**
	 * Constructs a ModalView instance tied to a given 
	 * ModalWindow instance. The source Asset will be 
	 * set to NULL.
	 * <br/><br/>
	 * 
	 * If you need to create a ModalView for a NEW 
	 * Asset, see the ModalView(ModalWindow, boolean)-
	 * constructor.
	 * 
	 * @param host Reference to the ModalWindow 
	 * instance that hosts this ModalView.
	 */
	public ModalView(ModalWindow host) {
		this(host, false);
	}

	/**
	 * A helper method that will take a JPanel and 
	 * create a default modal view where the JPanel 
	 * is contained. The JPanel should contain the 
	 * more specific details of the source Asset 
	 * while this method renders the input fields 
	 * for the name and the unique identifier of the 
	 * Asset. This method will also add the two 
	 * options at the bottom of the ModalView: 
	 * approval (save, create) and cancellation 
	 * (cancel).
	 * 
	 * @param wrappedElement Reference to the JPanel 
	 * that contains the specifics of the Asset 
	 * properties.
	 * 
	 * @return Reference to the JPanel of the 
	 * ModalView.
	 */
	protected JPanel createDefaultModalView(JPanel wrappedElement) {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Name field
		this.txtName.setText(this.source.getName());
		
			// Identifier field
		((RequireUnusedIdentifier) this.txtIdentifier.getRequirementFilter()).setAsset(this.source);
		this.txtIdentifier.setText(this.source.getIdentifier());
		
			// Control area (create, save, cancel)
		JPanel containerControls = new JPanel();
		
		if( this.isEdited )
		containerControls.add(new ClickableButton("Save", (e) -> { actionSave(); }));
		else
		containerControls.add(new ClickableButton("Create", (e) -> { actionCreate(); }));
		
		containerControls.add(new ClickableButton("Cancel", (e) -> { actionCancel(); }));
		
		container.add(this.txtName.render());
		container.add(this.txtIdentifier.render());
		container.add(wrappedElement);
		container.add(containerControls);
		
		return container;
	}
	
	/**
	 * This method should create a new instance of 
	 * source Asset class and provide it with the 
	 * default configuration. The instance must be 
	 * stored in the source-field.
	 */
	public abstract void setFactorySettings();
	
	/**
	 * Validates the inputs of all the input fields 
	 * in the ModalView and returns the number of 
	 * invalid inputs, issues. By default, only the 
	 * name and the unique identifier of the Asset 
	 * are validated, however, it is recommended to 
	 * override this method when other input fields 
	 * are present.
	 * <br/><br/>
	 * 
	 * When overriding this method it is crucial 
	 * that super.validateInputs is called first 
	 * and that the result is included in the 
	 * number of issues returned by the method as 
	 * the number of issues will determine whether 
	 * the changes to the Asset can be saved. The 
	 * changes are only saved when the number of 
	 * issues is 0.
	 * 
	 * @return The number of invalid  (issues) 
	 * inputs in the input fields of the ModalView.
	 */
	public int validateInputs() {
		return GUIUtilities.checkMultiple(
				this.txtName.getRequirementFilter().checkValid(),
				this.txtIdentifier.getRequirementFilter().checkValid()
		);
	}
	
	/**
	 * Configures the source Asset with the 
	 * settings input in the input fields of the 
	 * ModalView if the inputs are valid. If the 
	 * inputs are invalid the changes wont be 
	 * changed but only if the doChecks is set 
	 * to false. 
	 * <br/><br/>
	 * 
	 * It is recommended that the doChecks is 
	 * always set to true as otherwise the 
	 * changes will be saved to the source 
	 * regardless whether they're valid or not.
	 * <br/><br/>
	 * 
	 * This method should be overridden by each 
	 * module as it only saves the changes to 
	 * the name and the unique identifier of the 
	 * Asset by default.
	 * 
	 * @param doChecks Whether the validity of 
	 * inputs should be tested before storing 
	 * the changes to the source Asset.
	 * 
	 * @return Whether the changes were 
	 * successfully saved in the source Asset.
	 */
	public boolean saveChanges(boolean doChecks) {
		if( doChecks )
		{
			int issues = validateInputs();
			
			if( issues > 0 )
			{
				GUIUtilities.showErrorIfInvalid("Invalid input!", issues);
				return false;
			}
		}
		
		String name = (String) this.txtName.getRequirementFilter().getValue();
		String id = (String) this.txtIdentifier.getRequirementFilter().getValue();
		
		this.source.setName(name);
		this.source.setIdentifier(id);
		
		return true;
	}
	
	/**
	 * Called upon clicking the approval option 
	 * "Create". Saves the changes to the Asset 
	 * properties in the source Asset given that 
	 * the inputs in the ModalView are valid. If 
	 * the inputs are invalid, nothing happens.
	 * If the inputs are valid, the new Asset 
	 * will be added to the currently active 
	 * Project and the ModalView will be closed.
	 */
	protected void actionCreate() {
		if( !saveChanges(true) )
		return;
		
		finalizeCreation();
		
		this.host.closeModalWindow(this);
	}
	
	/**
	 * Called upon clicking the approval option 
	 * "Save". Saves the changes to the Asset 
	 * properties in the source Asset given that 
	 * the inputs in the ModalView are valid. If 
	 * the inputs are invalid, nothing happens.
	 * If the inputs are valid, the changes are 
	 * saved and the ModalView will be closed.
	 */
	protected void actionSave() {
		if( !saveChanges(true) )
		return;
		
		this.host.closeModalWindow(this);
	}
	
	/**
	 * Called upon clicking the cancellation 
	 * option "Cancel". Closes the ModalView 
	 * and disgards all changes.
	 */
	protected void actionCancel() {
		this.host.closeModalWindow(this);
	}
	
	/**
	 * A helper method that requests the Controller 
	 * to add the source Asset to the currently 
	 * active Project.
	 */
	protected void finalizeCreation() {
		Application.controller.addNewAsset(this.source);
	}
	
	// GETTERS AND SETTERS

	/**
	 * Returns a reference to the source Asset that 
	 * this ModalView provides interface to.
	 * 
	 * @return Reference to the source Asset.
	 */
	public A getAsset() {
		return this.source;
	}
	
	/**
	 * Sets the sourece Asset that this ModalView 
	 * provides interface to.
	 * 
	 * @param source Reference to the new source 
	 * Asset instance.
	 */
	public void setAsset(A source) {
		this.source = source;
	}
	
	/**
	 * Sets whether the source Asset is being 
	 * edited. When true, the approval option will 
	 * be "Save", and when false, the option will 
	 * be "Create".
	 * 
	 * @param isEdited Whether the source ASset is 
	 * being edited (true) or created (false).
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}
}
