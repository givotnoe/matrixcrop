package tech.stoptalking.crop.drawable.fg;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by tdbes on 14.09.2017.
 */

public class FullViewFGDrawable implements FGDrawable {

    @Override
    public RectF init(float viewWidth, float viewHeight) {
        return new RectF(0, 0, viewWidth, viewHeight);
    }

    @Override
    public void onDraw(Canvas canvas, RectF bounds) {

    }
}
