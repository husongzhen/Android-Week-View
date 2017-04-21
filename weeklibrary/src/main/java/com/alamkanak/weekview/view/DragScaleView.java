package com.alamkanak.weekview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.utils.EventTextUtils;


/**
 * 作者：husongzhen on 17/4/19 17:46
 * 邮箱：husongzhen@musikid.com
 */

public class DragScaleView extends View implements View.OnTouchListener {
    public static final int DRAG_VERTICAL = 1;
    public static final int DRAG_ALL = 0;
    public static final int DRAG_HORIZONTAL = 2;
    private final int minHeight = 200;
    protected int screenWidth;
    protected int screenHeight;
    protected int lastX;
    protected int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private int dragDirection;
    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;
    private int offset = 0;
    protected Paint paint = new Paint();
    private int mEventPadding = 8;
    private TextPaint mEventTextPaint;
    private int mEventTextSize = 12;
    private int mEventTextColor = Color.BLACK;
    private WeekViewEvent weekevent;
    private RectF rect;
    private OnDragupListener dragUpListener;
    private int startSum;
    private int endSum;
    private int pos;
    private int dragType;


    public void setDragType(int dragType) {
        this.dragType = dragType;
    }

    public void setDragUpListener(OnDragupListener l) {
        this.dragUpListener = l;
    }

    public DragScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        init(attrs);
    }

    public DragScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        init(attrs);
    }

