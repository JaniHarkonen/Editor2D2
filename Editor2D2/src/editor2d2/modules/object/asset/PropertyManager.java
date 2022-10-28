package editor2d2.modules.object.asset;

import java.util.ArrayList;

/**
 * Manages the custom property fields of <b>EObject</b> and its
 * <b>Instances</b>. This class wraps an ArrayList consisting of
 * <b>ObjectProperty</b>-objects and provides methods to access
 * its contents; including adding, modification, removal and
 * copying of <b>ObjectProperties</b>.<br/><br/>
 * 
 * Instances of <b>PropertyManager</b> should be utilized by
 * <b>EObject</b> and <b>Instance</b> instead of implementing
 * their own ArrayList of <b>ObjectProperties</b> to avoid
 * code redundancy.
 * @author User
 *
 */
public class PropertyManager {
	
	/**
	 * Contains the custom property fields of an object.
	 */
	private ArrayList<ObjectProperty> properties;
	
	
	/**
	 * Constructs a <b>PropertyManager</b> instance with
	 * an empty property list.
	 */
	public PropertyManager() {
		this.properties = createNewProperties();
	}
	
	/**
	 * Constructs a <b>PropertyManager</b> instance using
	 * given <b>ObjectProperty</b> ArrayList as the property
	 * fields.
	 * @param properties ArrayList containing the initial
	 * property fields.
	 */
	public PropertyManager(ArrayList<ObjectProperty> properties) {
		this.properties = properties;
	}
	
	
	/**
	 * Copies the property fields of a given <b>PropertyManager</b>
	 * to a destination manager. The copies will be deep and created
	 * using <b>ObjectProperty's</b> copy-constructor. The copied
	 * fields will completely override the fields of the
	 * destination manager. A reference to the list of the copied
	 * fields will be returned.<br/><br/>
	 * 
	 * If the destination manager is null, the method simple
	 * returns the copied fields.
	 * @param src Source manager to copy from.
	 * @param dest Destination manager to copy to.
	 * @return ArrayList of the copies.
	 */
	public static ArrayList<ObjectProperty> copyManagerProperties(PropertyManager src, PropertyManager dest) {
		if( src == null )
		return null;
		
		ArrayList<ObjectProperty> copy = createNewProperties();
		
		for( ObjectProperty op : src.properties )
		copy.add(new ObjectProperty(op));
		
		if( dest != null )
		dest.properties = copy;
		
		return copy;
	}
	
	/**
	 * Copies the property fields of a given <b>PropertyManager</b>
	 * to an ArrayList and returns it.
	 * @param src Source manager to copy from.
	 * @return ArrayList of the copies.
	 */
	public static ArrayList<ObjectProperty> copyManagerProperties(PropertyManager src) {
		return copyManagerProperties(src, null);
	}
	
	private static ArrayList<ObjectProperty> createNewProperties() {
		return new ArrayList<ObjectProperty>();
	}
	
	
	/**
	 * Adds a property field, represented by an <b>ObjectProperty</b>
	 * instance, to the wrapped properties. 
	 * @param op Property field to add.
	 */
	public void addProperty(ObjectProperty op) {
		if( op == null )
		return;
		
		this.properties.add(op);
	}
	
	/**
	 * Replaces a given property field given its name with a new
	 * <b>ObjectProperty</b> instance.
	 * @param name Name of the property field to be replaced.
	 * @param newProp The replacement field.
	 */
	public void modifyProperty(String name, ObjectProperty newProp) {
		if( newProp == null )
		return;
		
		ObjectProperty op = getProperty(name);
		
		if( op == null )
		return;
		
		op.set(newProp);
	}
	
	/**
	 * Replaces a given property field given its index in the
	 * properties list with a new <b>ObjectProperty</b> instance.
	 * @param index Index of the field to be replaced.
	 * @param newProp THe replacement field.
	 */
	public void modifyProperty(int index, ObjectProperty newProp) {
		if( newProp == null )
		return;
		
		ObjectProperty op = getProperty(index);
		
		if( op == null )
		return;
		
		op.set(newProp);
	}
	
