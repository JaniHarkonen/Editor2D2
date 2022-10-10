package editor2d2.subservice;

public interface Subscriber {

		// Called when the handle the subscriber is subscribed to is updated
	public void onNotification(Handle handle, Vendor vendor);
}
