package editor2d2.model.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import editor2d2.subservice.Subscriber;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

public class HotkeyListener extends SubscriptionService implements KeyListener, Vendor {
	
	public static final String KEY_UPDATED_HANDLE = "key-updated";
	
		// List of keys being held in the order that they
		// were pressed in
	private ArrayList<Integer> heldKeys;
	
	
	public HotkeyListener() {
		this.heldKeys = new ArrayList<Integer>();
	}
	
	
		// Returns whether a given Subscription handle indicates
		// an keyboard update
	public static boolean didKeyUpdate(String handle) {
		return handle.equals(KEY_UPDATED_HANDLE);
	}
	
		// Returns whether a given key is being held
	public static boolean isKeyHeld(Vendor hotkeyListener, int key) {
		return ((HotkeyListener) hotkeyListener).isKeyHeld(key);
	}
	
		// Returns whether a sequence of keys are being
		// held
	public static boolean isSequenceHeld(Vendor hotkeyListener, int... keys) {
		return ((HotkeyListener) hotkeyListener).isSequenceHeld(keys);
	}

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
	
	
		// Returns whether a given key is being held
	public boolean isKeyHeld(int key) {
		for( int heldKey : this.heldKeys )
		if( heldKey == key )
		return true;
			
		return false;
	}
	
		// Returns whether a sequence of keys are being
		// held
		// THIS METHOD SHOULD BE PRIORITIZED OVER isKeyHeld
		// AS A SINGLE KEY CAN BE HELD IN MULTIPLE SEQUENCES
	public boolean isSequenceHeld(int... keys) {
		if( keys.length != this.heldKeys.size() )
		return false;
		
		for( int i = 0; i < this.heldKeys.size(); i++ )
		if( this.heldKeys.get(i) != keys[i] )
		return false;
		
		return true;
	}
}
