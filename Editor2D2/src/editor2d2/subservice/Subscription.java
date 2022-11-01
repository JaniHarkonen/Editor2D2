package editor2d2.subservice;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Subscription {

		// Subscription handle that will be used to subscribe to the vendor
	private final String handle;
	
		// Reference to the vendor that previously updated this handle
	private Vendor vendor;
	
		// Mapping of subscribers to their IDs
	private final Map<String, Subscriber> subscribers;
	
	
	public Subscription(String handle) {
		this.handle = handle;
		this.vendor = null;
		this.subscribers = new ConcurrentHashMap<String, Subscriber>();
	}
	
	
		// Updates the handle and the subscribers
	public void update(Vendor vendor) {
		this.vendor = vendor;
		
		for( Map.Entry<String, Subscriber> en : this.subscribers.entrySet() )
		en.getValue().onNotification(this.handle, this.vendor);
	}
	
		// Subscribes a given subscriber to this handle
	public void subscribe(String subId, Subscriber subscriber) {
		this.subscribers.put(subId, subscriber);
	}
	
		// Unsubscribes a given subscriber from this handle
	public void unsubscribe(String subId) {
		this.subscribers.remove(subId);
	}
	
		// Unsubscribes all subscribers from the handle
	public void unsubscribeAll() {
		this.subscribers.clear();
	}
	
		// Returns a reference to the vendor that last updated this handle
		// If the handle hasn't been updated, the caller will automatically
		// subscribe to the handle
	public Vendor get(String subId, Subscriber subscriber) {
		if( this.vendor != null )
		return this.vendor;
		
		subscribe(subId, subscriber);
		return null;
	}
	
		// Returns a reference to the vendor that last updated this handle
		// without subscribing for automatic updates
	public Vendor getVendor() {
		return this.vendor;
	}
	
		// Returns the handle
	public String getHandle() {
		return this.handle;
	}
	
		// Returns the list of subscribers
	public ArrayList<Subscriber> getSubscribers() {
		ArrayList<Subscriber> subs = new ArrayList<Subscriber>();
		
		for( Map.Entry<String, Subscriber> en : this.subscribers.entrySet() )
		subs.add(en.getValue());
		
		return subs;
	}
}
