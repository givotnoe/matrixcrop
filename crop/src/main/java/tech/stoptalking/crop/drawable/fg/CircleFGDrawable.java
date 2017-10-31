package tech.stoptalking.crop.drawable.fg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by tdbes on 14.09.2017.
 */

public class CircleFGDrawable implements FGDrawable {

    private Path clipPath;
    private Paint bgPaint;

    public CircleFGDrawable(int bgColor) {

        clipPath = new Path();

        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
    }

    @Override
    public RectF init(float viewWidth, float viewHeight) {

        RectF res = new RectF();

        res.left = 0;
        res.top = 0;

        if (viewWidth < viewHeight) {
            res.bottom = viewWidth / 3;
            res.right = viewWidth / 3;
        } else {
            res.bottom = viewHeight / 3;
            res.right = viewHeight / 3;
        }

        return res;
    }

    @Override
    public void onDraw(Canvas canvas, RectF bounds) {

        canvas.save();

        //clip
        clipPath.reset();
        clipPath.addCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, Path.Direction.CCW);
        clipPath.close();

        canvas.clipPath(clipPath, Region.Op.DIFFERENCE);


        //draw overlay
        canvas.drawPaint(bgPaint);

        canvas.restore();
    }
}
