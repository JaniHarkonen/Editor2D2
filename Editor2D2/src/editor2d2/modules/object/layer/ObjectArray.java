package editor2d2.modules.object.layer;

import java.util.ArrayList;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.scene.placeable.Drawable;
import editor2d2.model.project.scene.placeable.RenderContext;
import editor2d2.modules.object.placeable.Instance;

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
	
	
		// Adds an instance to the Instance list
	public void add(Instance inst) {
		this.objects.add(inst);
	}
	
		// Removes an instance from the Instance list given its index
	public void remove(int index) {
		this.objects.remove(index);
	}
	
		// Removes a given instance from the Instance list
	public void remove(Instance inst) {
		for( int i = 0; i < this.objects.size(); i++ )
		{
			if( this.objects.get(i) != inst )
			continue;
		
			this.objects.remove(i);
			return;
		}
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
	
	@Override
	public ArrayList<? extends Object> getCollection() {
		return this.objects;
	}
}
