package org.hugoandrade.calendarviewlib.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.hugoandrade.calendarviewlib.R;

import java.util.ArrayList;
import java.util.List;

public class MultipleTriangleView extends View {

    private class TriangleAttr {

        private Paint mNotePaint;
        private Paint mBackgroundPaint;

        private Path mBackgroundPath;

        private Direction mDirection;

        private String mNote;
        private int mBackgroundColor;
    }

    private static final Direction DEFAULT_DIRECTION = Direction.TOP_LEFT;
    private static final int DEFAULT_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
    private static final String DEFAULT_NOTE_TEXT = "";

    private List<TriangleAttr> mTriangleAttr;

    private float mSeparatorWidth;
    private ViewDirection mViewDirection;

    public MultipleTriangleView(Context context) {
        this(context, null);
    }

    public MultipleTriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleTriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Direction mDirection;
        int mBackgroundColor;
        String mNote;
        int mNumberOfItems;

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultipleTriangleView);
            switch (a.getInt(R.styleable.MultipleTriangleView_mtr_direction, 0)) {
                case 0:
                    mDirection = Direction.TOP_LEFT;
                    break;
                case 1:
                    mDirection = Direction.TOP_RIGHT;
                    break;
                case 2:
                    mDirection = Direction.BOTTOM_LEFT;
                    break;
                case 3:
                default:
                    mDirection = Direction.BOTTOM_RIGHT;
            }
            switch (a.getInt(R.styleable.MultipleTriangleView_mtr_view_direction, 0)) {
                case 0:
                    mViewDirection = ViewDirection.HORIZONTAL;
                    break;
                case 1:
                    mViewDirection = ViewDirection.VERTICAL;
                    break;
                default:
                    mViewDirection = ViewDirection.VERTICAL;
            }
            mBackgroundColor = a.getColor(R.styleable.MultipleTriangleView_mtr_background_color, DEFAULT_BACKGROUND_COLOR);
            mNote = a.getString(R.styleable.MultipleTriangleView_mtr_note_text);
            mNote = mNote == null ? DEFAULT_NOTE_TEXT : mNote;

            mSeparatorWidth = a.getDimension(R.styleable.MultipleTriangleView_mtr_separator_width, 0);
            mNumberOfItems = a.getInteger(R.styleable.MultipleTriangleView_mtr_number_of_items, 1);
            a.recycle();
        } else {
            mDirection = DEFAULT_DIRECTION;
            mViewDirection = ViewDirection.VERTICAL;

            mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            mNote = DEFAULT_NOTE_TEXT;

            mSeparatorWidth = 0;
            mNumberOfItems = 1;
        }

        mTriangleAttr = new ArrayList<>();
        for (int i = 0 ; i < mNumberOfItems ; i++) {
            TriangleAttr t = new TriangleAttr();

            t.mDirection = mDirection;
            t.mBackgroundColor = mBackgroundColor;
            t.mNote = mNote;

            t.mBackgroundPaint = new Paint();
            t.mBackgroundPaint.setStyle(Paint.Style.FILL);
            t.mBackgroundPaint.setColor(t.mBackgroundColor);
            t.mBackgroundPaint.setAntiAlias(true);

            t.mNotePaint = new Paint();
            t.mNotePaint.setColor(getResources().getColor(R.color.note_color, null));
            t.mNotePaint.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "nanum_square_ac.otf"));

            mTriangleAttr.add(t);
        }

        mViewDirection = ViewDirection.VERTICAL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 0;
        int desiredHeight = 0;

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

    public int getNumberOfItems() {
        return mTriangleAttr.size();
    }

    /**
     * Set the color of the triangle.
     * @param color the color of the triangle.
     */
    public void setTriangleBackgroundColor(int color) {
        boolean somethingHasChanged = false;

        for (TriangleAttr t : mTriangleAttr) {
            somethingHasChanged = somethingHasChanged || setTriangleBackgroundColor(t, color);
        }

        if (somethingHasChanged)
            invalidate();
    }

    public boolean setTriangleBackgroundColor(int i, int color) {
        if (i >= mTriangleAttr.size())
            return false;

        boolean somethingHasChanged = setTriangleBackgroundColor(mTriangleAttr.get(i), color);

        if (somethingHasChanged)
            invalidate();

        return true;
    }

    private boolean setTriangleBackgroundColor(TriangleAttr t, int color) {
        if (t.mBackgroundColor != color) {
            t.mBackgroundColor = color;
            if (t.mBackgroundPaint != null) {
                t.mBackgroundPaint.setColor(color);
            }
            t.mBackgroundPath = null;
            return true;
        }
        return false;
    }

    /**
     * Set the note of the triangle.
     * @param note the note of the triangle.
     */
    public void setNote(String note) {
        boolean somethingHasChanged = false;
        for (TriangleAttr t : mTriangleAttr) {
            somethingHasChanged = somethingHasChanged || setNote(t, note);
        }

        if (somethingHasChanged)
            invalidate();
    }

    public boolean setNote(int i, String note) {
        if (i >= mTriangleAttr.size())
            return false;

        boolean somethingHasChanged = setNote(mTriangleAttr.get(i), note);

        if (somethingHasChanged)
            invalidate();

        return true;
    }

    private boolean setNote(TriangleAttr t, String note) {
        if (!t.mNote.equals(note)) {
            t.mNote = note;
            return true;
        }
        return false;
    }

    /**
     * Set the direction of the triangle.
     * @param direction the direction of the triangle.
     */
    public void setDirection(Direction direction) {
        for (TriangleAttr t : mTriangleAttr) {

            if (direction != t.mDirection) {
                t.mDirection = direction;
            }
        }
        invalidate();
    }

    //
    // View Overrides
    //

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTriangleAttr.size() < 1)
            return;

        float width = getWidth() - getPaddingEnd() - getPaddingStart();
        float height = getHeight() - getPaddingTop() - getPaddingBottom();

        float separatorWidthTotal = (mTriangleAttr.size() - 1) * mSeparatorWidth;


        if (mViewDirection == ViewDirection.VERTICAL) {
            if (separatorWidthTotal > width)
                return;

            float iheight = (height - separatorWidthTotal) / mTriangleAttr.size();

            float startX = getPaddingStart();
            float startY = getPaddingTop();

            for (TriangleAttr t : mTriangleAttr) {
                canvas.drawPath(getBackgroundPath(t, startX, startY, width, iheight), t.mBackgroundPaint);
                t.mNotePaint.setTextSize(iheight-5);
                canvas.drawText(t.mNote, startX, startY+iheight-mSeparatorWidth, t.mNotePaint);

                startY = startY + iheight + mSeparatorWidth;
            }
        }
        else {
            if (separatorWidthTotal > height)
                return;

            float iwidth = (width - separatorWidthTotal) / mTriangleAttr.size();

            float startX = getPaddingStart();
            float startY = getPaddingTop();

            for (TriangleAttr t : mTriangleAttr) {
                canvas.drawPath(getBackgroundPath(t, startX, startY, iwidth, height), t.mBackgroundPaint);

                startX = startX + iwidth + mSeparatorWidth;
            }
        }
    }

    private int max(int val1, int val2) {
        return val1 > val2 ? val1 : val2;
    }

    private float min(float val1, float val2) {
        return val1 < val2 ? val1 : val2;
    }

    private Path getBackgroundPath(TriangleAttr t,
                                   float initX,
                                   float initY,
                                   float width,
                                   float height) {
        if (t.mBackgroundPath == null) {
            t.mBackgroundPath = new Path();
            Point p1, p2, p3, p4;
            p1 = new Point((int) initX, (int) initY);
            p2 = new Point((int) initX, (int) (initY + height));
            p3 = new Point((int) (initX + width), (int) (initY + height));
            p4 = new Point((int) (initX + width), (int) initY);

            t.mBackgroundPath.moveTo(p1.x, p1.y);
            t.mBackgroundPath.lineTo(p2.x, p2.y);
            t.mBackgroundPath.lineTo(p3.x, p3.y);
            t.mBackgroundPath.lineTo(p4.x, p4.y);
        }
        return t.mBackgroundPath;
    }

    //
    // Direction
    //

    public enum Direction {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    public enum ViewDirection {
        HORIZONTAL,
        VERTICAL
    }
}
