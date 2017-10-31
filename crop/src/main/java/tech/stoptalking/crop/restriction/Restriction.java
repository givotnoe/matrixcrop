package tech.stoptalking.crop.restriction;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Created by tdbes on 12.09.2017.
 */

public interface Restriction {
    void restrictPositioning(RectF bounds, RectF target, Matrix matrix);
}
