package editor2d2.gui.body;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import editor2d2.common.dragbox.DragBox;
import editor2d2.common.dragbox.DragBoxPoll;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.CameraBounds;

public class SceneView extends GUIComponent {
	
		// Reference to the Scene that this view will render
	private final Scene scene;
	
		// The JPanel that the scene will be rendered in
	private JPanel view;
	
		// Reference to the DragBox that will be used to drag the Scene view
	private DragBox sceneDragger;
	
	
	public SceneView(Scene scene) {
		this.scene = scene;
		this.view = createView();
		this.sceneDragger = new DragBox(0, 0, this.scene.getWidth(), this.scene.getHeight());
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(this.view);
		
		return container;
	}
	
		// Creates the JPanel that will render the Scene by creating an
		// anonymous class extending JPanel
	private JPanel createView() {
		final int	MB_LEFT = 1,
					MB_MIDDLE = 2,
					MB_RIGHT = 3;
		
		Camera cam = this.scene.getCamera();
		
		@SuppressWarnings("serial")
		JPanel container = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D gg = (Graphics2D) g;
				
					// Render the Scene
				cam.render(gg);
				
					// Render the highlighted placement grid cell
				/*g.setColor(new Color(255, 0, 0, 50));
				g.fillRect(selectionArea.x, selectionArea.y, selectionArea.width, selectionArea.height);*/
				
					// Render the Scene bounds
				float[] dash = { 6.0f, 6.0f };
				gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
				gg.setColor(Color.BLACK);
				
				double cam_z = cam.getZ();
				
				Rectangle2D.Double bounds_scene = new Rectangle2D.Double(
					cam.getOnScreenX(0), cam.getOnScreenY(0),
					scene.getWidth() * cam_z, scene.getHeight() * cam_z
				);
				
				gg.draw(bounds_scene);
				
					// Render the Camera bounds
				CameraBounds cbounds = cam.getBounds();
				
				gg.setColor(Color.RED);
				Rectangle2D.Double bounds_cam = new Rectangle2D.Double(
					cam.getOnScreenX(cbounds.left), cam.getOnScreenY(cbounds.top),
					cam.getOnScreenX(cbounds.right), cam.getOnScreenY(cbounds.bottom)
				);
					
				gg.draw(bounds_cam);
				
					// Render the placement grid of the layer that is currently active
				/*dash[0] = 1.0f;
				dash[1] = 1.0f;
				
				gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
				
				for( int i = 0; i < scene.getWidth(); i += scene.getLayers().get(0).getObjectGrid().getCellWidth() )
				gg.drawLine(i, 0, i, scene.getHeight());
				
				for( int i = 0; i < scene.getHeight(); i += scene.getLayers().get(0).getObjectGrid().getCellHeight() )
				gg.drawLine(0, i,  scene.getWidth(), i);
				*/
			}
		};
		
			// Register mouse clicks
		container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				int mb = e.getButton();
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int mb = e.getButton();
				
					// Left mouse button released
				if( mb == MB_LEFT )
				sceneDragger.stopDragging();
			}
		});
		
			// Register mouse movements
		container.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
					// Handle Scene dragging
				if( !sceneDragger.checkDragging() )
				sceneDragger.startDragging(e.getX(), e.getY(), 1);
				else
				{
					DragBoxPoll dgpoll = sceneDragger.poll(e.getX(), e.getY(), 1);
					cam.shift(dgpoll.x, dgpoll.y, 0);
					update();
				}
			}
		});
		
		return container;
	}
}
