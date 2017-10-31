package tech.stoptalking.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import tech.stoptalking.crop.config.ViewConfig;
import tech.stoptalking.crop.detectors.DragDetector;
import tech.stoptalking.crop.drawable.bg.BGDrawable;
import tech.stoptalking.crop.drawable.bg.CropCenterBGDrawable;
import tech.stoptalking.crop.drawable.fg.CircleFGDrawable;
import tech.stoptalking.crop.drawable.fg.FGDrawable;
import tech.stoptalking.crop.restriction.InsideRestriction;
import tech.stoptalking.crop.restriction.Restriction;

import static tech.stoptalking.crop.config.ViewConfig.BG_MOV_FG_STILL_MODE;
import static tech.stoptalking.crop.config.ViewConfig.BG_STILL_FG_MOV_MODE;
import static tech.stoptalking.crop.config.ViewConfig.BOUND_BG_MODE;
import static tech.stoptalking.crop.config.ViewConfig.BOUND_FG_MODE;
import static tech.stoptalking.crop.config.ViewConfig.BOUND_VIEW_MODE;


/**
 * Created by tdbes on 10.09.2017.
 */

public class CropImageView extends View {

    //modes
    private int                     moveMode;
    private int                     boundMode;

    //drawables
    private BGDrawable              background;
    private FGDrawable              foreground;

    //image
    private int                     viewWidth;
    private int                     viewHeight;

    //mediators
    private RectF fgMediatorSrc, fgMediatorDst, bgMediatorSrc, bgMediatorDst;
    private RectF viewBounds; //used in restriction

    //matrix - all scale/translation actions are reflected in the matrix
    private Matrix matrix;

    //detectors
    private ScaleGestureDetector scaleDetector;
    private DragDetector            dragDetector;

    //restriction
    private Restriction             restriction;

    //scaling
    private float                   scaleFactor, lastScaleFactor, realScaleFactor;

    private float                   minScaleFactor, maxScaleFactor;
    private static final float      MIN_SCALE_FACTOR_RELATIVE = 1,
                                    MAX_SCALE_FACTOR_RELATIVE = 3;

    public CropImageView(Context context) {
        super (context);

        init();
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super (context, attrs);

        init();
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);

        init();
    }

    //init section

    private void init () {

        //default config
        ViewConfig config =
                ViewConfig
                        .builder()
                        .moveMode(BG_STILL_FG_MOV_MODE)
                        .boundMode(BOUND_VIEW_MODE)
                        .restriction(new InsideRestriction())
                        .background(new CropCenterBGDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.profile_background_image)))
                        .foreground(new CircleFGDrawable(ContextCompat.getColor(getContext(), R.color.overlay_bg)))
                        .build();


        setConfig(config);

        //init matrix
        matrix = new Matrix();

        //init mediators
        fgMediatorSrc = new RectF();
        fgMediatorDst = new RectF();
        bgMediatorSrc = new RectF();
        bgMediatorDst = new RectF();

        //scale detector
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        //drag detector
        dragDetector = new DragDetector(new DragListener());
    }

    private void initInner() {

        if (viewHeight == 0 || viewWidth == 0) {
            return;
        }

        //init matrix
        matrix.reset();

        //init mediators
        if (foreground != null) {
            fgMediatorSrc = foreground.init(viewWidth, viewHeight);
            matrix.mapRect(fgMediatorDst, fgMediatorSrc);
        }

        if (background != null) {
            bgMediatorSrc = background.init(viewWidth, viewHeight);
            matrix.mapRect(bgMediatorDst, bgMediatorSrc);
        }

        //init view bound
        viewBounds = new RectF(0, 0, viewWidth, viewHeight);

        //init scale factor
        scaleFactor = 1;
        lastScaleFactor = 1;
        realScaleFactor = 1;

        minScaleFactor = MIN_SCALE_FACTOR_RELATIVE * scaleFactor;
        maxScaleFactor = MAX_SCALE_FACTOR_RELATIVE * scaleFactor;
    }

    //view section

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        //view height and width
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        initInner();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        // send event to scale detector
        scaleDetector.onTouchEvent(ev);

        //send event to drag detector
        dragDetector.onTouchEvent(ev);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (BG_STILL_FG_MOV_MODE == moveMode) {

            //apply matrix to fgMediatorDst
            matrix.mapRect(fgMediatorDst, fgMediatorSrc);

            //restrict fgMediatorDst positioning
            restrict(fgMediatorDst, matrix);
            matrix.mapRect(fgMediatorDst, fgMediatorSrc);

        } else if (BG_MOV_FG_STILL_MODE == moveMode) {

            //apply matrix to fgMediatorDst
            matrix.mapRect(bgMediatorDst, bgMediatorSrc);

            //restrict fgMediatorDst positioning
            restrict(bgMediatorDst, matrix);
            matrix.mapRect(bgMediatorDst, bgMediatorSrc);
        }

        //drawables
        if (background != null) {
            background.onDraw(canvas, bgMediatorDst);
        }

        if (foreground != null) {
            foreground.onDraw(canvas, fgMediatorDst);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (background != null) {
            Bitmap bmp = background.getBitmap();

            if (bmp != null) {
                bmp.recycle();
            }
        }
    }

    //additional

    public Bitmap crop () {
        return cropBitmap(bgMediatorDst, fgMediatorDst);
    }

    private Bitmap cropBitmap (RectF bitmapMediator, RectF cropMediator) {

        Bitmap output = Bitmap.createBitmap((int) cropMediator.width(), (int) cropMediator.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Matrix cropMatrix = new Matrix();
        cropMatrix.postTranslate(-cropMediator.left, -cropMediator.top);

        RectF dstCropMediator = new RectF();
        cropMatrix.mapRect(dstCropMediator, bitmapMediator);

        canvas.drawBitmap(background.getBitmap(), null, dstCropMediator, null);

        return output;
    }

    public void setConfig (ViewConfig config) {

        foreground = config.getForeground();
        background = config.getBackground();
        restriction = config.getRestriction();

        moveMode = config.getMoveMode();
        boundMode = config.getBoundMode();

        initInner();
    }

    private void restrict (RectF target, Matrix matrix) {

        switch (boundMode) {

            case BOUND_BG_MODE: {

                restriction.restrictPositioning(bgMediatorDst, target, matrix);

                break;
            }

            case BOUND_FG_MODE: {

                restriction.restrictPositioning(fgMediatorDst, target, matrix);

                break;
            }

            case BOUND_VIEW_MODE: {

                restriction.restrictPositioning(viewBounds, target, matrix);

                break;
            }
        }
    }

    //scale listener

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            //refresh scale factor
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor * detector.getScaleFactor(), maxScaleFactor));
            realScaleFactor = scaleFactor / lastScaleFactor;
            lastScaleFactor = scaleFactor;

            //refresh matrix
            matrix.postScale(realScaleFactor, realScaleFactor,  detector.getFocusX(), detector.getFocusY());

            invalidate();

            return true;
        }
    }

    //drag listener

    private class DragListener implements DragDetector.Listener {

        @Override
        public void onDrag(float dx, float dy) {

            matrix.postTranslate(dx, dy);

            invalidate();
        }
    }
}
