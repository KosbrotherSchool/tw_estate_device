package com.kosbrother.realestate.fragment;

import com.kosbrother.realestate.MainActivity;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.FrameLayout;

//public class TouchableWrapper extends FrameLayout
//{
//	private boolean mMapIsTouched;
//	
//	public TouchableWrapper(Context context)
//	{
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev)
//	{
//		switch (ev.getAction())
//		{
//		case MotionEvent.ACTION_DOWN:
//			mMapIsTouched = true;
//			break;
//
//		case MotionEvent.ACTION_UP:
//			mMapIsTouched = false;
//			break;
//		}
//
//		return super.dispatchTouchEvent(ev);
//	}
//	
//	public boolean getIsMapTouched()
//	{
//		return mMapIsTouched;
//	}
//}

public  class TouchableWrapper extends FrameLayout {

	private long lastTouched = 0;
	private static final long SCROLL_TIME = 200L; // 200 Milliseconds, but you can adjust that to your liking
	private UpdateMapAfterUserInterection updateMapAfterUserInterection;
	private UpdateMapActionDown updateMapActionDown;

	public TouchableWrapper(Context context) {
		super(context);
		// Force the host activity to implement the UpdateMapAfterUserInterection Interface
		try {
			updateMapAfterUserInterection = (MainActivity) context;
			updateMapActionDown = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");
        }
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastTouched = SystemClock.uptimeMillis();
			updateMapActionDown.onUpdateMapActionDown();
			break;
		case MotionEvent.ACTION_UP:
			final long now = SystemClock.uptimeMillis();
			if (now - lastTouched > SCROLL_TIME) {
				// Update the map
				updateMapAfterUserInterection.onUpdateMapAfterUserInterection();
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	// Map Activity must implement this interface
    public interface UpdateMapAfterUserInterection {
        public void onUpdateMapAfterUserInterection();
    }
    
    public interface UpdateMapActionDown {
        public void onUpdateMapActionDown();
    }
}