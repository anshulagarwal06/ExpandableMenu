package android.anshul.com.expendableMenu;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.anshul.com.fireview.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by Anshul on 10/07/15.
 */
public class ExpandableMenuView extends View {


    private static final int ANIMATION_DURATION = 100;
    private static final int DISTANCE_CONSTANT = 100;
    private int mCenterY;
    private int mCenterX;
    private Paint mCirclePaint;
    private int mSmallRadius;
    private int mBigRadius;
    private float mRadius;

    public static final int[] STATE_ACTIVE =
            {android.R.attr.state_enabled, android.R.attr.state_active};
    public static final int[] STATE_PRESSED =
            {android.R.attr.state_enabled, -android.R.attr.state_active,
                    android.R.attr.state_pressed};
    private Drawable mCenterDrawable;
    private Drawable mLeftDrawable;
    private Drawable mRightDrawable;
    private Drawable mTopDrawable;
    private Drawable mBottomDrawable;
    private int mDrawableHeight = 0, mDrawableWidth = 0;
    private boolean isPressed = false;
    private ValueAnimator mExpendAnimation, mCompressAnimation;
    private MenuListener mListener;
    private ValueAnimator mExtraExpendAnimation, mReverserExtraAnimation;
    private boolean isExtraAnimate;


    public ExpandableMenuView(Context context) {
        super(context);
        initView(context, null);
    }

