package tech.stoptalking.crop.drawable.bg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by tdbes on 14.09.2017.
 */

public class CropCenterBGDrawable implements BGDrawable {

    private Bitmap bmp;

    public CropCenterBGDrawable(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Override
    public RectF init(float viewWidth, float viewHeight) {

        //res
        RectF res = new RectF();

        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        float wAspect = viewWidth / bmpWidth;
        float hAspect = viewHeight / bmpHeight;

        //get scale factor
        float scaleFactor = wAspect * bmpHeight >= viewHeight ? wAspect : hAspect;

        //offsets
        float wOffset = (bmpWidth * scaleFactor - viewWidth) / 2;
        float hOffset = (bmpHeight * scaleFactor - viewHeight) / 2;

        res.left = -wOffset;
        res.top = -hOffset;

        res.right = bmpWidth * scaleFactor - wOffset;
        res.bottom = bmpHeight * scaleFactor - hOffset;

        return res;
    }

    @Override
    public void onDraw(Canvas canvas, RectF bounds) {
        canvas.drawBitmap(bmp, null, bounds, null);
    }

    @Override
    public Bitmap getBitmap() {
        return bmp;
    }
}
