package editor2d2.model.subservice;

public interface Subscriber {

		// Called when the handle the subscriber is subscribed to is updated
	public void onNotification(String handle, Vendor vendor);
}