	/**
	 * Removes a property field given its name.
	 * @param name Name of the property field to be removed.
	 */
	public void removeProperty(String name) {
		if( name == null )
		return;
		
		int index = indexOf(name);
		
		if( index < 0 )
		return;

		this.properties.remove(index);
	}
	
	/**
	 * Removes a property field given its index in the list
	 * of properties.
	 * @param index Index of the property field to be removed.
	 */
	public void removeProperty(int index) {
		if( checkBounds(index) )
		this.properties.remove(index);
	}
	
	/**
	 * Removes all of the property fields by creating a new
	 * ArrayList of properties.
	 */
	public void removeAllProperties() {
		this.properties = createNewProperties();
	}
	
	/**
	 * Copies the properties of this <b>PropertyManager</b>
	 * instance to a given destination manager.
	 * @param dest Destination manager to copy to.
	 */
	public void copyProperties(PropertyManager dest) {
		copyManagerProperties(this, dest);
	}
	
	/**
	 * Copies the properties of this <b>PropertyManager</b>
	 * instance to an ArrayList and returns it.
	 * @return ArrayList of the copies.
	 */
	public ArrayList<ObjectProperty> copyProperties() {
		return copyManagerProperties(this);
	}
	
	
	/**
	 * Checks whether a given index is falls outside of 
	 * the bounds of the property ArrayList.
	 * @param index Index to check.
	 * @return Whether the index is out of bounds.
	 */
	private boolean checkBounds(int index) {
		return (index >= 0 && index < this.properties.size());
	}
	
	
	/******************** GETTERS & SETTERS **********************/
	
	/**
	 * Returns a reference to the property field at a given
	 * index in the properties list.
	 * @param index Index of the property to find.
	 * @return Reference to the property or null if the
	 * index was out of bounds.
	 */
	public ObjectProperty getProperty(int index) {
		if( checkBounds(index) )
		return this.properties.get(index);
		
		return null;
	}
	
	/**
	 * Returns a reference to the property field with a given
	 * name. <br/> <br/>
	 * 
	 * Only the first occurrence of the property field will be
	 * returned as multiple fields with the same name should
	 * not exist.
	 * @param name Name of the property field to find.
	 * @return Reference to the property or null if no such
	 * property exists.
	 */
	public ObjectProperty getProperty(String name) {
		if( name == null )
		return null;
		
		for( int i = 0; i < this.properties.size(); i++ )
		{
			ObjectProperty op = this.properties.get(i);
			if( op.name.equals(name) )
			return op;
		}
		
		return null;
	}
	
	/**
	 * Returns a reference to the list of properties wrapped by
	 * this <b>PropertiesManager</b>.
	 * @return Reference to the properties.
	 */
	public ArrayList<ObjectProperty> getAllProperties() {
		return this.properties;
	}
	
	/**
	 * Finds the index of a given property field in the
	 * properties list.
	 * @param op Property field whose index to find.
	 * @return The index of the property or -1 if no such
	 * property exists.
	 */
	public int indexOf(ObjectProperty op) {
		for( int i = 0; i < this.properties.size(); i++ )
		if( this.properties.get(i) == op )
		return i;
		
		 return -1;
	}
	
	/**
	 * Finds the index of a property field given its name in
	 * the properties list. <br/><br/>
	 * 
	 * Only the first occurrence of the property field will be
	 * returned as multiple fields with the same name should
	 * not exist. 
	 * @param name Name of the property whose index to find.
	 * @return The index of the property or -1 if no such
	 * property exists.
	 */
	public int indexOf(String name) {
		for( int i = 0; i < this.properties.size(); i++ )
		if( this.properties.get(i).name.equals(name) )
		return i;
		
		return -1;
	}
}