//    public DragScaleView(Context context) {
//        super(context);
//        setOnTouchListener(this);
//        init();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
        drawText(canvas);
    }

    protected void init(AttributeSet attrs) {
        // Prepare event text size and color.
        Context context = getContext();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, com.alamkanak.weekview.R.styleable.WeekView, 0, 0);
        mEventTextSize = a.getDimensionPixelSize(com.alamkanak.weekview.R.styleable.WeekView_eventTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mEventTextSize, context.getResources().getDisplayMetrics()));
        mEventTextColor = a.getColor(com.alamkanak.weekview.R.styleable.WeekView_eventTextColor, mEventTextColor);
        mEventPadding = a.getDimensionPixelSize(com.alamkanak.weekview.R.styleable.WeekView_eventPadding, mEventPadding);

        initText();
        initScreenWH();
    }

    private void initScreenWH() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    private void initText() {
        mEventTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mEventTextPaint.setStyle(Paint.Style.FILL);
        mEventTextPaint.setColor(mEventTextColor);
        mEventTextPaint.setTextSize(mEventTextSize);
    }

    private void drawText(Canvas canvas) {
        if (weekevent == null || rect == null) return;
//        setBackgroundColor(event.getColor());
        drawEventTitle(canvas, rect.top, rect.left);
    }


    public void show(WeekViewEvent event, RectF eventRect, int pos) {
        this.pos = pos;
        setBackgroundColor(event.getColor());
        int top = (int) eventRect.top;
        int bottom = (int) eventRect.bottom;
        int left = (int) eventRect.left;
        int right = (int) eventRect.right;
        setEventTitle(event, eventRect);
        updateView(top, bottom, left, right);
        setVisibility(View.VISIBLE);
    }

    private void updateView(int top, int bottom, int left, int right) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.width = right - left;
        params.height = bottom - top;
        params.topMargin = top;
        params.leftMargin = left;
        setLayoutParams(params);
    }


    public void dimiss() {
        setVisibility(View.GONE);
    }


    private void setEventTitle(WeekViewEvent event, RectF rect) {
        this.weekevent = event;
        this.rect = rect;
        invalidate();
    }

    private void drawEventTitle(Canvas canvas, float originalTop, float originalLeft) {
        if (rect.right - rect.left - mEventPadding * 2 < 0) return;
        if (rect.bottom - rect.top - mEventPadding * 2 < 0) return;
        String bob = EventTextUtils.getEventTitle(weekevent);
        int availableHeight = (int) (rect.bottom - originalTop - mEventPadding * 2);
        int availableWidth = (int) (rect.right - originalLeft - mEventPadding * 2);

        // Get text dimensions.
        StaticLayout textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int lineHeight = textLayout.getHeight() / textLayout.getLineCount();

        if (availableHeight >= lineHeight) {
            // Calculate available number of line counts.
            int availableLineCount = availableHeight / lineHeight;
            do {
                // Ellipsize text to fit into event rect.
                textLayout = new StaticLayout(TextUtils.ellipsize(bob, mEventTextPaint, availableLineCount * availableWidth, TextUtils.TruncateAt.END), mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // Reduce line count.
                availableLineCount--;

                // Repeat until text is short enough.
            } while (textLayout.getHeight() > availableHeight);

            // Draw text.
            canvas.save();
            canvas.translate(mEventPadding, mEventPadding);
            textLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawRect(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4.0f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(offset, offset, getWidth() - offset, getHeight()
                - offset, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();
            lastY = (int) event.getRawY();
            lastX = (int) event.getRawX();
            dragDirection = getDirection(v, (int) event.getX(),
                    (int) event.getY());
            startSum = 0;
            endSum = 0;
        }
        // 处理拖动事件
        delDrag(v, event, action);
        invalidate();
        return false;
    }


    protected void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                switch (dragDirection) {
                    case LEFT: // 左边缘
                        left(v, dx);
                        break;
                    case RIGHT: // 右边缘
                        right(v, dx);
                        break;
                    case BOTTOM: // 下边缘
                        bottom(v, dy);
                        break;
                    case TOP: // 上边缘
                        top(v, dy);
                        break;
                    case CENTER: // 点击中心-->>移动
                        sumStart(dy);
                        sumEnd(dy);
                        center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下
                        left(v, dx);
                        bottom(v, dy);
                        break;
                    case LEFT_TOP: // 左上
                        left(v, dx);
                        top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        right(v, dx);
                        bottom(v, dy);
                        break;
                    case RIGHT_TOP: // 右上
                        right(v, dx);
                        top(v, dy);
                        break;
                }
                if (dragDirection != CENTER) {
                    v.layout(oriLeft, oriTop, oriRight, oriBottom);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                dragDirection = 0;

                if (dragUpListener == null) {
                    return;
                }
                dimiss();

                int endY = (int) event.getRawY();
                int endX = (int) event.getRawX();





                dragUpListener.onDragUpListener(this, pos, startSum, endSum);

                break;
        }
    }

    private void sumStart(int dy) {
        startSum += dy;
    }

    private void sumEnd(int dy) {
        endSum += dy;
    }


    private void center(View v, int dx, int dy) {

        if (isDragVertical()) {
            dx = 0;
        }
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        if (left < -offset) {
            left = -offset;
            right = left + v.getWidth();
        }
        if (right > screenWidth + offset) {
            right = screenWidth + offset;
            left = right - v.getWidth();
        }
        if (top < -offset) {
            top = -offset;
            bottom = top + v.getHeight();
        }
        if (bottom > screenHeight + offset) {
            bottom = screenHeight + offset;
            top = bottom - v.getHeight();
        }
        v.layout(left, top, right, bottom);
    }


    private void top(View v, int dy) {
        oriTop += dy;
        if (oriTop < -offset) {
            oriTop = -offset;
        }


        if (oriBottom - oriTop - 2 * offset < minHeight) {
            oriTop = oriBottom - 2 * offset - minHeight;
        }else{
            sumStart(dy);
        }
    }


    private void bottom(View v, int dy) {
        oriBottom += dy;
        if (oriBottom > screenHeight + offset) {
            oriBottom = screenHeight + offset;
        }
        if (oriBottom - oriTop - 2 * offset < minHeight) {
            oriBottom = minHeight + oriTop + 2 * offset;
        }else{
            sumEnd(dy);
        }
    }


    private void right(View v, int dx) {
        if (isDragVertical()) {
            return;
        }
        oriRight += dx;
        if (oriRight > screenWidth + offset) {
            oriRight = screenWidth + offset;
        }
        if (oriRight - oriLeft - 2 * offset < minHeight) {
            oriRight = oriLeft + 2 * offset + minHeight;
        }
    }


    private void left(View v, int dx) {
        if (isDragVertical()) {
            return;
        }
        oriLeft += dx;
        if (oriLeft < -offset) {
            oriLeft = -offset;
        }
        if (oriRight - oriLeft - 2 * offset < minHeight) {
            oriLeft = oriRight - 2 * offset - minHeight;
        }
    }

    private boolean isDragVertical() {
        return dragType == DragScaleView.DRAG_VERTICAL;
    }


    protected int getDirection(View v, int x, int y) {
        int left = v.getLeft();
        int right = v.getRight();
        int bottom = v.getBottom();
        int top = v.getTop();
        if (x < 40 && y < 40) {
            return LEFT_TOP;
        }
        if (y < 40 && right - left - x < 40) {
            return RIGHT_TOP;
        }
        if (x < 40 && bottom - top - y < 40) {
            return LEFT_BOTTOM;
        }
        if (right - left - x < 40 && bottom - top - y < 40) {
            return RIGHT_BOTTOM;
        }
        if (x < 40) {
            return LEFT;
        }
        if (y < 40) {
            return TOP;
        }
        if (right - left - x < 40) {
            return RIGHT;
        }
        if (bottom - top - y < 40) {
            return BOTTOM;
        }
        return CENTER;
    }


    public int getCutWidth() {
        return getWidth() - 2 * offset;
    }


    public int getCutHeight() {
        return getHeight() - 2 * offset;
    }


    public interface OnDragupListener {
        void onDragUpListener(View drag, int pos, int dirX, int dirY);
    }
}