package editor2d2.gui;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Nearly all GUI-components should extend GUIComponent 
 * with the exception of certain components that must 
 * extend a Swing-component. This class uses Swing-
 * components to render the GUI-component.
 * 
 * GUIComponent functions as a wrapper for a JPanel 
 * that will be used to represent the GUI-component and 
 * its contents. By default, the JPanel uses a BoxLayout-
 * layout manager with BoxLayout.PAGE_AXIS orientation, 
 * however, this can be changed upon construction.
 * 
 * Classes extending GUIComponent must implement their 
 * own draw-method, which constructs the GUI-component 
 * and returns a container JPanel. This JPanel will be 
 * drawn onto the element-JPanel that represents the 
 * GUI-component. In general, all state information 
 * used by the component should be stored in its fields 
 * as calling draw will always produce a new JPanel and 
 * all the contents of the previous one - including 
 * previously created instances - will be lost and dealt 
 * by the JVM carbage collector (hopefully :/).
 * 
 * When a component needs to render a GUI-component it 
 * simply has to instantiate the GUI-component and call 
 * its render-method which will return the 
 * element-JPanel.
 * 
 * If the state of a GUIComponent changes it can use the 
 * update-method to call the render-method itself. For 
 * example, clicking a button on a GUI-component may 
 * lead to more input fields popping up inside the 
 * component. In this case, update would be called upon 
 * button click. This way, the parent component (the one 
 * rendering the component in the first place) does not 
 * need to be updated or called upon.
 * 
 * @author User
 *
 */
public abstract class GUIComponent {

	/**
	 * JPanel that functions as the final container for 
	 * the contents of the GUI-component. This is the 
	 * JPanel that will be returned to the parent 
	 * component for rendering.
	 */
	protected JPanel element;
	
	/**
	 * The class is abstract so it cannot be 
	 * instantiated, however, the instances of classes 
	 * extending GUIComponent will call this upon 
	 * construction to create the element-JPanel and 
	 * set its layout to the default one, BoxLayout 
	 * with BoxLayout.PAGE_AXIS orientation.
	 */
	public GUIComponent() {
		this.element = new JPanel();
		this.element.setLayout(new BoxLayout(this.element, BoxLayout.PAGE_AXIS));
	}
	
	/**
	 * The class is abstract so it cannot be 
	 * instantiated, however, the instances of classes 
	 * extending GUIComponent will call this upon 
	 * construction to create the element-JPanel. The 
	 * JPanel will have a given LayoutManager. <b>
	 * Notice: </b>the LayoutManager can only be set 
	 * here and it cannot be changed. The LayoutManager 
	 * will also ONLY apply to the outmost JPanel (the
	 * element-JPanel).
	 * 
	 * @param layoutManager The LayoutManager that the 
	 * element-JPanel should use.
	 */
	public GUIComponent(LayoutManager layoutManager) {
		this.element = new JPanel();
		this.element.setLayout(layoutManager);
	}
	
	/**
	 * Creates empty space using a given number of empty  
	 * JLabels containing only a " "-string. The JLabels 
	 * will be placed onto a given JPanel.
	 * 
	 * @param jpanel JPanel to add the empty space to.
	 * @param n Amount of empty space, the number of 
	 * empty JLabels, to be added.
	 */
	public static void addEmptySpace(JPanel jpanel, int n) {
		for( int i = 0; i < n; i++ )
		jpanel.add(new JLabel(" "));
	}
	
	/**
	 * Creates a single empty space using a an empty 
	 * JLabel containing only a " "-string. The JLabel 
	 * will be placed onto a given JPanel.
	 * 
	 * @param jpanel JPanel to add the mpty space to.
	 */
	public static void addEmptySpace(JPanel jpanel) {
		addEmptySpace(jpanel, 1);
	}
	
	/**
	 * Clears the element-JPanel and draws the contents 
	 * of the GUI-component using the draw method. 
	 * Returns a reference to the element-JPanel once 
	 * the drawing is complete. 
	 * 
	 * @return A reference to the element-JPanel of the 
	 * GUIComponent containing the result of the 
	 * rendering.
	 */
	public final JPanel render() {
		element.removeAll();
		element.add(draw());
		element.revalidate();
		element.repaint();
		
		return element;
	}
	
	/**
	 * Re-renders the component using the render-method.
	 * This method has only been created to improve 
	 * code clarity.
	 * 
	 * See render-method for more information on the 
	 * rendering process.
	 */
	public void update() {
		render();
	}
	
	/**
	 * MUST BE OVERRIDDEN.
	 * 
	 * Called upon rendering the GUIComponent. This 
	 * method is responsible for the actual drawing of 
	 * the contents of the GUI-component. When the 
	 * component is rendered, this method is called and 
	 * its previous result is disgarded. The result 
	 * is then returned and added to the element-JPanel
	 * representing the component.
	 * 
	 * @return A container JPanel representing the 
	 * contents of the component that will be placed 
	 * onto the element-JPanel.
	 */
	protected abstract JPanel draw();
}
