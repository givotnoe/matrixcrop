package tech.stoptalking.crop.restriction;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Created by tdbes on 12.09.2017.
 */

public class AroundRestriction implements Restriction {

    @Override
    public void restrictPositioning(RectF bounds, RectF target, Matrix matrix) {

        if (target.left > bounds.left) {
            matrix.postTranslate(-target.left, 0);
        }

        if (target.top > bounds.top) {
            matrix.postTranslate(0, -target.top);
        }

        if (target.right < bounds.right) {
            matrix.postTranslate(bounds.right - target.right, 0);
        }

        if (target.bottom < bounds.bottom) {
            matrix.postTranslate(0, bounds.bottom - target.bottom);
        }
    }
}
