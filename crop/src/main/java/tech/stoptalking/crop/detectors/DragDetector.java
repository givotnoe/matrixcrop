package tech.stoptalking.crop.detectors;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * Created by tdbes on 12.09.2017.
 */

public class DragDetector {

    private int         activePointer = INVALID_POINTER_ID;
    private float       lastX, lastY;

    private Listener    listener;

    public DragDetector (Listener listener) {
        this.listener = listener;
    }

    public boolean onTouchEvent (MotionEvent ev) {

        //drag gesture
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {

            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                lastX = x;
                lastY = y;
                // Save the ID of this pointer (for dragging)
                activePointer = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, activePointer);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - lastX;
                final float dy = y - lastY;

                listener.onDrag(dx, dy);

                // Remember this touch position for the next move event
                lastX = x;
                lastY = y;

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                activePointer = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == activePointer) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastX = MotionEventCompat.getX(ev, newPointerIndex);
                    lastY = MotionEventCompat.getY(ev, newPointerIndex);
                    activePointer = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public interface Listener {
        void onDrag(float dx, float dy);
    }
}
