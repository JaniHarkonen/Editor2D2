package editor2d2.subservice;

/**
 * Each class looking to receive notifications from the 
 * SubscriptionService should implement this interface. 
 * Subscribers receive updates from the Vendors through 
 * the Subscription they are subscribed to. See 
 * Subscription and SubscriptionService for more.
 * 
 * @author User
 *
 */
public interface Subscriber {

	/**
	 * This method is called when the Subscription the 
	 * Subscriber is subscribed to is updated. The caller 
	 * is the Vendor updating the subscription and each 
	 * call is coupled with a handle that can be used to 
	 * distinguish between different Subscriptions when 
	 * there are multiple.
	 * 
	 * @param handle Identifier of the Subscription update 
	 * event.
	 * @param vendor Vendor object updating the Subscription.
	 */
	public void onNotification(String handle, Vendor vendor);
}
