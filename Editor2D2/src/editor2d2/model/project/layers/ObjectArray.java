package editor2d2.model.project.layers;

import java.util.ArrayList;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.scene.placeables.Drawable;
import editor2d2.model.project.scene.placeables.Instance;
import editor2d2.model.project.scene.placeables.RenderContext;

public class ObjectArray implements Gridable, Drawable  {

		// ArrayList of objects wrapped by this ObjectArray
	public ArrayList<Instance> objects;
	
	
	public ObjectArray() {
		this.objects = new ArrayList<Instance>();
	}
	
	
	@Override
	public void draw(RenderContext rctxt) {
		for( Instance inst : this.objects )
		inst.draw(rctxt);
	}
	
		// GETTERS / SETTERS
	
		// Returns a reference to an Instance stored at a given index
	public Instance get(int index) {
		return this.objects.get(index);
	}
	
		// Returns the list of all Instances int the Object Array
	public ArrayList<Instance> getAllInstances() {
		return this.objects;
	}
	
	
		// Adds an instance to the Instance list
	public void add(Instance e) {
		this.objects.add(e);
	}
}
