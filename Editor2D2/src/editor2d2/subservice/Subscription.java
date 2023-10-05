package editor2d2.subservice;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Subscriptions are a way of referencing values that may not 
 * be available yet between objects. Each Subscription 
 * consists of Subscribers and a Vendor. The Vendor vends to 
 * the Subscribers that are subscribed to the Subscriptions. 
 * 
 * When a Subscriber needs a value that may not exist yet, it 
 * can try to get the value through a Subscription by calling 
 * its get-method. This method will return the Vendor that 
 * possesses the value which can be returned via a getter. If 
 * the Vendor is not available - making the value unavailable 
 * as well - the Subscriber will subscribe to the Subscription 
 * using a unique subscriber ID. Once the Vendor becomes 
 * available it calls the update-method of the Subscription 
 * which will notify all Subscribers of the availability. The 
 * Vendor may continue to update the Subscribers when its 
 * value changes.
 * 
 * Updates will be handled by the onNotification-method of the 
 * Subscribers and they are always associated with an update 
 * handle. Subscribers can be unsubscribed from the 
 * Subscription using the unsubscribe-method given their 
 * subscriber ID.
 * 
 * Subscriptions are useful when dealing with GUI-components as 
 * there are often issues with the order in which the 
 * components are instantiated.
 * 
 * This class implements a ConcurrentHashMap to keep track of 
 * the Subscribers making it thread-safe.
 * 
 * @author User
 *
 */
public class Subscription {

	/**
	 * Subscription ID that will be used by the Subscribers to 
	 * differentiate between Subscriptions.
	 */
	private final String handle;
	
	/**
	 * Reference to the Vendor that previously updated this handle. 
	 * If the value is NULL, no Vendor has yet updated this 
	 * Subscription and, thus, no value is available.
	 */
	private Vendor vendor;
	
	/**
	 * ConcurrentHashMap of the Subscribers currently subscribed 
	 * to this Subscription.
	 */
	private final Map<String, Subscriber> subscribers;
	
	
	/**
	 * Constructs a Subscription instance and assigns it a given 
	 * handle that will be used by the Subscribers to identify it.
	 *  
	 * @param handle Subscription ID.
	 */
	public Subscription(String handle) {
		this.handle = handle;
		this.vendor = null;
		this.subscribers = new ConcurrentHashMap<String, Subscriber>();
	}
	
	/**
	 * Updates all the Subscribers that a new value is available 
	 * from the Vendor. The Vendor holding the value should call 
	 * this method and pass a reference to itself to the 
	 * Subscribers. The update will be delivered by calling the 
	 * onNotification-method of the Subscriber.
	 * 
	 * @param vendor Reference to the Vendor calling this method.
	 */
	public void update(Vendor vendor) {
		this.vendor = vendor;
		
		for( Map.Entry<String, Subscriber> en : this.subscribers.entrySet() )
		en.getValue().onNotification(this.handle, this.vendor);
	}
	
	/**
	 * Subscribes a given Subscriber to this Subscription. The 
	 * Subscriber will have a given subscriber ID that can later 
	 * be used to unsubscribe the Subscriber later.
	 * 
	 * @param subId Subscriber ID used to identify the Subscriber.
	 * @param subscriber Reference to the Subscriber that is to 
	 * be subscribed to the Subscription.
	 */
	public void subscribe(String subId, Subscriber subscriber) {
		this.subscribers.put(subId, subscriber);
	}
	
	/**
	 * Unsubscribes a Subscriber with a given subscriber ID from 
	 * this Subscription.
	 * 
	 * @param subId ID of the Subscriber to unsubscribe from 
	 * this Subscription.
	 */
	public void unsubscribe(String subId) {
		this.subscribers.remove(subId);
	}
	
	/**
	 * Unsubscribes all Subscribers currently subscribed to 
	 * this Subscription.
	 */
	public void unsubscribeAll() {
		this.subscribers.clear();
	}

	/**
	 * Returns a reference to the vendor that last updated this 
	 * Subscription. If this Subscription hasn't been updated yet 
	 * the Subscriber calling this method will be subscribed to 
	 * this Subscription and updated once the Vendor becomes 
	 * available. The Subscriber is coupled with a unique 
	 * subscriber ID upon subscribing. If the Vendor is available, 
	 * no subscription occurs.
	 * 
	 * @param subId Subscriber ID that will be used to subscribe 
	 * the Subscriber to this Subscription when no Vendor is 
	 * available.
	 * @param subscriber Reference to the Subscriber requesting 
	 * the Vendor's value (the caller).
	 * 
	 * @return Returns a reference to the Vendor holding the 
	 * value or NULL if the Vendor is not available yet.
	 */
	public Vendor get(String subId, Subscriber subscriber) {
		if( this.vendor != null )
		return this.vendor;
		
		subscribe(subId, subscriber);
		return null;
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a reference to the Vendor that last updated this 
	 * Subscription last. <b>Notice: </b>this method does NOT 
	 * subscribe the caller to this Subscription.
	 * 
	 * @return Returns a reference to the Vendor that last 
	 * updated this Subscription.
	 */
	public Vendor getVendor() {
		return this.vendor;
	}
	
	/**
	 * Returns the subscription ID that will be used by the 
	 * Subscribers to identify this Subscription upon 
	 * update.
	 * 
	 * @return Returns the subscription ID.
	 */
	public String getHandle() {
		return this.handle;
	}
	
	/**
	 * Returns an ArrayList of all the Subscribers currently 
	 * subscribed to this Subscription.
	 * 
	 * @return Returns an ArrayList of all Subscribers.
	 */
	public ArrayList<Subscriber> getSubscribers() {
		ArrayList<Subscriber> subs = new ArrayList<Subscriber>();
		
		for( Map.Entry<String, Subscriber> en : this.subscribers.entrySet() )
		subs.add(en.getValue());
		
		return subs;
	}
}
