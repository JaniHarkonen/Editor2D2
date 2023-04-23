package editor2d2.model.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import editor2d2.subservice.Subscriber;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

/**
 * This class is used to detect hotkey presses in 
 * the application. An instance of this class is 
 * attached to the Window when Window's setup-
 * method is called. HotkeyListener implements 
 * the Swing's KeyListener-interface so that it 
 * can be attached to the master window. 
 * <br/><br/>
 * 
 * HotkeyListener is a type of SubscriptionService 
 * (extends SubscriptionService) that has a single 
 * Subscription whose Subscribers are updated each 
 * time a keyboard press is detected in the 
 * application. The class utilizes an ArrayList 
 * to keep track of the ASCII-codes of the keys 
 * that are being held down at any given moment.
 * <br/><br/>
 * 
 * When a GUI-component needs to receive updates 
 * regarding hotkey presses, it should implement 
 * the Subscriber-interface and subscribe to the 
 * HotkeyListener via the subscribe-method. When 
 * the Subscribers are notified, they can use the 
 * isSequenceHeld- and isKeyHeld-methods to 
 * determine whether a given key or sequence of 
 * keys is being held down at the time of the 
 * notification. The class also provides static 
 * versions of these methods for utility, so that 
 * the onNotification-method of the Subscribers 
 * can simply pass a reference to the Vendor that 
 * called the method wihout having to cast the 
 * Vendor to HotkeyListener and then check the 
 * keypresses.
 * <br/><br/>
 * 
 * See Window and Window's setup-method for more 
 * information on setting up the master window.
 * <br/><br/>
 * 
 * See SubscriptionService and Subscription for 
 * more information on passing data between 
 * Subscribers and Vendors.
 * 
 * @author User
 *
 */
public class HotkeyListener extends SubscriptionService implements KeyListener, Vendor {
	
	/**
	 * Unique handle of the Subscription through 
	 * which hotkey press updates are received by 
	 * the Subscribers.
	 */
	public static final String KEY_UPDATED_HANDLE = "key-updated";
	
	/**
	 * ArrayList holding the ASCII-codes of the 
	 * keyboard keys being held down at the 
	 * moment.
	 */
	private ArrayList<Integer> heldKeys;
	
	/**
	 * Constructs a HotkeyListener instance and 
	 * creates an empty ArrayList that will be 
	 * used to store the ASCII-codes of the 
	 * keys being held down at any point in time.
	 */
	public HotkeyListener() {
		this.heldKeys = new ArrayList<Integer>();
	}
	
	/**
	 * Utility method to determine whether a given 
	 * unique handle is the one that is used to 
	 * listen to hotkey presses.
	 * 
	 * @param handle Handle that will be compated to 
	 * the key update handle.
	 * 
	 * @return Returns whether a given handle is the 
	 * one used by HotkeyListener to notify 
	 * Subscribers of key presses.
	 */
	public static boolean didKeyUpdate(String handle) {
		return handle.equals(KEY_UPDATED_HANDLE);
	}
	
	/**
	 * Utility method that returns whether a given 
	 * key is being held down according to a given 
	 * HotkeyListener. The reference to the 
	 * HotkeyListener is accepted as a Vendor 
	 * because this method is to be used inside the 
	 * onNotification-method of a Subscriber.
	 * <br/><br/>
	 * 
	 * Using this method the Subscribers can avoid 
	 * having to cast the Vendor down to 
	 * HotkeyListener each time they want to access 
	 * its isKeyHeld-method.
	 * <br/><br/>
	 * 
	 * See isKeyHeld-method for the non-static 
	 * version of this method.
	 * 
	 * @param hotkeyListener Reference to the Vendor 
	 * that triggered the onNotification-method that 
	 * this method is called inside. Should be a 
	 * HotkeyListener.
	 * @param key ASCII-code of the key that is 
	 * to be checked.
	 * 
	 * @return Whether the key with the given 
	 * ASCII-code is being held down according to 
	 * the given HotkeyListener.
	 */
	public static boolean isKeyHeld(Vendor hotkeyListener, int key) {
		return ((HotkeyListener) hotkeyListener).isKeyHeld(key);
	}
	
