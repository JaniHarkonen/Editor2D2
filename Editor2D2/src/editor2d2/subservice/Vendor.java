package editor2d2.subservice;

/**
 * Each class looking to vend to Subscriptions must implement 
 * this interface. Vendors are the objects that hold values 
 * required by the Subscribers. A Vendor must register to a 
 * Subscription using SubscriptionService at which point all 
 * Subscribers will be notified of the availability of the 
 * Vendor.
 * 
 * See SubscriptionService for more information on 
 * registering and subscribing to Subscriptions.
 * 
 * See Subscriber for more information on the Vendor 
 * counterpart that subscribes to Subscriptions vended by 
 * the Vendor.
 * 
 * @author User
 *
 */
public interface Vendor {

}
