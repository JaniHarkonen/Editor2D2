package editor2d2.model.project.layers;

import java.util.ArrayList;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.scene.placeables.Instance;

public class ObjectArray implements Gridable {

		// ArrayList of objects wrapped by this ObjectArray
	public ArrayList<Instance> objects;
	
	
	public ObjectArray() {
		this.objects = new ArrayList<Instance>();
	}
	
	
		// GETTERS / SETTERS
	
	public Instance get(int index) {
		return this.objects.get(index);
	}
	
	public void add(Instance e) {
		this.objects.add(e);
	}
}