	/**
	 * Utility method that returns whether a given 
	 * key sequence is being held down according to 
	 * a given HotkeyListener. The reference to the 
	 * HotkeyListener is accepted as a Vendor 
	 * because this method is to be used inside the 
	 * onNotification-method of a Subscriber.
	 * <br/><br/>
	 * 
	 * Using this method the Subscribers can avoid 
	 * having to cast the Vendor down to 
	 * HotkeyListener each time they want to access 
	 * its isSequenceHeld-method.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method does NOT consider 
	 * the order of the keys.
	 * <br/><br/>
	 * 
	 * See isSequenceHeld-method for the non-static 
	 * version of this method.
	 * 
	 * @param hotkeyListener Reference to the Vendor 
	 * that triggered the onNotification-method that 
	 * this method is called inside. Should be a 
	 * HotkeyListener.
	 * @param keys The ASCII-codes of the keys that 
	 * have to be held simultaneously. Accepts a 
	 * varying number of integer arguments.
	 * 
	 * @return Whether the keys with the given ASCII-
	 * codes are being held down simultaneously 
	 * according to the given HotkeyListener.
	 */
	public static boolean isSequenceHeld(Vendor hotkeyListener, int... keys) {
		return ((HotkeyListener) hotkeyListener).isSequenceHeld(keys);
	}

	/**
	 * Subscribes a Subscriber to listen for key 
	 * press updates vended by this HotkeyListener. 
	 * The Subscriber will be coupled with a unique 
	 * subscrpition ID that can be used to 
	 * reference the Subscriber later, for example 
	 * when unsubscribing. A reference to the 
	 * Subscriber calling this method must also be 
	 * passed.
	 * <br/><br/>
	 * 
	 * If no Subscription matching the handle 
	 * exists, a new Subscription will be created 
	 * and the Subscriber will be subscribed to 
	 * that.
	 * <br/><br/>
	 * 
	 * This method uses the subscribe-method of the 
	 * SubscriptionService. See SubscriptionService 
	 * for more information.
	 * 
	 * @param subId Unique ID that identifies the 
	 * Subscriber that is to be subscribed to the 
	 * Subscription.
	 * @param self Reference to the Subscriber that 
	 * is to be subscribed (should be a reference to 
	 * the Subscriber calling this method).
	 */
	public void subscribe(String subId, Subscriber self) {
		subscribe(KEY_UPDATED_HANDLE, subId, self);
	}
	
	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if( isKeyHeld(keyCode) )
		return;
		
		this.heldKeys.add(keyCode);
		register(KEY_UPDATED_HANDLE, this);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		for( int i = 0; i < this.heldKeys.size(); i++ )
		{
			if( this.heldKeys.get(i) != keyCode )
			continue;
			
			this.heldKeys.remove(i);
			break;
		}
		
		register(KEY_UPDATED_HANDLE, this);
	}
	
	
	/**
	 * Returns whether a given key is being held down.
	 * <br/><br/>
	 * 
	 * This method also has a static counterpart. See 
	 * isKeyHeld-method for the non-static version of 
	 * this method.
	 * 
	 * @param key ASCII-code of the key that is 
	 * to be checked.
	 * 
	 * @return Whether the key with the given 
	 * ASCII-code is being held down.
	 */
	public boolean isKeyHeld(int key) {
		for( int heldKey : this.heldKeys )
		if( heldKey == key )
		return true;
			
		return false;
	}
	
	/**
	 * Returns whether a given key sequence is being 
	 * held down simultaneously.
	 * <br/><br/>
	 * 
	 * This method also has a static counterpart. See 
	 * isKeyHeld-method for the non-static version of 
	 * this method.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method does NOT consider 
	 * the order of the keys.
	 * <br/><br/>
	 * 
	 * @param keys ASCII-code of the key that is 
	 * to be checked.
	 * 
	 * @return Whether the key with the given 
	 * ASCII-code is being held down.
	 */
	public boolean isSequenceHeld(int... keys) {
		if( keys.length != this.heldKeys.size() )
		return false;
		
		for( int i = 0; i < this.heldKeys.size(); i++ )
		if( this.heldKeys.get(i) != keys[i] )
		return false;
		
		return true;
	}
	
	/**
	 * Resets all key inputs by clearing the 
	 * ArrayList keeping track of the ASCII-codes
	 * of all the keys being held down at the 
	 * moment.
	 */
	public void resetKeys() {
		this.heldKeys = new ArrayList<Integer>();
	}
}
