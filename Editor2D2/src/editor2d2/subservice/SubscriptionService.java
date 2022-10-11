package editor2d2.subservice;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionService {

		// A mapping of handles to their subscriptions
	private Map<String, Subscription> subscriptions;
	
		// Singleton class, only instantiate once
	public SubscriptionService() {
		this.subscriptions = new HashMap<String, Subscription>();
	}
	
	
		// Registers a vendor to a handle
	public void register(String handle, Vendor vendor) {
		Subscription subscription = this.subscriptions.get(handle);
		
		if( subscription == null )
		{
			subscription = new Subscription(handle);
			this.subscriptions.put(handle, subscription);
		}
		
		subscription.update(vendor);
	}
	
		// Subscribes a subscriber to a given handle with a given subscriber ID
	public void subscribe(String handle, String subId, Subscriber self) {
		if( subId == null || handle == null )
		return;
		
		Subscription subscription = this.subscriptions.get(handle);
		
			// No subscription found, create it
		if( subscription == null )
		{
			subscription = new Subscription(handle);
			this.subscriptions.put(handle, subscription);
		}
		
		subscription.subscribe(subId, self);
	}
	
		// Unsubscribes a with a given ID subscriber from a given handle
	public void unsubscribe(String handle, String subId) {
		if( subId == null )
		return;
		
		Subscription subscription = this.subscriptions.get(handle);
		
			// No subscription found
		if( subscription == null )
		return;
		
		subscription.unsubscribe(subId);
	}
	
	/**
	 * Returns the vendor registered at a given handle or subscribes the
	 * caller to the handle, if the vendor hasn't already registered.
	 * 
	 * @param handle Handle whose vendor to resolve.
	 * @param subId ID used to identify the subscriber that is polling the handle.
	 * @param subscriber Reference to the subscribe polling the handle.
	 * 
	 * @return Returns a reference to the vendor or NULL, if the vendor hasn't
	 * registered yet.
	 */
	public Vendor get(String handle, String subId, Subscriber self) {
		if( handle == null || subId == null || self == null )
		return null;
		
		Subscription subscription = this.subscriptions.get(handle);
		subscribe(handle, subId, self);
		
			// No subscription found
		if( subscription == null )
		return null;
		
		return subscription.getVendor();
	}
	
		// Returns the vendor of a given handle WITHOUT subscribing to the handle
	public Vendor getWithoutSubscription(String handle) {
		Subscription subscription = this.subscriptions.get(handle);
		
		if( subscription == null )
		return null;
		
		return subscription.getVendor();
	}
	
		// Returns a reference to the subscription with a given handle
	public Subscription getSubscription(String handle) {
		return this.subscriptions.get(handle);
	}
}
