package tech.stoptalking.crop.drawable.fg;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by tdbes on 14.09.2017.
 */

public interface FGDrawable {

    RectF init(float viewWidth, float viewHeight);
    void onDraw(Canvas canvas, RectF bounds);
}