    public ExpandableMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ExpandableMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public ExpandableMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.expendableMenu,
                    0, 0);
            try {
                if (typedArray.getResourceId(R.styleable.expendableMenu_left_drawable, 0) != 0) {
                    mLeftDrawable = getDrawable(typedArray.getResourceId(R.styleable.expendableMenu_left_drawable, 0));
                    updateDrawableSize(mLeftDrawable.getIntrinsicWidth(), mLeftDrawable.getIntrinsicHeight());
                }

                if (typedArray.getResourceId(R.styleable.expendableMenu_right_drawable, 0) != 0) {
                    mRightDrawable = getDrawable(typedArray.getResourceId(R.styleable.expendableMenu_right_drawable, 0));
                    updateDrawableSize(mLeftDrawable.getIntrinsicWidth(), mLeftDrawable.getIntrinsicHeight());
                }
                if (typedArray.getResourceId(R.styleable.expendableMenu_top_drawable, 0) != 0) {
                    mTopDrawable = getDrawable(typedArray.getResourceId(R.styleable.expendableMenu_top_drawable, 0));
                    updateDrawableSize(mLeftDrawable.getIntrinsicWidth(), mLeftDrawable.getIntrinsicHeight());
                }
                if (typedArray.getResourceId(R.styleable.expendableMenu_bottom_drawable, 0) != 0) {
                    mBottomDrawable = getDrawable(typedArray.getResourceId(R.styleable.expendableMenu_bottom_drawable, 0));
                    updateDrawableSize(mLeftDrawable.getIntrinsicWidth(), mLeftDrawable.getIntrinsicHeight());
                }

            } finally {
                typedArray.recycle();
                typedArray = null;
            }

        }
        setDrawableBound();

        mCirclePaint = new Paint();
        mCirclePaint.setColor(getResources().getColor(R.color.circle_color));
        mCirclePaint.setStyle(Paint.Style.FILL);
        mSmallRadius = getResources().getDimensionPixelSize(R.dimen.small_radius);
        mBigRadius = getResources().getDimensionPixelSize(R.dimen.big_radius);
        mRadius = mSmallRadius;

        mExpendAnimation = ValueAnimator.ofFloat(mSmallRadius, mBigRadius - 20);
        mExpendAnimation.setDuration(ANIMATION_DURATION);
        mExpendAnimation.setInterpolator(new AccelerateInterpolator());
        mExpendAnimation.addUpdateListener(mUpdateListener);

        mCompressAnimation = ValueAnimator.ofFloat(mBigRadius, mSmallRadius);
        mCompressAnimation.setDuration(ANIMATION_DURATION);
        mCompressAnimation.setInterpolator(new AccelerateInterpolator());
        mCompressAnimation.addUpdateListener(mUpdateListener);

        mExtraExpendAnimation = ValueAnimator.ofFloat(mBigRadius - 20, mBigRadius);
        mExtraExpendAnimation.setDuration(ANIMATION_DURATION);
        mExtraExpendAnimation.setInterpolator(new LinearInterpolator());
        mExtraExpendAnimation.addUpdateListener(mUpdateListener);

        mReverserExtraAnimation = ValueAnimator.ofFloat(mBigRadius, mBigRadius - 20);
        mReverserExtraAnimation.setDuration(ANIMATION_DURATION);
        mReverserExtraAnimation.setInterpolator(new LinearInterpolator());
        mReverserExtraAnimation.addUpdateListener(mUpdateListener);


    }

    private void setDrawableBound() {
        if (mLeftDrawable != null) {
            mLeftDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }

        if (mRightDrawable != null) {
            mRightDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (mBottomDrawable != null) {
            mBottomDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (mTopDrawable != null) {
            mTopDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
    }

    @TargetApi(22)
    private Drawable getDrawable(int resId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(resId, getContext().getTheme());
        } else {
            return getResources().getDrawable(resId);
        }
    }

    private void updateDrawableSize(int width, int height) {
        mDrawableWidth = mDrawableWidth == 0 ? width : Math.min(mDrawableWidth, width);
        mDrawableHeight = mDrawableHeight == 0 ? width : Math.min(mDrawableHeight, width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth;
        int desiredHeight;
        desiredWidth = getContext().getResources().getDimensionPixelSize(R.dimen.min_size);
        desiredHeight = getContext().getResources().getDimensionPixelSize(R.dimen.min_size);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint);
        if (!isPressed) {
            canvas.save();
            canvas.translate(mCenterX - mDrawableWidth / 2, mCenterY - mDrawableHeight / 2);
            mCenterDrawable.draw(canvas);
            canvas.restore();
        } else {
            //right

            if (mRightDrawable != null) {
                canvas.save();
                canvas.translate(mCenterX + mRadius - mDrawableWidth, mCenterY - mDrawableHeight / 2);
                mRightDrawable.draw(canvas);
                canvas.restore();
            }

            //left
            if (mLeftDrawable != null) {
                canvas.save();
                canvas.translate(mCenterX - mRadius, mCenterY - mDrawableHeight / 2);
                mLeftDrawable.draw(canvas);
                canvas.restore();
            }

            //Top
            if (mTopDrawable != null) {
                canvas.save();
                canvas.translate(mCenterX - mDrawableWidth / 2, mCenterY - mRadius/* mDrawableHeight / 2*/);
                mTopDrawable.draw(canvas);
                canvas.restore();
            }

            //bottom
            if (mBottomDrawable != null) {
                canvas.save();
                canvas.translate(mCenterX - mDrawableWidth / 2, mCenterY + mRadius - mDrawableHeight);
                mBottomDrawable.draw(canvas);
                canvas.restore();
            }

        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCenterDrawable = getResources().getDrawable(R.drawable.item_selector);
        mCenterDrawable.setBounds(0, 0, mCenterDrawable.getIntrinsicWidth(), mCenterDrawable.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isButtonPressed(event)) {
                    isPressed = true;
                    isExtraAnimate = false;
                    mExpendAnimation.start();
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:

                updateDrawableState(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isPressed) {
                    mRadius = mSmallRadius;
                    isPressed = false;
                    checkMenuPressed(event);
                    resetDrawableState();
                    mCompressAnimation.start();
                    return true;
                }

                return false;
            //   invalidate();
        }

        return super.onTouchEvent(event);
    }

    private void resetDrawableState() {
        if (mLeftDrawable != null) {
            mLeftDrawable.setState(STATE_ACTIVE);
        }
        if (mRightDrawable != null) {
            mRightDrawable.setState(STATE_ACTIVE);
        }
        if (mTopDrawable != null) {

            mTopDrawable.setState(STATE_ACTIVE);
        }
        if (mBottomDrawable != null) {
            mBottomDrawable.setState(STATE_ACTIVE);
        }

    }

    private void checkMenuPressed(MotionEvent event) {
        if (mListener == null) {
            return;
        }

        float x, y;
        x = event.getX();
        y = event.getY();

        //right
        if (mRightDrawable != null && x >= mCenterX + mBigRadius - mDrawableWidth * 2 && x <= mCenterX + mBigRadius) {
            if (y >= mCenterY - mDrawableHeight && y <= mCenterY + mDrawableHeight/* + DISTANCE_CONSTANT*/) {
                mListener.rightPressed();
            }
        } else if (mLeftDrawable != null && x <= mCenterX - mBigRadius + mDrawableWidth * 2 && x >= mCenterX - mBigRadius) {
            if (y >= mCenterY - mDrawableHeight && y <= mCenterY + mDrawableHeight) {
                mListener.leftPressed();
            }
        } else if (x >= mCenterX - mDrawableWidth / 2 && x <= mCenterX + mDrawableWidth / 2) {

            // top
            if (mTopDrawable != null && y >= mCenterY - mBigRadius && y <= mCenterY - mBigRadius + mDrawableHeight * 2) {
                mListener.topPressed();

            } else if (mBottomDrawable != null && y <= mCenterY + mBigRadius && y >= mCenterY + mBigRadius - mDrawableHeight * 2) {//bottom
                mListener.bottomPressed();

            }

        }

    }

    private void updateDrawableState(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        //right
        if (x >= mCenterX + mBigRadius - mDrawableWidth * 2 && x <= mCenterX + mBigRadius) {
            if (y >= mCenterY - mDrawableHeight && y <= mCenterY + mDrawableHeight/* + DISTANCE_CONSTANT*/) {
                resetDrawableState();
                mRightDrawable.setState(STATE_PRESSED);
                if (!isExtraAnimate) {
                    mExtraExpendAnimation.start();
                    isExtraAnimate = true;
                }
                invalidate();
            } else {
                resetDrawableState();
                startReverserExtraAnimation();

            }
        } else if (x <= mCenterX - mBigRadius + mDrawableWidth * 2 && x >= mCenterX - mBigRadius) {
            if (y >= mCenterY - mDrawableHeight && y <= mCenterY + mDrawableHeight) {
                resetDrawableState();
                mLeftDrawable.setState(STATE_PRESSED);
                if (!isExtraAnimate) {
                    mExtraExpendAnimation.start();
                    isExtraAnimate = true;
                }
                invalidate();
            } else {
                resetDrawableState();
                startReverserExtraAnimation();
            }
        } else if (x >= mCenterX - mDrawableWidth / 2 && x <= mCenterX + mDrawableWidth / 2) {

            // top
            if (y >= mCenterY - mBigRadius && y <= mCenterY - mBigRadius + mDrawableHeight * 2) {
                resetDrawableState();
                mTopDrawable.setState(STATE_PRESSED);
                if (!isExtraAnimate) {
                    mExtraExpendAnimation.start();
                    isExtraAnimate = true;
                }

            } else if (y <= mCenterY + mBigRadius && y >= mCenterY + mBigRadius - mDrawableHeight * 2) {//bottom
                resetDrawableState();
                mBottomDrawable.setState(STATE_PRESSED);
                if (!isExtraAnimate) {
                    mExtraExpendAnimation.start();
                    isExtraAnimate = true;
                }

            } else {
                resetDrawableState();
                startReverserExtraAnimation();
            }

        } else {
            resetDrawableState();
            startReverserExtraAnimation();
        }

    }

    private void startReverserExtraAnimation() {
        if (isExtraAnimate) {
            mReverserExtraAnimation.start();
            isExtraAnimate = false;
        }
    }

    private boolean isButtonPressed(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mCenterX - mRadius && x <= mCenterX + mRadius) {
            if (y >= mCenterY - mRadius && x <= mCenterY + mRadius) {
                return true;
            }
        }
        return false;
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mRadius = (float) valueAnimator.getAnimatedValue();
            invalidate();
        }
    };


    public void setOnMenuListener(MenuListener listener) {
        mListener = listener;
    }

    public interface MenuListener {

        void rightPressed();

        void leftPressed();

        void topPressed();

        void bottomPressed();

    }
}
