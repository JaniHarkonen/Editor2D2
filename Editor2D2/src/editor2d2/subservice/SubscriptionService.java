package editor2d2.subservice;

import java.util.HashMap;
import java.util.Map;

/**
 * SubscriptionService is a manager-type class whose instance 
 * keeps track of all the Subscriptions that Subscribers and 
 * Vendors can subscribe to and unsubscribe from. Each 
 * Subscription is coupled with a unique handle (subscription 
 * ID) that can be used to reference the Subscription when 
 * subscribing, unsubscribing or getting a reference to a 
 * Vendor of a Subscription. 
 * 
 * Essentially, the SubscriptionService is an API of sorts 
 * that interfaces with Subscriptions. The Subscriptions 
 * aren't meant to be direclty accessed by the Subscribers 
 * and Vendors themselves, thus there is much overlap between 
 * the methods found in the SubscriptionService and those in 
 * Subscriptions.
 * 
 * Vendors register to the Subscriptions via the register-
 * method, while Subscribers subscribe, unsubscribe or get 
 * Vendors via the subscribe-, unsubscribe- and get-methods. 
 * A registration is analogous to the update-method found in 
 * Subscription where all Subscribers will be notified of the 
 * new Vendor.
 * 
 * Each time a Subscriber subscribes to a Subscription or a 
 * Vendor registers a Subscription that doesn't exist, it 
 * will be created and stored with its unique handle.
 * 
 * This class implements a HashMap and, as a result, is NOT 
 * thread-safe. So far, no concurrency problems have risen, 
 * however, the class could easily be ported to use a 
 * ConcurrentHashMap given the similarities with HashMap.
 * 
 * @author User
 *
 */
public class SubscriptionService {

	/**
	 * HashMap of the Subscriptions handled by this 
	 * SubscriptionService instance. The Subscriptions are 
	 * coupled with their unique handles that can be used 
	 * to refer to them.
	 */
	private Map<String, Subscription> subscriptions;
	
	
	/**
	 * Constructs a SubscriptionService instance with 
	 * default settings, including an empty Subscription 
	 * HashMap.
	 */
	public SubscriptionService() {
		this.subscriptions = new HashMap<String, Subscription>();
	}
	
	/**
	 * Registers a Vendor to a Subscription with a given 
	 * handle. If no Subscription matching the handle exists, 
	 * a new Subscription will be created and the Vendor 
	 * will be registered to that.
	 * 
	 * All Subscribers subscribed to the Subscription will 
	 * be notified of the change in Vendor, or the change in 
	 * Vendor's state, via the update-method of Subscriber.
	 * 
	 * This method should in general only be called by 
	 * Vendors that are looking to register themselves with 
	 * a Subscription.
	 * 
	 * See Subscriber for more information about update-
	 * method.
	 * 
	 * @param handle Unique Subscription handle that 
	 * identifies the Subscription that the Vendor is to 
	 * be registered to.
	 * @param vendor Vendor that vends to the Subscription
	 *(should be a reference to the Vendor calling this 
	 *method).
	 */
	public void register(String handle, Vendor vendor) {
		Subscription subscription = this.subscriptions.get(handle);
		
			// No subscription with such handle -> create a new one
		if( subscription == null )
		{
			subscription = new Subscription(handle);
			this.subscriptions.put(handle, subscription);
		}
		
		subscription.update(vendor);
	}
	
	/**
	 * Subscribes a Subscriber to a Subscription of a given 
	 * handle. The Subscriber will be coupled with a unique 
	 * subscrpition ID that can be used to reference the 
	 * Subscriber later, for example when unsubscribing. 
	 * A reference to the Subscriber calling this method must 
	 * also be passed.
	 * 
	 * If no Subscription matching the handle exists, a new 
	 * Subscription will be created and the Subscriber will 
	 * be subscribed to that.
	 * 
	 * @param handle Unique handle that identifies the 
	 * Subscription the Subscriber is to be subscribed to.
	 * @param subId Unique ID that identifies the 
	 * Subscriber that is to be subscribed to the 
	 * Subscription.
	 * @param self Reference to the Subscriber that is to 
	 * be subscribed (should be a reference to the 
	 * Subscriber calling this method).
	 */
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
	
	/**
	 * Unsubscribes a Subscriber from a Subscription of a 
	 * given handle. The subscription ID of the Subscriber 
	 * is required to find the Subscriber within the 
	 * Subscription. If no Subscription matching the handle 
	 * exists, nothing happens.
	 * 
	 * @param handle Unique handle that identifies the 
	 * Subscription that the Subscriber should be 
	 * unsubscribed from.
	 * @param subId Unique ID that identifies the 
	 * Subscriber that is to be unsubscribed. 
	 */
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
	
	/**
	 * Returns a reference to the Vendor that last registered 
	 * itself with the Subscription of a given handle. The 
	 * Subscriber will also be subscribed to the Subscription 
	 * and, thus, the Subscriber's reference as well as a 
	 * unique subscription ID must be provided. If no 
	 * Subscription matching the handle exists, a new 
	 * Subscription will be created using the subscribe-
	 * method and NULL will be returned.
	 * 
	 * This method will not accept NULL Subscribers. If you 
	 * want to get a Vendor without subscribing, see the 
	 * getWithoutSubscription-method below.
	 * 
	 * See subscribe-method for more information on 
	 * subscriptions.
	 * 
	 * @param handle Unique handle that identifies the 
	 * Subscription whose Vendor's reference should be 
	 * returned.
	 * @param subId Unique ID that identifies the Subscriber 
	 * that should be subscribed to the Subscription if the 
	 * Vendor is not yet available.
	 * @param self Reference to the Subscriber that is to be 
	 * subscribed if the Vendor is not yet available (should 
	 * be a reference to the Subscriber calling this method).
	 * 
	 * @return Returns a reference to the Vendor that last 
	 * registered with the Subscription.
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
	
	/**
	 * Returns a reference to the Vendor that last registered 
	 * itself with the Subscription of a given handle. <b>
	 * Notice: </b>this method will NOT subscribe the caller 
	 * to the Subscription if the Vendor is not yet available. 
	 * Thus, no references to the Subscriber nor subscription 
	 * IDs are required. If the Vendor is not available, NULL 
	 * will be returned.
	 * 
	 * If no Subscription matching the handle exists, NULL 
	 * will also be returned.
	 * 
	 * @param handle Unique handle that identifies the 
	 * Subscription whose Vendor's reference should be 
	 * returned.
	 * 
	 * @return Returns a reference to the Vendor of the 
	 * Subscription or NULL if no Vendor is available.
	 */
	public Vendor getWithoutSubscription(String handle) {
		Subscription subscription = this.subscriptions.get(handle);
		
		if( subscription == null )
		return null;
		
		return subscription.getVendor();
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a reference to a Subscription of a given 
	 * handle or NULL if no such Subscription exists.
	 * 
	 * @param handle Unique handle that identifies the 
	 * Subscription that should be returned.
	 * 
	 * @return Returns a Subscription instance with the 
	 * given handle or NULL if no Subscription was found.
	 */
	public Subscription getSubscription(String handle) {
		return this.subscriptions.get(handle);
	}
}
