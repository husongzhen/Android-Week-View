package com.alamkanak.weekview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.alamkanak.weekview.utils.EventTextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.media.CamcorderProfile.get;
import static com.alamkanak.weekview.WeekViewUtil.isSameDay;
import static com.alamkanak.weekview.WeekViewUtil.today;
import static java.util.Collections.max;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://alamkanak.github.io/
 */
public class WeekView extends View {


    private Paint nowLine;
    private boolean containsAllDayEvent;
    private boolean touchAble = true;
    private boolean showTimeArea = false;
    private boolean scrollToTimeLine = false;
    private int eventLeftColorWidth = 4;
    private int touchEditCicleR = 15;
    private int editCircleIconId = 0;
    private EventRect createEvent;
    private int toTime;
    private boolean pastEventEditAble = true;
    private List<EventRect> allDaySortEvents;
    private int editCicleDistance;
    private Path path;
    private int rectMargin = 2;
    private Paint timeColumnBgPaint;
    private Paint mHeaderLabelPaint;

    public void setPastEventEditAble(boolean pastEventEditAble) {
        this.pastEventEditAble = pastEventEditAble;
    }

    public void setTouchAble(boolean b) {
        this.touchAble = b;
    }

    private enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    private boolean zoomAble = false;

    @Deprecated
    public static final int LENGTH_SHORT = 1;
    @Deprecated
    public static final int LENGTH_LONG = 2;
    private final Context mContext;
    private Paint mTimeTextPaint;
    private float mTimeTextWidth;
    private float mTimeTextHeight;
    private Paint mHeaderTextPaint;
    private Paint mHeaderWeekTextPaint;
    private float mHeaderTextHeight;
    private float mHeaderHeight;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    // 保存偏移
    private PointF mCurrentOrigin = new PointF(0f, 0f);
    private Direction mCurrentScrollDirection = Direction.NONE;
    private Paint mHeaderBackgroundPaint;
    private Paint mHeaderLabelBackgroundPaint;
    private Paint mAllDayBackShaderPaint;
    //   一列的宽度
    private float mDayColumnWidth;
    private Paint mDayBackgroundPaint;
    private Paint mHourSeparatorPaint;
    private Paint mTimeLineSeparatorPaint;
    private float mHeaderMarginBottom;
    private Paint mTodayBackgroundPaint;
    private Paint mFutureBackgroundPaint;
    private Paint mPastBackgroundPaint;
    private Paint mFutureWeekendBackgroundPaint;
    private Paint mPastWeekendBackgroundPaint;
    private Paint mNowLinePaint;
    private Paint mTodayHeaderTextPaint;
    private Paint mWeekHeaderTextPaint;
    private Paint mEventBackgroundPaint;
    private Paint mEventBoarderPaint;
    private float mHeaderColumnWidth;
    private List<EventRect> mEventRects;
    private List<? extends WeekViewEvent> mPreviousPeriodEvents;
    private List<? extends WeekViewEvent> mCurrentPeriodEvents;
    private List<? extends WeekViewEvent> mNextPeriodEvents;
    private TextPaint mEventTextPaint;
    private TextPaint mCreateEventPaint;
    private Paint mHeaderColumnBackgroundPaint;
    private int mFetchedPeriod = -1; // the middle period the calendar has fetched.
    private boolean mRefreshEvents = false;
    private Direction mCurrentFlingDirection = Direction.NONE;
    private ScaleGestureDetector mScaleDetector;
    private boolean mIsZooming;
    private Calendar mFirstVisibleDay;
    private Calendar mLastVisibleDay;
    private boolean mShowFirstDayOfWeekFirst = false;
    private int mDefaultEventColor;
    private int mMinimumFlingVelocity = 0;
    private int mScaledTouchSlop = 0;
    // Attributes and their default values.
    private int mHourHeight = 50;
    private int mNewHourHeight = -1;
    private int mMinHourHeight = 0; //no minimum specified (will be dynamic, based on screen)
    private int mEffectiveMinHourHeight = mMinHourHeight; //compensates for the fact that you can't keep zooming out.
    private int mMaxHourHeight = 250;
    private int add_icon_rect = 100;
    private int mColumnGap = 0;
    private int mFirstDayOfWeek = Calendar.MONDAY;
    private int mTextSize = 12;
    private int mWeekTextSize = 16;
    private int mHeaderColumnPadding = 10;
    private int mHeaderColumnTextColor = Color.BLACK;
    private int mHeaderLabelColor = Color.BLACK;
    private int mNumberOfVisibleDays = 3;
    private int mHeaderRowPadding = 10;
    private int mHeaderRowBackgroundColor = Color.WHITE;
    private int mDayBackgroundColor = Color.rgb(245, 245, 245);
    private int mPastBackgroundColor = Color.rgb(227, 227, 227);
    private int mFutureBackgroundColor = Color.rgb(245, 245, 245);
    private int mPastWeekendBackgroundColor = 0;
    private int mFutureWeekendBackgroundColor = 0;
    private int mNowLineColor = Color.rgb(102, 102, 102);
    private int mNowLineThickness = 5;
    private int mHourSeparatorColor = Color.rgb(230, 230, 230);
    private int mTimeLineSeparatorColor = Color.rgb(230, 230, 230);
    private int mAllDaySeparatorColor = Color.rgb(230, 230, 230);
    private int mTodayBackgroundColor = Color.rgb(239, 247, 254);
    private int mHourSeparatorHeight = 1;
    private int mTodayHeaderTextColor = Color.rgb(39, 137, 228);
    private int mHeaderLabelStartColor = Color.rgb(39, 137, 228);
    private int mHeaderLabelEndColor = Color.rgb(39, 137, 228);
    private int mEventTextSize = 11;
    private int mCreateEventSize = 14;
    private int mEventTextColor = Color.BLACK;
    private int mEventBackColor = Color.WHITE;
    private int addIconReference = R.drawable.appwidget_add;
    private int mCreateEventColor = Color.BLACK;
    private int mTimeColumnBgColor = Color.WHITE;
    private int mEventFinishTextColor = Color.GRAY;
    private int mHeaderLabelBackColor = Color.WHITE;
    private int mEventPadding = 10;
    private int mAllDayEventItemPadding = 2;
    private int mHeaderColumnBackgroundColor = Color.WHITE;
    private boolean mIsFirstDraw = true;
    private boolean mAreDimensionsInvalid = true;
    @Deprecated
    private int mDayNameLength = LENGTH_LONG;
    private int mOverlappingEventGap = 0;
    private int mEventMarginVertical = 0;
    private float mXScrollingSpeed = 1f;
    private Calendar mScrollToDay = null;
    private double mScrollToHour = -1;
    private int mEventCornerRadius = 0;
    private boolean mShowDistinctWeekendColor = false;
    private boolean mShowNowLine = true;
    private boolean mShowDistinctPastFutureColor = false;
    private boolean mHorizontalFlingEnabled = true;
    private boolean mVerticalFlingEnabled = true;
    private int mAllDayEventHeight = 200;
    private int mAllDayEventMaxHeight = 200;
    private int mScrollDuration = 250;
    private int nowLineCircleR = 6;


    // Listeners.
    private EventClickListener mEventClickListener;
    private EventLongPressListener mEventLongPressListener;
    private WeekViewLoader mWeekViewLoader;
    private EmptyViewClickListener mEmptyViewClickListener;
    private EmptyViewLongPressListener mEmptyViewLongPressListener;
    private DateTimeInterpreter mDateTimeInterpreter;
    private ScrollListener mScrollListener;
    private OutCreateClickListener outCreateClickListener;
    private static final int ALLDAY_TYPE = 111;
    private static final int DAY_TYPE = 112;
    private static final int EDIT_TYPE = 113;
    private static final int EDIT_TOP_TYPE = 114;
    private static final int EDIT_BOTTOM_TYPE = 115;
    private static final int EDIT_OUT_VIEW_TYPE = 118;
    private static final int TIME_SET_TYPE = 117;
    private static final int TIME_EVENTS_TYPE = 116;
    private static final int EDIT_STATUE = 113;
    private static final int SHOW_STATUE = 114;
    //    private static final int SPEED_TOP_STATUE = 114;
    private static final int SPEED_NULL_STATUE = 116;
    //    private static final int SPEED_BOTTOM_STATUE = 115;
    private int speedType;
    private int flingType;
    private int viewStatue = SHOW_STATUE;
    private boolean isEditOutClick;
    private ADayItem touchAllDayItem;
    public int editPos;
    public String editId;
    private PointF editPoint = new PointF(0f, 0f);
    private int timeType = TIME_EVENTS_TYPE;
    //    private RectF topEditRect, bottomEditRect;
    private RectF topEditRect = new RectF();
    private RectF bottomEditRect = new RectF();
    private float topSum, bottomSum;
    private EventEditListener editListener;


    //allday
    private int allDayHeight = 100;
    private float allDayTopSum = 0;
    private AllDayMap dayMap = new AllDayMap();


    public void setEditListener(EventEditListener editListener) {
        this.editListener = editListener;
    }


    public void setOutCreateClickListener(OutCreateClickListener outCreateClickListener) {
        this.outCreateClickListener = outCreateClickListener;
    }

    public void setTimeSet() {
        this.timeType = TIME_SET_TYPE;
    }

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onDown(MotionEvent e) {
            goToNearestOrigin();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Check if view is zoomed.
            if (mIsZooming)
                return true;


            mScroller.forceFinished(true);
            switch (mCurrentScrollDirection) {
                case NONE: {
                    // Allow scrolling only in one direction.
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        if (distanceX > 0) {
                            mCurrentScrollDirection = Direction.LEFT;
                        } else {
                            mCurrentScrollDirection = Direction.RIGHT;
                        }
                    } else {
                        mCurrentScrollDirection = Direction.VERTICAL;
                    }
                    break;
                }
                case LEFT: {
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < -mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.RIGHT;
                    }
                    break;
                }
                case RIGHT: {
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.LEFT;
                    }
                    break;
                }
            }

            // Calculate the new origin after scroll.
            switch (mCurrentScrollDirection) {
                case LEFT:
                case RIGHT:
                    if (!isTimeSet()) {
                        mCurrentOrigin.x -= distanceX * mXScrollingSpeed;
                        ViewCompat.postInvalidateOnAnimation(WeekView.this);
                    }
                    break;
                case VERTICAL:
                    if (flingType == ALLDAY_TYPE) {
                        touchAllDayItem.getOriginPoint().y -= distanceY;
                    } else {
                        if (viewStatue == EDIT_STATUE && flingType != EDIT_OUT_VIEW_TYPE) {
                            editPoint.y -= distanceY;
                        } else {
                            mCurrentOrigin.y -= distanceY;
                        }
                    }
                    ViewCompat.postInvalidateOnAnimation(WeekView.this);
                    break;
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mIsZooming)
                return true;

            if ((mCurrentFlingDirection == Direction.LEFT && !mHorizontalFlingEnabled) ||
                    (mCurrentFlingDirection == Direction.RIGHT && !mHorizontalFlingEnabled) ||
                    (mCurrentFlingDirection == Direction.VERTICAL && !mVerticalFlingEnabled)) {
                return true;
            }

//            mScroller.forceFinished(true);

            mCurrentFlingDirection = mCurrentScrollDirection;
            switch (mCurrentFlingDirection) {
                case LEFT:
                case RIGHT:
                    if (!isTimeSet()) {
                        mScroller.fling((int) mCurrentOrigin.x, (int) getCurrectOriginY(), (int) (velocityX * mXScrollingSpeed), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(mHourHeight * 24 + getHourTop() - getHeight()), 0);
                    }
                    break;
                case VERTICAL:
                    if (viewStatue == EDIT_STATUE) {
//                        mScroller.fling((int) mCurrentOrigin.x, (int) getCurrectOriginY(), 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(mHourHeight * 24 + getHourTop() - getHeight()), 0);
                        return true;
                    }

                    if (flingType == ALLDAY_TYPE) {
                        mScroller.fling((int) mCurrentOrigin.x, (int) touchAllDayItem.getOriginPoint().y, 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(touchAllDayItem.getContentHeight() - mAllDayEventHeight), 0);
                    } else {
                        mScroller.fling((int) mCurrentOrigin.x, (int) getCurrectOriginY(), 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(mHourHeight * 24 + getHourTop() - getHeight()), 0);
                    }
//                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(mAllDayEventHeight), 0);
                    break;
            }

            ViewCompat.postInvalidateOnAnimation(WeekView.this);
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // If the tap was on an event then trigger the callback.
            if (createEvent != null
                    && createEvent.rectF != null
                    && !createEvent.rectF.contains(e.getX(), e.getY())
                    && outCreateClickListener != null) {
                outCreateClickListener.onOutCreateEventClick(createEvent);
                setViewStatue(SHOW_STATUE);
                createEvent = null;
                return super.onSingleTapConfirmed(e);
            }


            if (mEventRects != null && mEventClickListener != null) {
                for (EventRect event : mEventRects) {
                    if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
                        mEventClickListener.onEventClick(event, event.rectF);
                        playSoundEffect(SoundEffectConstants.CLICK);
                        return super.onSingleTapConfirmed(e);
                    }
                }
            }


            if (isEditOutClick) {
                isEditOutClick = false;
                return super.onSingleTapConfirmed(e);
            }


            // If the tap was on in an empty space, then trigger the callback.
            if (mEmptyViewClickListener != null && e.getX() > mHeaderColumnWidth && e.getY() > (mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom)) {
                Calendar selectedTime = getTimeFromPoint(e.getX(), e.getY());
                if (selectedTime != null) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mEmptyViewClickListener.onEmptyViewClicked(selectedTime, pastEventEditAble);
                }
            }

            return super.onSingleTapConfirmed(e);
        }


        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
            if (mEventRects != null) {
                int size = mEventRects.size();
                for (int i = 0; i < size; i++) {
                    EventRect event = mEventRects.get(i);
                    if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
                        if (!event.event.isAllDay()) {
                            if (event.event.isCreate() && flingType != ALLDAY_TYPE) {
                                return;
                            }
                            if (pastEventEditAble) {
                                setEditEvent(i);
                            } else {
                                if (event.event.isAboveToday()) {
                                    setEditEvent(i);
                                }
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);


            if (mEventLongPressListener != null && mEventRects != null) {
                int size = mEventRects.size();
                for (int i = 0; i < size; i++) {
                    EventRect event = mEventRects.get(i);
                    if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
                        mEventLongPressListener.onEventLongPress(e, i);
                        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                        return;
                    }
                }
            }

            // If the tap was on in an empty space, then trigger the callback.
            if (mEmptyViewLongPressListener != null && e.getX() > mHeaderColumnWidth && e.getY() > (mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom)) {
                Calendar selectedTime = getTimeFromPoint(e.getX(), e.getY());
                if (selectedTime != null) {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    mEmptyViewLongPressListener.onEmptyViewLongPress(selectedTime);
                }
            }
        }
    };

    public void setEditEvent(int i) {
        editId = mEventRects.get(i).event.getId();
        setViewStatue(EDIT_STATUE);
        editPoint.set(0f, 0f);
        editPos = i;
        ViewCompat.postInvalidateOnAnimation(WeekView.this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!touchAble) {
            return true;
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                编辑状态下


                int id = viewStatue;
                switch (id) {
                    case EDIT_TYPE:
                        editStatus(event);
                        break;

                    case SHOW_STATUE:
                        showStatus(event);
                        break;


                }


            case MotionEvent.ACTION_UP:
                if (viewStatue == EDIT_STATUE) {
                    editListener.onUpListener(editPos, topSum, bottomSum);
                    editPoint.set(0f, 0f);
                    topSum = 0;
                    bottomSum = 0;
                }
                break;

        }


        mScaleDetector.onTouchEvent(event);
        boolean val = mGestureDetector.onTouchEvent(event);

        // Check after call of mGestureDetector, so mCurrentFlingDirection and mCurrentScrollDirection are set.
        if (event.getAction() == MotionEvent.ACTION_UP && !mIsZooming && mCurrentFlingDirection == Direction.NONE) {
            if (mCurrentScrollDirection == Direction.RIGHT || mCurrentScrollDirection == Direction.LEFT) {
                goToNearestOrigin();
            }
            mCurrentScrollDirection = Direction.NONE;
        }

        return val;
    }

    private void showStatus(MotionEvent event) {
        mScroller.forceFinished(true);
        if (isTouchAllDay(event)) {
            flingType = ALLDAY_TYPE;
        } else {
            flingType = DAY_TYPE;
        }
    }

    private void editStatus(MotionEvent event) {
        if (topEditRect.contains(event.getX(), event.getY())) {
            flingType = EDIT_TOP_TYPE;
        } else if (bottomEditRect.contains(event.getX(), event.getY())) {
            flingType = EDIT_BOTTOM_TYPE;
        } else if (mEventRects.get(editPos).rectF != null
                && mEventRects.get(editPos).rectF.contains(event.getX(), event.getY())) {
            flingType = EDIT_TYPE;
        } else {
            flingType = EDIT_OUT_VIEW_TYPE;
            isEditOutClick = true;
            if (!isTimeSet()) {
                setViewStatue(SHOW_STATUE);
            }
            ViewCompat.postInvalidateOnAnimation(WeekView.this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();


        if (mScroller.isFinished()) {
            if (mCurrentFlingDirection != Direction.NONE) {
                // Snap to day after fling is finished.
                goToNearestOrigin();
            }
        } else {
            if (flingType == ALLDAY_TYPE) {
                if (mScroller.computeScrollOffset()) {
                    if (mScroller.isFinished()) {
                        return;
                    }
                    touchAllDayItem.getOriginPoint().y = mScroller.getCurrY();
                    touchAllDayItem.getOriginPoint().x = mScroller.getCurrX();
                }
            } else {
                if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) {
                    goToNearestOrigin();
                } else if (mScroller.computeScrollOffset()) {
//                    if (viewStatue != EDIT_STATUE) {
//                        if (flingType == ALLDAY_TYPE && mCurrentFlingDirection == Direction.NONE) {
//                            if (mScroller.isFinished()) {
//                                return;
//                            }
//                            touchAllDayItem.getOriginPoint().y = mScroller.getCurrY();
//                            touchAllDayItem.getOriginPoint().x = mScroller.getCurrX();
//                        } else {
                    setCurrentOriginY(mScroller.getCurrY());
                    mCurrentOrigin.x = mScroller.getCurrX();
//                        }
                    ViewCompat.postInvalidateOnAnimation(this);
//                }
                }
            }

        }

    }


    private boolean isTimeSet() {
        return timeType == TIME_SET_TYPE;
    }

    public void setZoomAble(boolean zoomAble) {
        this.zoomAble = zoomAble;
    }

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // Hold references.
        mContext = context;

        // Get the attribute values (if any).
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeekView, 0, 0);
        try {
            mFirstDayOfWeek = a.getInteger(R.styleable.WeekView_firstDayOfWeek, mFirstDayOfWeek);
            mHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourHeight, mHourHeight);
            mMinHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_minHourHeight, mMinHourHeight);
            mEffectiveMinHourHeight = mMinHourHeight;
            mMaxHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_maxHourHeight, mMaxHourHeight);
            add_icon_rect = a.getDimensionPixelSize(R.styleable.WeekView_addIconRect, add_icon_rect);
            mTextSize = a.getDimensionPixelSize(R.styleable.WeekView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, context.getResources().getDisplayMetrics()));
            mWeekTextSize = a.getDimensionPixelSize(R.styleable.WeekView_weekTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mWeekTextSize, context.getResources().getDisplayMetrics()));
            mHeaderColumnPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerColumnPadding, mHeaderColumnPadding);
            mColumnGap = a.getDimensionPixelSize(R.styleable.WeekView_columnGap, mColumnGap);
            mHeaderColumnTextColor = a.getColor(R.styleable.WeekView_headerColumnTextColor, mHeaderColumnTextColor);
            mNumberOfVisibleDays = a.getInteger(R.styleable.WeekView_noOfVisibleDays, mNumberOfVisibleDays);
            mShowFirstDayOfWeekFirst = a.getBoolean(R.styleable.WeekView_showFirstDayOfWeekFirst, mShowFirstDayOfWeekFirst);
            mHeaderRowPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerRowPadding, mHeaderRowPadding);
            nowLineCircleR = a.getDimensionPixelSize(R.styleable.WeekView_nowLineCircleR, nowLineCircleR);
            mHeaderRowBackgroundColor = a.getColor(R.styleable.WeekView_headerRowBackgroundColor, mHeaderRowBackgroundColor);
            mDayBackgroundColor = a.getColor(R.styleable.WeekView_dayBackgroundColor, mDayBackgroundColor);
            mFutureBackgroundColor = a.getColor(R.styleable.WeekView_futureBackgroundColor, mFutureBackgroundColor);
            mPastBackgroundColor = a.getColor(R.styleable.WeekView_pastBackgroundColor, mPastBackgroundColor);
            mFutureWeekendBackgroundColor = a.getColor(R.styleable.WeekView_futureWeekendBackgroundColor, mFutureBackgroundColor); // If not set, use the same color as in the week
            mPastWeekendBackgroundColor = a.getColor(R.styleable.WeekView_pastWeekendBackgroundColor, mPastBackgroundColor);
            mNowLineColor = a.getColor(R.styleable.WeekView_nowLineColor, mNowLineColor);
            mNowLineThickness = a.getDimensionPixelSize(R.styleable.WeekView_nowLineThickness, mNowLineThickness);
            mHourSeparatorColor = a.getColor(R.styleable.WeekView_hourSeparatorColor, mHourSeparatorColor);
            mTimeLineSeparatorColor = a.getColor(R.styleable.WeekView_mTimeLineSeparatorColor, mTimeLineSeparatorColor);
            mAllDaySeparatorColor = a.getColor(R.styleable.WeekView_mAllDaySeparatorColor, mAllDaySeparatorColor);
            mTodayBackgroundColor = a.getColor(R.styleable.WeekView_todayBackgroundColor, mTodayBackgroundColor);
            mHourSeparatorHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourSeparatorHeight, mHourSeparatorHeight);
            allDayHeight = a.getDimensionPixelSize(R.styleable.WeekView_allDayHeight, allDayHeight);
            mTodayHeaderTextColor = a.getColor(R.styleable.WeekView_todayHeaderTextColor, mTodayHeaderTextColor);
            mHeaderLabelStartColor = a.getColor(R.styleable.WeekView_mHeaderLabelStartColor, mHeaderLabelStartColor);
            mHeaderLabelEndColor = a.getColor(R.styleable.WeekView_mHeaderLabelEndColor, mHeaderLabelEndColor);
            mTodayHeaderTextColor = a.getColor(R.styleable.WeekView_todayHeaderTextColor, mTodayHeaderTextColor);
            mEventTextSize = a.getDimensionPixelSize(R.styleable.WeekView_eventTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mEventTextSize, context.getResources().getDisplayMetrics()));
            mCreateEventSize = a.getDimensionPixelSize(R.styleable.WeekView_createEventSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mCreateEventSize, context.getResources().getDisplayMetrics()));
            mEventTextColor = a.getColor(R.styleable.WeekView_eventTextColor, mEventTextColor);
            mCreateEventColor = a.getColor(R.styleable.WeekView_mCreateEventColor, mCreateEventColor);
            mTimeColumnBgColor = a.getColor(R.styleable.WeekView_mTimeColumnBgColor, mTimeColumnBgColor);
            mHeaderLabelColor = a.getColor(R.styleable.WeekView_mHeaderLabelColor, mHeaderLabelColor);
            mHeaderLabelBackColor = a.getColor(R.styleable.WeekView_mHeaderLabelBackColor, mHeaderLabelBackColor);
            mEventFinishTextColor = a.getColor(R.styleable.WeekView_eventFinishTextColor, mEventFinishTextColor);
            mEventPadding = a.getDimensionPixelSize(R.styleable.WeekView_eventPadding, mEventPadding);
            mAllDayEventItemPadding = a.getDimensionPixelSize(R.styleable.WeekView_eventPadding, mAllDayEventItemPadding);
            touchEditCicleR = a.getDimensionPixelSize(R.styleable.WeekView_touchEditCicleR, touchEditCicleR);
            rectMargin = a.getDimensionPixelSize(R.styleable.WeekView_rectMargin, rectMargin);
            editCicleDistance = a.getInteger(R.styleable.WeekView_editCicleDistance, editCicleDistance);
            eventLeftColorWidth = a.getDimensionPixelSize(R.styleable.WeekView_eventLeftColorWidth, eventLeftColorWidth);
            mHeaderColumnBackgroundColor = a.getColor(R.styleable.WeekView_headerColumnBackground, mHeaderColumnBackgroundColor);
            mDayNameLength = a.getInteger(R.styleable.WeekView_dayNameLength, mDayNameLength);
            mOverlappingEventGap = a.getDimensionPixelSize(R.styleable.WeekView_overlappingEventGap, mOverlappingEventGap);
            mEventMarginVertical = a.getDimensionPixelSize(R.styleable.WeekView_eventMarginVertical, mEventMarginVertical);
            mXScrollingSpeed = a.getFloat(R.styleable.WeekView_xScrollingSpeed, mXScrollingSpeed);
            mEventCornerRadius = a.getDimensionPixelSize(R.styleable.WeekView_eventCornerRadius, mEventCornerRadius);
            mShowDistinctPastFutureColor = a.getBoolean(R.styleable.WeekView_showDistinctPastFutureColor, mShowDistinctPastFutureColor);
            mShowDistinctWeekendColor = a.getBoolean(R.styleable.WeekView_showDistinctWeekendColor, mShowDistinctWeekendColor);
            mShowNowLine = a.getBoolean(R.styleable.WeekView_showNowLine, mShowNowLine);
            mHorizontalFlingEnabled = a.getBoolean(R.styleable.WeekView_horizontalFlingEnabled, mHorizontalFlingEnabled);
            mVerticalFlingEnabled = a.getBoolean(R.styleable.WeekView_verticalFlingEnabled, mVerticalFlingEnabled);
            mAllDayEventMaxHeight = a.getDimensionPixelSize(R.styleable.WeekView_allDayEventHeight, mAllDayEventMaxHeight);
            mScrollDuration = a.getInt(R.styleable.WeekView_scrollDuration, mScrollDuration);
            editCircleIconId = a.getResourceId(R.styleable.WeekView_editCircleIconId, editCircleIconId);
            addIconReference = a.getResourceId(R.styleable.WeekView_addIconReference, addIconReference);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        // Scrolling initialization.
        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
        mScroller = new OverScroller(mContext, new FastOutLinearInInterpolator());

        mMinimumFlingVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();
        mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        // Measure settings for time column.
        mTimeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTimeTextPaint.setTextSize(mTextSize);
        mTimeTextPaint.setColor(mHeaderColumnTextColor);
        Rect rect = new Rect();
        mTimeTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mTimeTextHeight = rect.height();
        mHeaderMarginBottom = mTimeTextHeight / 2;
        mHeaderMarginBottom = 0;
        initTextTimeWidth();
        nowLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        nowLine.setStrokeWidth((float) 5.0);
        nowLine.setColor(Color.RED);
        // Measure settings for header row.
        mHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTextPaint.setColor(mHeaderColumnTextColor);
        mHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mHeaderTextPaint.setTextSize(mTextSize);
        mHeaderTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mHeaderTextHeight = rect.height();
        mHeaderTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mHeaderLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderLabelPaint.setColor(mHeaderLabelColor);
        mHeaderLabelPaint.setTextAlign(Paint.Align.CENTER);
        mHeaderLabelPaint.setTextSize(mTextSize);
        mHeaderLabelPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mHeaderLabelPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mHeaderWeekTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderWeekTextPaint.setColor(mHeaderLabelColor);
        mHeaderWeekTextPaint.setTextAlign(Paint.Align.CENTER);
        mHeaderWeekTextPaint.setTextSize(mWeekTextSize);
//        mHeaderWeekTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mHeaderTextHeight = rect.height();
        mHeaderWeekTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        // Prepare header background paint.
        mHeaderBackgroundPaint = new Paint();
        mHeaderBackgroundPaint.setColor(mHeaderRowBackgroundColor);


        mHeaderLabelBackgroundPaint = new Paint();
        LinearGradient lg=new LinearGradient(0,0,1000,getHeight(),mHeaderLabelStartColor,mHeaderLabelEndColor, Shader.TileMode.CLAMP);
        mHeaderLabelBackgroundPaint.setShader(lg);

        mAllDayBackShaderPaint = new Paint();
        mAllDayBackShaderPaint.setColor(mHeaderRowBackgroundColor);
        mAllDayBackShaderPaint.setShadowLayer(4, 4, 4, mAllDaySeparatorColor);

        // Prepare day background color paint.
        mDayBackgroundPaint = new Paint();
        mDayBackgroundPaint.setColor(mDayBackgroundColor);
        mFutureBackgroundPaint = new Paint();
        mFutureBackgroundPaint.setColor(mFutureBackgroundColor);
        mPastBackgroundPaint = new Paint();
        mPastBackgroundPaint.setColor(mPastBackgroundColor);
        mFutureWeekendBackgroundPaint = new Paint();
        mFutureWeekendBackgroundPaint.setColor(mFutureWeekendBackgroundColor);
        mPastWeekendBackgroundPaint = new Paint();
        mPastWeekendBackgroundPaint.setColor(mPastWeekendBackgroundColor);

        // Prepare hour separator color paint.
        mHourSeparatorPaint = new Paint();
        mHourSeparatorPaint.setStyle(Paint.Style.STROKE);
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorHeight);
        mHourSeparatorPaint.setColor(mHourSeparatorColor);


        mTimeLineSeparatorPaint = new Paint();
        mTimeLineSeparatorPaint.setStyle(Paint.Style.STROKE);
        mTimeLineSeparatorPaint.setStrokeWidth(mHourSeparatorHeight);
        mTimeLineSeparatorPaint.setColor(mTimeLineSeparatorColor);


        // Prepare the "now" line color paint
        mNowLinePaint = new Paint();
        mNowLinePaint.setStrokeWidth(mNowLineThickness);
        mNowLinePaint.setColor(mNowLineColor);

        // Prepare today background color paint.
        mTodayBackgroundPaint = new Paint();
        mTodayBackgroundPaint.setColor(mTodayBackgroundColor);

        // Prepare today header text color paint.
        mTodayHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mTodayHeaderTextPaint.setTypeface(Typeface.DEFAULT);
        mTodayHeaderTextPaint.setColor(mTodayHeaderTextColor);


        mWeekHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWeekHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mWeekHeaderTextPaint.setTextSize(mWeekTextSize);
        mWeekHeaderTextPaint.setTypeface(Typeface.DEFAULT);
        mWeekHeaderTextPaint.setColor(mTodayHeaderTextColor);


        // Prepare event background color.
        mEventBackgroundPaint = new Paint();
        mEventBackgroundPaint.setColor(mEventBackColor);

        // Prepare header column background color.
        mHeaderColumnBackgroundPaint = new Paint();
        mHeaderColumnBackgroundPaint.setColor(mHeaderColumnBackgroundColor);

        // Prepare event text size and color.
        mEventTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mEventTextPaint.setStyle(Paint.Style.FILL);
        mEventTextPaint.setColor(mEventTextColor);
        mEventTextPaint.setTextSize(mEventTextSize);
        // create event text size and color.

        mCreateEventPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mCreateEventPaint.setStyle(Paint.Style.FILL);
        mCreateEventPaint.setColor(mCreateEventColor);
        mCreateEventPaint.setTextSize(mCreateEventSize);


//        boarder paint
        mEventBoarderPaint = new Paint();
        mEventBoarderPaint.setColor(Color.RED);
        mEventBoarderPaint.setStyle(Paint.Style.STROKE);
        mEventBoarderPaint.setStrokeWidth(1f);
        path = new Path();


//  time left bg
        timeColumnBgPaint = new Paint();
        timeColumnBgPaint.setColor(mTimeColumnBgColor);

        // Set default event color.
        mDefaultEventColor = Color.parseColor("#9fc6e7");

        mScaleDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                if (!zoomAble) {
                    return;
                }
                mIsZooming = false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                if (!zoomAble) {
                    return true;
                }
                mIsZooming = true;
                goToNearestOrigin();
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (!zoomAble) {
                    return true;
                }
                mNewHourHeight = Math.round(mHourHeight * detector.getScaleFactor());
                invalidate();
                return true;
            }
        });
    }

    // fix rotation changes
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mAreDimensionsInvalid = true;
    }

    /**
     * Initialize time column width. Calculate value with all possible hours (supposed widest text).
     */
    private void initTextTimeWidth() {
        mTimeTextWidth = 0;
        for (int i = 0; i < 24; i++) {
            // Measure time string and get max width.
            String time = getDateTimeInterpreter().interpretTime(i);
            if (time == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null time");
            mTimeTextWidth = Math.max(mTimeTextWidth, mTimeTextPaint.measureText(time));
        }

        mTimeTextWidth += mTimeTextWidth / 2;
    }


    public void setLinearWidth(int width) {
        LinearGradient lg = new LinearGradient(0, 0, width, getHeight(), mHeaderLabelStartColor, mHeaderLabelEndColor, Shader.TileMode.CLAMP);
        mHeaderLabelBackgroundPaint.setShader(lg);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the header row.
        drawHeaderRowAndEvents(canvas);
        // Draw the time column and all the axes/separators.
        drawTimeColumnAndAxes(canvas);
    }


    public void showTimeArea() {
        showTimeArea = true;
//        postInvalidate();
    }

    private HashMap<Integer, Integer> cacheAllDayHeight = new HashMap<>();

    private void calculateHeaderHeight() {
        //Make sure the header is the right size (depends on AllDay events)
        cacheAllDayHeight.clear();
        if (isTimeSet()) {
            mHeaderHeight = 0;
            containsAllDayEvent = false;
            return;
        }
        containsAllDayEvent = false;
        if (mEventRects != null && mEventRects.size() > 0) {
            for (int dayNumber = 0;
                 dayNumber < mNumberOfVisibleDays;
                 dayNumber++) {
                Calendar day = (Calendar) getFirstVisibleDay().clone();
                day.add(Calendar.DATE, dayNumber);
                for (int i = 0; i < mEventRects.size(); i++) {
                    if (isSameDay(mEventRects.get(i).event.getStartTime(), day) && mEventRects.get(i).event.isAllDay()) {
                        int cacheHeight = cacheAllDayHeight.containsKey(dayNumber) ? cacheAllDayHeight.get(dayNumber) : 0;
                        cacheAllDayHeight.put(dayNumber, cacheHeight + allDayHeight);
                        containsAllDayEvent = true;
                    }
                }
            }
        }


        if (containsAllDayEvent) {
            int maxHeight = Collections.max(cacheAllDayHeight.values());
            if (maxHeight > mAllDayEventMaxHeight) {
                mAllDayEventHeight = mAllDayEventMaxHeight;
            } else {
                mAllDayEventHeight = maxHeight;
            }
            mHeaderHeight = mAllDayEventHeight + mHeaderMarginBottom + mAllDayEventItemPadding;
        } else {
            mHeaderHeight = mAllDayEventItemPadding;
        }
    }


    //    下一页
    public void setNextPager() {
        mCurrentOrigin.x -= (mDayColumnWidth + mColumnGap) * mNumberOfVisibleDays;
        notifyDatasetChanged();
    }


    //    上一页
    public void setPreviousPager() {
        mCurrentOrigin.x += (mDayColumnWidth + mColumnGap) * mNumberOfVisibleDays;
        notifyDatasetChanged();
    }


    public void setCurrectFirstDayPager(Calendar currectDay) {
        int dayCount = -(int) getDayCount(currectDay);
        mCurrentOrigin.x = (mDayColumnWidth + mColumnGap) * dayCount;
        notifyDatasetChanged();
    }

    private float getDayCount(Calendar currectDay) {
        Calendar str2 = today();  //"yyyyMMdd"格式 如 20131022
        return (float) ((currectDay.getTimeInMillis() - str2.getTimeInMillis()) / (1000 * 3600 * 24));
    }


    private void drawTimeColumnAndAxes(Canvas canvas) {
        // Draw the background color for the header column.
        drawAllDayLabel(canvas);


        // Clip to paint in left column only.
        canvas.clipRect(0, getHourTop(), mHeaderColumnWidth + rectMargin, getHeight(), Region.Op.REPLACE);
        mHeaderColumnBackgroundPaint.setAlpha(0);
        canvas.drawRect(0, getHourTop(), mHeaderColumnWidth + rectMargin, getHeight(), mHeaderColumnBackgroundPaint);
        mHeaderColumnBackgroundPaint.setAlpha(255);


//        bg
        if (containsAllDayEvent) {
            canvas.drawRect(0, getHourTop() + 8, mHeaderColumnWidth, getHeight(), timeColumnBgPaint);
        } else {
            canvas.drawRect(0, 0, mHeaderColumnWidth, getHeight(), timeColumnBgPaint);
        }


        canvas.drawLine(mHeaderColumnWidth, getHourTop(), mHeaderColumnWidth, getHeight(), mTimeLineSeparatorPaint);

        for (int i = 0; i < 24; i++) {
            float top = getHourTop() + getCurrectOriginY() + mHourHeight * i + mHeaderMarginBottom;
            // Draw the text if its y position is not outside of the visible area. The pivot point of the text is the point at the bottom-right corner.
            String time = getDateTimeInterpreter().interpretTime(i);
//            String time = i + getResources().getString(hour);
            if (time == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null time");
            if (top < getHeight())
                if (i != 0) {
                    canvas.drawText(time, mTimeTextWidth * 0.9f, top + mTimeTextHeight, mTimeTextPaint);
                }

        }
    }

    private float getHourColumnTop() {
        if (isTimeSet()) {
            return 0;
        }
        return mHeaderHeight + mHeaderRowPadding * 2;
    }

    private void drawAllDayLabel(Canvas canvas) {
//        if (!containsAllDayEvent) {
//            return;
//        }
//        canvas.clipRect(0, getHeaderTop(), mHeaderColumnWidth, getHeight(), Region.Op.REPLACE);
        canvas.drawRect(0, getHeaderTop(), mHeaderColumnWidth, getHeight(), mHeaderBackgroundPaint);

        if (containsAllDayEvent) {
            canvas.drawText(getResources().getString(R.string.all_day), mHeaderColumnWidth * 0.9f, getHeaderTop() + mHeaderHeight / 2 + mEventPadding, mTimeTextPaint);
        }

        canvas.save();
        canvas.drawLine(mHeaderColumnWidth, getHeaderTop(), mHeaderColumnWidth, getHourTop(), mTimeLineSeparatorPaint);
        canvas.drawLine(0, getHeaderTop(), mHeaderColumnWidth, getHeaderTop(), mHourSeparatorPaint);
        canvas.restore();
    }


    private void drawHeaderRowAndEvents(Canvas canvas) {
        // Calculate the available width for each day.
        mHeaderColumnWidth = mTimeTextWidth + mHeaderColumnPadding * 2;
        computeColumnWidth();
        calculateHeaderHeight(); //Make sure the header is the right size (depends on AllDay events)

        Calendar today = today();
        if (mAreDimensionsInvalid) {
            mEffectiveMinHourHeight = Math.max(mMinHourHeight, (int) ((getHeight() - mHeaderHeight - mHeaderRowPadding * 2 - mHeaderMarginBottom) / 24));

            mAreDimensionsInvalid = false;
            if (mScrollToDay != null)
                goToDate(mScrollToDay);

            mAreDimensionsInvalid = false;
            if (mScrollToHour >= 0)
                goToHour(mScrollToHour);

            mScrollToDay = null;
            mScrollToHour = -1;
            mAreDimensionsInvalid = false;
        }
        if (mIsFirstDraw) {
            mIsFirstDraw = false;

            // If the week view is being drawn for the first time, then consider the first day of the week.
            if (mNumberOfVisibleDays >= 7 && today.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek && mShowFirstDayOfWeekFirst) {
                int difference = (today.get(Calendar.DAY_OF_WEEK) - mFirstDayOfWeek);
                mCurrentOrigin.x += (mDayColumnWidth + mColumnGap) * difference;
            }
        }

        // Calculate the new height due to the zooming.
//        if (mNewHourHeight > 0) {
//            if (mNewHourHeight < mEffectiveMinHourHeight)
//                mNewHourHeight = mEffectiveMinHourHeight;
//            else if (mNewHourHeight > mMaxHourHeight)
//                mNewHourHeight = mMaxHourHeight;
//
//            mCurrentOrigin.y = (mCurrentOrigin.y / mHourHeight) * mNewHourHeight;
//            mHourHeight = mNewHourHeight;
//            mNewHourHeight = -1;
//        }

        // If the new mCurrentOrigin.y is invalid, make it valid.
        if (getCurrectOriginY() < getHeight() - mHourHeight * 24 - getHourTop()) {
//            mCurrentOrigin.y = getHeight() - mHourHeight * 24 - getHourTop();
            setCurrentOriginY(getHeight() - mHourHeight * 24 - getHourTop());
        }


        // Don't put an "else if" because it will trigger a glitch when completely zoomed out and
        // scrolling vertically.
        if (getCurrectOriginY() > 0) {
//            mCurrentOrigin.y = 0;
            setCurrentOriginY(0);
        }

        // Consider scroll offset.
        int leftDaysWithGaps = (int) -(Math.ceil(mCurrentOrigin.x / (mDayColumnWidth + mColumnGap)));
        float startFromPixel = mCurrentOrigin.x + (mDayColumnWidth + mColumnGap) * leftDaysWithGaps +
                mHeaderColumnWidth;
        float startPixel = startFromPixel;

        // Prepare to iterate for each day.
        Calendar day = (Calendar) today.clone();
        day.add(Calendar.HOUR, 6);

        // Prepare to iterate for each hour to draw the hour lines.
        int lineCount = (int) ((getHeight() - mHeaderHeight - mHeaderRowPadding * 2 -
                mHeaderMarginBottom) / mHourHeight) + 1;
        lineCount = (lineCount) * (mNumberOfVisibleDays + 1);
        float[] hourLines = new float[lineCount * 4];

        // Clear the cache for event rectangles.
        if (mEventRects != null) {
            for (EventRect eventRect : mEventRects) {
                eventRect.rectF = null;
            }
        }

        canvas.clipRect(mHeaderColumnWidth, getHourTop(), getWidth(), getHeight(), Region.Op.REPLACE);
        // Clip to paint events only.


        // Iterate through each day.
        Calendar oldFirstVisibleDay = mFirstVisibleDay;
        mFirstVisibleDay = (Calendar) today.clone();
        mFirstVisibleDay.add(Calendar.DATE, -(Math.round(mCurrentOrigin.x / (mDayColumnWidth + mColumnGap))));
        if (!mFirstVisibleDay.equals(oldFirstVisibleDay) && mScrollListener != null) {
            mScrollListener.onFirstVisibleDayChanged(mFirstVisibleDay, oldFirstVisibleDay);
        }
        for (int dayNumber = leftDaysWithGaps + 1;
             dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1;
             dayNumber++) {

            // Check if the day is today.
            day = (Calendar) today.clone();
            mLastVisibleDay = (Calendar) day.clone();
            day.add(Calendar.DATE, dayNumber - 1);
            mLastVisibleDay.add(Calendar.DATE, dayNumber - 2);
            boolean sameDay = isSameDay(day, today);

            // Get more events if necessary. We want to store the events 3 months beforehand. Get
            // events only when it is the first iteration of the loop.
            if (mEventRects == null || mRefreshEvents ||
                    (dayNumber == leftDaysWithGaps + 1
                            && mFetchedPeriod != (int) mWeekViewLoader.toWeekViewPeriodIndex(day)
                            && Math.abs(mFetchedPeriod - mWeekViewLoader.toWeekViewPeriodIndex(day)) > 0.5)) {
                getMoreEvents(day);
                mRefreshEvents = false;
            }
            // Draw background color for each day.
            float start = (startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel);
            if (mDayColumnWidth + startPixel - start > 0) {
                if (mShowDistinctPastFutureColor) {
                    boolean isWeekend = day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
                    Paint pastPaint = isWeekend && mShowDistinctWeekendColor ? mPastWeekendBackgroundPaint : mPastBackgroundPaint;
                    Paint futurePaint = isWeekend && mShowDistinctWeekendColor ? mFutureWeekendBackgroundPaint : mFutureBackgroundPaint;
                    float startY = mHeaderHeight + mHeaderRowPadding * 2 + mTimeTextHeight / 2 + mHeaderMarginBottom + getCurrectOriginY();

                    if (sameDay) {
                        Calendar now = Calendar.getInstance();
                        float beforeNow = (now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) / 60.0f) * mHourHeight;
                        canvas.drawRect(start, startY, startPixel + mDayColumnWidth, startY + beforeNow, pastPaint);
                        canvas.drawRect(start, startY + beforeNow, startPixel + mDayColumnWidth, getHeight(), futurePaint);
                    } else if (day.before(today)) {
                        canvas.drawRect(start, startY, startPixel + mDayColumnWidth, getHeight(), pastPaint);
                    } else {
                        canvas.drawRect(start, startY, startPixel + mDayColumnWidth, getHeight(), futurePaint);
                    }
                } else {
                    canvas.drawRect(start, getHourTop(), startPixel + mDayColumnWidth, getHeight(), sameDay ? mTodayBackgroundPaint : mDayBackgroundPaint);
                }
            }

            // Prepare the separator lines for hours.
            drawRowLines(canvas, startPixel, hourLines, start);
            // Draw the events.
            drawEvents(day, startPixel, canvas);
            drawNowTime(canvas, startPixel, sameDay, start);
            drawColumnLine(canvas, startPixel);
            canvas.drawLine(mHeaderColumnWidth, getHourTop(), getWidth(), getHourTop(), mHourSeparatorPaint);
            // In the next iteration, start from the next day.
            startPixel += mDayColumnWidth + mColumnGap;

        }

        drawEditView(canvas);
        drawAllDay(canvas, today, leftDaysWithGaps, startFromPixel);
        scrollToTimeLine();
        initTimeLine();
    }

    private void drawAllDay(Canvas canvas, Calendar today, int leftDaysWithGaps, float startFromPixel) {
        float startPixel;
        Calendar day;// Hide everything in the first cell (top left corner).
        canvas.clipRect(0, 0, mTimeTextWidth + mHeaderColumnPadding * 2, getHeaderTop() + mHeaderHeight, Region.Op.REPLACE);
        canvas.drawRect(0, 0, mTimeTextWidth + mHeaderColumnPadding * 2, getHeaderTop() + mHeaderHeight, mHeaderBackgroundPaint);
        drawAllDayBack(canvas);
//        drawTopHeaderLine(canvas);
        // Draw the header row texts.
        startPixel = startFromPixel;
        canvas.drawRect(0, 0, getWidth(), getHeaderTop(), mHeaderLabelBackgroundPaint);
        for (int dayNumber = leftDaysWithGaps + 1; dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1; dayNumber++) {
            // Check if the day is today.
            day = (Calendar) today.clone();
            day.add(Calendar.DATE, dayNumber - 1);
            boolean sameDay = isSameDay(day, today);
            // Draw the day labels.
            String dayLabel = getDateTimeInterpreter().interpretDate(day);
            String dayWeek = getDateTimeInterpreter().interpretWeek(day);
            if (dayLabel == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null date");
            drawAllDayEvents(day, startPixel, canvas);
            drawWeekHeader(canvas, startPixel, sameDay, dayLabel, dayWeek);
            startPixel += mDayColumnWidth + mColumnGap;
        }
    }

    private void drawWeekHeader(Canvas canvas, float startPixel, boolean sameDay, String dayLabel, String dayWeek) {
        if (!isTimeSet()) {
            Paint weekPaint = sameDay ? mTodayHeaderTextPaint : mHeaderLabelPaint;
            Paint labelPaint = sameDay ? mWeekHeaderTextPaint : mHeaderWeekTextPaint;
            float daywidth = labelPaint.measureText(dayLabel);
            float weekwidth = weekPaint.measureText(dayWeek);
            float startX = startPixel + (mDayColumnWidth - daywidth - weekwidth) / 2;
            canvas.drawRect(startPixel, 0, startPixel + mDayColumnWidth, getHeaderTop(), mHeaderLabelBackgroundPaint);
            canvas.drawText(dayLabel, startX, mHeaderTextHeight + mHeaderRowPadding, labelPaint);
            canvas.drawText(dayWeek, startX + weekwidth, mHeaderTextHeight + mHeaderRowPadding, weekPaint);
            canvas.save();
//            canvas.drawLine(startPixel, 0, startPixel, getHeaderTop(), mHourSeparatorPaint);
            canvas.restore();
        }
    }

    private void initTimeLine() {
        if (scrollToTimeLine) {
            float top = mHourHeight * toTime;
            mCurrentOrigin.y = -top;
            invalidate();
            scrollToTimeLine = false;
        }
    }

    private void drawAllDayBack(Canvas canvas) {
        if (isTimeSet()) {
            return;
        }
        // Clip to paint header row only.
        canvas.clipRect(0, 0, getWidth(), getHeaderTop() + mHeaderHeight + 20, Region.Op.REPLACE);
        mHeaderBackgroundPaint.setAlpha(0);
        // Draw the header background.
        canvas.drawRect(0, 0, getWidth(), getHeaderTop() + mHeaderHeight + 20, mHeaderBackgroundPaint);
        mHeaderBackgroundPaint.setAlpha(255);
        if (containsAllDayEvent) {
            canvas.save();
            canvas.drawRect(0, getHeaderTop(), getWidth(), getHeaderTop() + mHeaderHeight, mAllDayBackShaderPaint);
            canvas.restore();
        }
        // Clip to paint header row only.
        canvas.clipRect(0, 0, getWidth(), getHeaderTop() + mHeaderHeight, Region.Op.REPLACE);

        // Draw the header background.
        canvas.drawRect(0, 0, getWidth(), getHeaderTop() + mHeaderHeight, mHeaderBackgroundPaint);
    }

    private void scrollToTimeLine() {
        if (showTimeArea) {
            if (mEventRects != null && mEventRects.size() > 0 && mEventRects.get(editPos) != null) {
                float top = mHourHeight * 24 * mEventRects.get(editPos).top / 1440 + mHeaderHeight + getHeaderTop();
                mCurrentOrigin.y = mHourHeight - top;
                invalidate();
                showTimeArea = false;
            }
        }
    }


    public void scrollToTime(int hour) {
        this.toTime = hour;
        scrollToTimeLine = true;
    }


    private void drawEditView(Canvas canvas) {
        if (viewStatue == EDIT_STATUE) {
            EventRect event = mEventRects.get(editPos);
            RectF rectF = event.rectF;
            if (rectF == null) {
                return;
            }

            float top, bottom;
            switch (flingType) {
                case EDIT_TOP_TYPE:
                    topSum = editPoint.y;
                    bottomSum = 0;
                    break;

                case EDIT_BOTTOM_TYPE:
                    topSum = 0;
                    bottomSum = editPoint.y;
                    break;
                default:
                    bottomSum = editPoint.y;
                    topSum = editPoint.y;
                    break;
            }


            top = event.rectF.top + topSum;
            bottom = event.rectF.bottom + bottomSum;
            float minHeight = mHourHeight * 0.25f;
            if (bottom - top <= minHeight) {
                switch (flingType) {
                    case EDIT_TOP_TYPE:
                        topSum = bottom - minHeight - event.rectF.top - mAllDayEventItemPadding * 2;
                        break;

                    case EDIT_BOTTOM_TYPE:
                        bottomSum = top + minHeight - event.rectF.bottom + mAllDayEventItemPadding * 2;
                        break;
                }
            }
            top = event.rectF.top + topSum;
            bottom = event.rectF.bottom + bottomSum;
            if (top <= getHourTop() + getCurrectOriginY()) {
                topSum = getHourTop() + getCurrectOriginY() - event.rectF.top + mAllDayEventItemPadding * 2;
                if (flingType == EDIT_TOP_TYPE) {
                    bottomSum = 0;
                } else {
                    bottomSum = topSum;
                }

            }

            while (bottom > mHourHeight * 24 + getHourTop() + getCurrectOriginY()) {
                bottomSum = mHourHeight * 24 + getHourTop() + getCurrectOriginY() - mHourHeight / 60 - event.rectF.bottom;
                if (flingType == EDIT_BOTTOM_TYPE) {
                    topSum = 0;
                } else {
                    topSum = bottomSum;
                }

//                top = event.rectF.top + topSum;
                bottom = event.rectF.bottom + bottomSum;
            }
            top = event.rectF.top + topSum;


//            if (top < mHourHeight && mCurrentOrigin.y != 0) {
//                speedType = SPEED_TOP_STATUE;
//            } else if (bottom > getHeight() - mHourHeight && mCurrentOrigin.y != getHeight() - mHourHeight * 24 - getHourTop()) {
//                speedType = SPEED_BOTTOM_STATUE;
//            } else {
//                speedType = SPEED_NULL_STATUE;
//            }


            // Calculate left and right.
            float left = event.rectF.left;
            float right = event.rectF.right;
            // Draw the event and the event name on top of it.
            if (left < right &&
                    left < getWidth() &&
                    top < getHeight() &&
                    right > mHeaderColumnWidth &&
                    bottom > getHourTop()) {

                event.rectF = new RectF(left, top, right, bottom);
                if (bottom - top < minHeight - mAllDayEventItemPadding * 2) {
                    drawEventMinHeight(canvas, event, top, left);
                    drawBoarder(canvas, event.event, event.rectF);
                    drawEditMinHeightSizeCircle(canvas, top, left);
                    mEventRects.get(editPos).rectF = null;
                } else {
                    mEventBackgroundPaint.setColor(event.event.getColor() == 0 ? mDefaultEventColor : event.event.getColor());
                    canvas.drawRect(left + rectMargin, top + rectMargin, left + eventLeftColorWidth - rectMargin, bottom - rectMargin, mEventBackgroundPaint);
                    if (isTimeSet()) {
                        mEventBackgroundPaint.setAlpha(80);
                    }

                    canvas.drawRoundRect(event.rectF, mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);
                    drawEventTitle(event, event.event, event.rectF, canvas, top, left);
                    editListener.onDragingListener(editPos, topSum, bottomSum);
                    if (isTimeSet()) {
                        mEventBackgroundPaint.setAlpha(255);
                    }

                    drawBoarder(canvas, event.event, event.rectF);

                    drawEditSizeCircle(event, canvas, top, bottom, left, right);

                }
            }

        }
    }


    private void drawEditMinHeightSizeCircle(Canvas canvas, float top, float left) {
        topEditRect = new RectF();
        bottomEditRect = new RectF();
        float topX = left + touchEditCicleR * 4;
        float topY = top + touchEditCicleR;
        canvas.drawCircle(topX, topY, touchEditCicleR, mEventBackgroundPaint);
        mEventBackgroundPaint.setAntiAlias(true);
        mEventBackgroundPaint.setColor(Color.WHITE);
        int smallR = (int) (touchEditCicleR * 0.6);
        canvas.drawCircle(topX, topY, smallR, mEventBackgroundPaint);
    }

    private void drawEventMinHeight(Canvas canvas, EventRect event, float top, float left) {
        int availableWidth = (int) (event.rectF.right - left - mEventPadding * 2);
        String bob = EventTextUtils.getEventLineTitle(event.event);
        StaticLayout textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        float originalTop = top - textLayout.getHeight();
        canvas.save();
        canvas.translate(left + touchEditCicleR * 5 + mEventPadding, originalTop + getMidWidth());
        textLayout.draw(canvas);
        canvas.restore();
    }


    private void drawEditSizeCircle(EventRect event, Canvas canvas, float top, float bottom, float left, float right) {
        float margin = touchEditCicleR * editCicleDistance;
        float scal = (event.rectF.right - event.rectF.left) / mDayColumnWidth;
        margin = scal * margin;
        float topX = left + margin;
        float topY = top;
        float bottomX = right - margin;
        float bottomY = bottom;
        topEditRect = new RectF(topX - getMidWidth(), topY - getMidWidth(), topX + getMidWidth(), topY + getMidWidth());
        bottomEditRect = new RectF(bottomX - getMidWidth(), bottomY - getMidWidth(), bottomX + getMidWidth(), bottomY + getMidWidth());
//        canvas.drawRect(topEditRect, mEventBackgroundPaint);


        int editColor = event.event.getmEditColor();
        if (editColor != 0 && !isTimeSet()) {
            mEventBackgroundPaint.setColor(editColor);
        }
        canvas.drawCircle(topX, topY, touchEditCicleR, mEventBackgroundPaint);
        canvas.drawCircle(bottomX, bottomY, touchEditCicleR, mEventBackgroundPaint);
        mEventBackgroundPaint.setAntiAlias(true);
        mEventBackgroundPaint.setColor(Color.WHITE);
        int smallR = (int) (touchEditCicleR * 0.6);
        canvas.drawCircle(topX, topY, smallR, mEventBackgroundPaint);
        canvas.drawCircle(bottomX, bottomY, smallR, mEventBackgroundPaint);
    }

    private int getMidWidth() {
        return touchEditCicleR * 3;
    }

    private void drawTopHeaderLine(Canvas canvas) {
        canvas.drawLine(0, getHeaderTop(), getWidth(), getHeaderTop(), mHourSeparatorPaint);
    }

    private void drawRowLines(Canvas canvas, float startPixel, float[] hourLines, float start) {
        int i = 0;
        for (int hourNumber = 0; hourNumber < 24; hourNumber++) {
            float top = getHourTop() + getCurrectOriginY() + mHourHeight * hourNumber;
            if (top > getHourTop() - mHourSeparatorHeight && top < getHeight() && startPixel + mDayColumnWidth - start > 0) {
                hourLines[i * 4] = start;
                hourLines[i * 4 + 1] = top;
                hourLines[i * 4 + 2] = startPixel + mDayColumnWidth;
                hourLines[i * 4 + 3] = top;
                i++;
            }
        }

        // Draw the lines for hours.
        canvas.drawLines(hourLines, mHourSeparatorPaint);
    }

    private void drawColumnLine(Canvas canvas, float startPixel) {
        float startY = getHeaderTop() + getCurrectOriginY();
        float startX = startPixel - mColumnGap;
        float endY = startY + 26 * mHourHeight;
        float endX = startX;
        canvas.drawLine(startX, startY, endX, endY, mHourSeparatorPaint);
    }

    private void drawNowTime(Canvas canvas, float startPixel, boolean sameDay, float start) {
        // Draw the line at the current time.
        if (mShowNowLine && sameDay) {
            float startY = getHourTop() + getCurrectOriginY();
            Calendar now = Calendar.getInstance();
            float beforeNow = (now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) / 60.0f) * mHourHeight;
            canvas.drawLine(start + nowLineCircleR, startY + beforeNow, startPixel + mDayColumnWidth, startY + beforeNow, mNowLinePaint);
            mNowLinePaint.setAntiAlias(true);
            canvas.drawCircle(start + nowLineCircleR, startY + beforeNow, nowLineCircleR, mNowLinePaint);
        }
    }


    private float getHourTop() {
        if (isTimeSet()) {
            return 0;
        }
        return mHeaderHeight + mHeaderRowPadding * 2 + mTimeTextHeight + mHeaderMarginBottom;
    }

    private void computeColumnWidth() {
        mDayColumnWidth = getWidth() - mHeaderColumnWidth - mColumnGap * (mNumberOfVisibleDays - 1);
        mDayColumnWidth = mDayColumnWidth / mNumberOfVisibleDays;
    }

    /**
     * Get the time and date where the user clicked on.
     *
     * @param x The x position of the touch event.
     * @param y The y position of the touch event.
     * @return The time and date at the clicked position.
     */
    private Calendar getTimeFromPoint(float x, float y) {
        int leftDaysWithGaps = (int) -(Math.ceil(mCurrentOrigin.x / (mDayColumnWidth + mColumnGap)));
        float startPixel = mCurrentOrigin.x + (mDayColumnWidth + mColumnGap) * leftDaysWithGaps +
                mHeaderColumnWidth;
        for (int dayNumber = leftDaysWithGaps + 1;
             dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1;
             dayNumber++) {
            float start = (startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel);
            if (mDayColumnWidth + startPixel - start > 0 && x > start && x < startPixel + mDayColumnWidth) {
                Calendar day = today();
                day.add(Calendar.DATE, dayNumber - 1);
                float pixelsFromZero = y - getCurrectOriginY() - mHeaderHeight
                        - mHeaderRowPadding * 2 - mTimeTextHeight / 2 - mHeaderMarginBottom;
                int hour = (int) (pixelsFromZero / mHourHeight);
                int minute = (int) (60 * (pixelsFromZero - hour * mHourHeight) / mHourHeight);
                day.add(Calendar.HOUR, hour);
                day.set(Calendar.MINUTE, minute);
                return day;
            }
            startPixel += mDayColumnWidth + mColumnGap;
        }
        return null;
    }

    /**
     * Draw all the events of a particular day.
     *
     * @param date           The day.
     * @param startFromPixel The left position of the day area. The events will never go any left from this value.
     * @param canvas         The canvas to draw upon.
     */
    private void drawEvents(Calendar date, float startFromPixel, Canvas canvas) {
        if (mEventRects != null && mEventRects.size() > 0) {
            for (int i = 0; i < mEventRects.size(); i++) {
                if (isSameDay(mEventRects.get(i).event.getStartTime(), date)
                        && !mEventRects.get(i).event.isAllDay()) {
                    drawDay(startFromPixel, canvas, i);
                    EventRect event = mEventRects.get(i);
                    if (event.event.getId() == editId) {
//                        setEditEvent(i);
                        editPos = i;
                    }
                    if (mEventRects.get(i).event.isEditAble()) {
                        mEventRects.get(i).event.setEditAble(false);
                        setEditEvent(i);
                    }
                }
            }
        }
    }

    private void drawDay(float startFromPixel, Canvas canvas, int i) {
        // Calculate top.
        float top = mHourHeight * 24 * mEventRects.get(i).top / 1440 + getCurrectOriginY() + mHeaderHeight + getHeaderTop();

        // Calculate bottom.
        float bottom = mEventRects.get(i).bottom;
        bottom = mHourHeight * 24 * bottom / 1440 + getCurrectOriginY() + getHourTop() - mEventMarginVertical;

        // Calculate left and right.
        float left = startFromPixel + mEventRects.get(i).left * mDayColumnWidth;
        if (left < startFromPixel)
            left += mOverlappingEventGap;
        float right = left + mEventRects.get(i).width * mDayColumnWidth;
        if (right < startFromPixel + mDayColumnWidth)
            right -= mOverlappingEventGap;

        // Draw the event and the event name on top of it.
        if (left < right &&
                left < getWidth() &&
                top < getHeight() &&
                right > mHeaderColumnWidth &&
                bottom > mHeaderHeight + mHeaderRowPadding * 2 + mTimeTextHeight / 2 + mHeaderMarginBottom
                ) {


//            rectMargin = mAllDayEventItemPadding *2;
            mEventRects.get(i).rectF = new RectF(left + rectMargin, top + rectMargin, right - rectMargin, bottom - rectMargin);
            mEventBackgroundPaint.setColor(mEventRects.get(i).event.getColor() == 0 ? mDefaultEventColor : mEventRects.get(i).event.getColor());
            if (isTimeSet() && editPos == i) {
                return;
            }

            if (viewStatue == EDIT_STATUE && editId == mEventRects.get(i).event.getId()) {
                mEventBackgroundPaint.setAlpha(150);
            } else {
                mEventBackgroundPaint.setAlpha(255);
            }
            RectF rectF = mEventRects.get(i).rectF;


            canvas.drawRoundRect(mEventRects.get(i).rectF, mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);
            drawEventTitle(mEventRects.get(i), mEventRects.get(i).event, mEventRects.get(i).rectF, canvas, top, left);
            drawBoarder(canvas, mEventRects.get(i).event, rectF);
        } else {
            mEventRects.get(i).rectF = null;
        }
    }

    private void drawBoarder(Canvas canvas, WeekViewEvent event, RectF rectF) {
        path.reset();
        path.moveTo(rectF.left, rectF.top);
        path.lineTo(rectF.right, rectF.top);
        path.lineTo(rectF.right, rectF.bottom);
        path.lineTo(rectF.left, rectF.bottom);
        path.close();
        mEventBoarderPaint.setColor(event.getmBoarderColor());
        canvas.drawPath(path, mEventBoarderPaint);
    }

    private float getHeaderTop() {
        if (isTimeSet()) {
            return 0;
        }
        return mHeaderRowPadding * 2 + mTimeTextHeight + mHeaderMarginBottom;
    }


    /**
     * Draw all the Allday-events of a particular day.
     *
     * @param date           The day.
     * @param startFromPixel The left position of the day area. The events will never go any left from this value.
     * @param canvas         The canvas to draw upon.
     */
    private void drawAllDayEvents(Calendar date, float startFromPixel, Canvas canvas) {
        if (!containsAllDayEvent) {
            return;
        }

//        if (flingType == ALLDAY_TYPE && touchAllDayItem != null && touchAllDayItem.getDay() != date.getTime().getTime()) {
//            return;
//        }
        ADayItem dayItem = dayMap.obtionItem(date.getTime().getTime());
        float height = mHeaderHeight;
//        float contentHeight = dayItem.getContentHeight();
        if (dayItem.getOriginPoint().y < height - dayItem.getContentHeight())
            dayItem.getOriginPoint().y = height - dayItem.getContentHeight();

        // Don't put an "else if" because it will trigger a glitch when completely zoomed out and
        // scrolling vertically.
        if (dayItem.getOriginPoint().y > 0) {
            dayItem.getOriginPoint().y = 0;
        }
//        allDayHeight = mEventPadding * 2 + mTimeTextHeight;
        float startTop = getHeaderTop();
        allDayTopSum = startTop;
        float dayTop = startTop;
        float dayLeft = startFromPixel;
        float dayRight = dayLeft + mDayColumnWidth;
        float dayBottom = startTop + mHeaderHeight;
        float contentHeight = 0;


        List<EventRect> sortEvents = allDaySortEvents;
        if (sortEvents != null && sortEvents.size() > 0) {
            for (int i = 0; i < sortEvents.size(); i++) {
                EventRect eventRect = sortEvents.get(i);
                if (isSameDay(eventRect.event.getStartTime(), date) && eventRect.event.isAllDay()) {
                    int index = mEventRects.indexOf(eventRect);
                    // Calculate top.
                    float top = allDayTopSum + dayItem.getOriginPoint().y;
                    contentHeight += allDayHeight;
                    // Calculate bottom.
                    float bottom = top + allDayHeight;
                    allDayTopSum = allDayTopSum + allDayHeight;
                    // Calculate left and right.
                    float left = startFromPixel;
                    if (left < startFromPixel)
                        left += mOverlappingEventGap;
                    float right = left + mDayColumnWidth;
                    if (right < startFromPixel + mDayColumnWidth)
                        right -= mOverlappingEventGap;

                    // Draw the event and the event name on top of it.
                    if (left < right &&
                            left < getWidth() &&
                            top < dayBottom &&
                            right > mHeaderColumnWidth &&
                            bottom > 0
                            ) {
                        dayItem.addEvent(mEventRects.get(index));
                        mEventRects.get(index).rectF = new RectF(left + rectMargin, top + rectMargin, right - rectMargin, bottom - rectMargin);
                        mEventBackgroundPaint.setColor(mEventRects.get(index).event.getColor() == 0 ? mDefaultEventColor : mEventRects.get(index).event.getColor());
                        canvas.drawRoundRect(mEventRects.get(index).rectF, mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);
                        drawAllDayEventTitle(mEventRects.get(index).event, mEventRects.get(index).rectF, canvas, top, left);
                        drawBoarder(canvas, mEventRects.get(index).event, mEventRects.get(index).rectF);
                    } else
                        mEventRects.get(index).rectF = null;
                }
            }


            drawTopColumnLine(canvas, dayLeft, dayTop, dayRight, dayBottom);
            dayItem.setRectF(new RectF(dayLeft, dayTop, dayRight, dayBottom)).setContentHeight(contentHeight);
//            canvas.drawRoundRect(dayItem.getRectF(), mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);
            if (!dayItem.isZero()) {
                dayMap.add(date.getTime().getTime(), dayItem);
            }


        }
    }

    private void drawTopColumnLine(Canvas canvas, float dayLeft, float dayTop, float dayRight, float dayBottom) {
        canvas.save();
        canvas.drawLine(dayLeft, dayTop, dayRight, dayTop, mHourSeparatorPaint);
        canvas.drawLine(dayRight, dayTop, dayRight, dayBottom, mHourSeparatorPaint);
        canvas.restore();
    }

    private void drawAllDayEventTitle(WeekViewEvent event, RectF rect, Canvas canvas, float originalTop, float originalLeft) {
//        if (rect.right - rect.left - mEventPadding * 2 < 0) return;
//        if (rect.bottom - rect.top - mEventPadding * 2 < 0) return;
        String bob = EventTextUtils.getEventTitle(event);
        int availableHeight = (int) (rect.bottom - originalTop - mEventPadding * 2);
        int availableWidth = (int) (rect.right - originalLeft - mEventPadding * 2);

        // Get text dimensions.
        StaticLayout textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int lineHeight = textLayout.getHeight() / textLayout.getLineCount();


        if (event.isFinish()) {
            mEventTextPaint.setStrikeThruText(true);
            mEventTextPaint.setColor(event.getmFinishColor());
        } else {
            mEventTextPaint.setColor(event.getmTitleColor());
        }

        bob = EventTextUtils.getEventLineTitle(event);
        textLayout = new StaticLayout(TextUtils.ellipsize(bob, mEventTextPaint, availableWidth, TextUtils.TruncateAt.END), mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(originalLeft + mEventPadding, rect.top + (rect.bottom - rect.top - lineHeight) / 2);
        textLayout.draw(canvas);
        canvas.restore();
        mEventTextPaint.setStrikeThruText(false);
        mEventTextPaint.setColor(mEventTextColor);
//        }
    }


    public static String addAlpha(String originalColor, double alpha) {
        long alphaFixed = Math.round(alpha * 255);
        String alphaHex = Long.toHexString(alphaFixed);
        if (alphaHex.length() == 1) {
            alphaHex = "0" + alphaHex;
        }
        originalColor = originalColor.replace("#", "#" + alphaHex);
        return originalColor;
    }


    public float getCurrectOriginY() {
        return mCurrentOrigin.y;
    }


    /**
     * Draw the name of the event on top of the event rectangle.
     *
     * @param eventRect
     * @param event        The event of which the title (and location) should be drawn.
     * @param rect         The rectangle on which the text is to be drawn.
     * @param canvas       The canvas to draw upon.
     * @param originalTop  The original top position of the rectangle. The rectangle may have some of its portion outside of the visible area.
     * @param originalLeft The original left position of the rectangle. The rectangle may have some of its portion outside of the visible area.
     */
    private void drawEventTitle(EventRect eventRect, WeekViewEvent event, RectF rect, Canvas canvas, float originalTop, float originalLeft) {
//        if (rect.right - rect.left - mEventPadding * 2 < 0) return;
//        if (rect.bottom - rect.top - mEventPadding * 2 < 0) return;
        String bob = EventTextUtils.getEventTitle(event);
        int availableHeight = (int) (rect.bottom - rect.top - mEventPadding * 2);
        int availableWidth = (int) (rect.right - rect.left - mEventPadding * 2);


        if (availableHeight <= 0 || availableWidth <= 0) {
            return;
        }


        // Get text dimensions.
        StaticLayout textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int lineHeight = textLayout.getHeight() / textLayout.getLineCount();


//        绘制新建控件
        if (event.isCreate()) {
//            setViewStatue(CREATE_STATUE);
            createEvent = eventRect;
            drawCreateTitle(event, rect, canvas, availableWidth, lineHeight);
        } else {
            drawEventTitle(event, rect, canvas, originalTop, originalLeft, bob, availableHeight, availableWidth, lineHeight);
        }
    }

    private void setViewStatue(int createStatue) {
        viewStatue = createStatue;
    }

    private void drawEventTitle(WeekViewEvent event, RectF rect, Canvas canvas, float originalTop, float originalLeft, String bob, int availableHeight, int availableWidth, int lineHeight) {
        StaticLayout textLayout;//
        mEventTextPaint.setColor(event.getmTitleColor());
        if (isTimeSet()) {
            bob = TextUtils.ellipsize(bob, mEventTextPaint, availableWidth, TextUtils.TruncateAt.END).toString();//      显示的高度如果大于行高
        }
        if (availableHeight >= lineHeight) {
            // Calculate available number of line counts.
            int availableLineCount = availableHeight / lineHeight;
            if (isTimeSet()) {
                textLayout = new StaticLayout(bob, mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            } else {
                do {
                    // Ellipsize text to fit into event rect.
                    textLayout = new StaticLayout(TextUtils.ellipsize(bob, mEventTextPaint, availableLineCount * availableWidth, TextUtils.TruncateAt.END), mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    // Reduce line count.
                    availableLineCount--;
                    // Repeat until text is short enough.
                } while (textLayout.getHeight() > availableHeight);

            }
            // Draw text.
            canvas.save();
            canvas.translate(originalLeft + mEventPadding, originalTop + mEventPadding);
            textLayout.draw(canvas);
            canvas.restore();
        } else {
            if (!isTimeSet()) {
                return;
            }
            bob = EventTextUtils.getEventLineTitle(event);
            textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            originalTop = originalTop - textLayout.getHeight();
            canvas.save();
            canvas.translate(originalLeft + mEventPadding, originalTop);
            textLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawCreateTitle(WeekViewEvent event, RectF rect, Canvas canvas, int availableWidth, int lineHeight) {
        Bitmap addIcon = BitmapFactory.decodeResource(getResources(), addIconReference);
        int bW = addIcon.getWidth();
        int bH = addIcon.getHeight();
        float scale = add_icon_rect * 1.0f / bW;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        addIcon = Bitmap.createBitmap(addIcon, 0, 0, bW, bH, matrix, true);
        canvas.drawBitmap(addIcon, rect.left + (rect.right - rect.left - addIcon.getWidth()) / 2, rect.top + (rect.bottom - rect.top - addIcon.getHeight()) / 2, mCreateEventPaint);
//        Matrix matrix = new Matrix();
//        canvas.drawBitmap(addIcon, matrix, mCreateEventPaint);

//        String bob;
//        StaticLayout textLayout;
//        bob = EventTextUtils.getEventLineTitle(event);
//        bob = TextUtils.ellipsize(bob, mEventTextPaint, availableWidth, TextUtils.TruncateAt.END).toString();//      显示的高度如果大于行高
//        textLayout = new StaticLayout(bob, mCreateEventPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//        int width = (int) mCreateEventPaint.measureText(bob);
//        int height = lineHeight;
//        canvas.save();
//        canvas.translate(rect.left + (rect.right - rect.left - width) / 2, rect.top + (rect.bottom - rect.top - height) / 2);
//        textLayout.draw(canvas);
//        canvas.restore();
    }


    /**
     * A class to hold reference to the events and their visual representation. An EventRect is
     * actually the rectangle that is drawn on the calendar for a given event. There may be more
     * than one rectangle for a single event (an event that expands more than one day). In that
     * case two instances of the EventRect will be used for a single event. The given event will be
     * stored in "originalEvent". But the event that corresponds to rectangle the rectangle
     * instance will be stored in "event".
     */
    public class EventRect implements Cloneable {
        public WeekViewEvent event;
        public WeekViewEvent originalEvent;
        public RectF rectF;
        public float left;
        public float width;
        public float top;
        public float bottom;

        /**
         * Create a new instance of event rect. An EventRect is actually the rectangle that is drawn
         * on the calendar for a given event. There may be more than one rectangle for a single
         * event (an event that expands more than one day). In that case two instances of the
         * EventRect will be used for a single event. The given event will be stored in
         * "originalEvent". But the event that corresponds to rectangle the rectangle instance will
         * be stored in "event".
         *
         * @param event         Represents the event which this instance of rectangle represents.
         * @param originalEvent The original event that was passed by the user.
         * @param rectF         The rectangle.
         */
        public EventRect(WeekViewEvent event, WeekViewEvent originalEvent, RectF rectF) {
            this.event = event;
            this.rectF = rectF;
            this.originalEvent = originalEvent;
        }


        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }


    /**
     * Gets more events of one/more month(s) if necessary. This method is called when the user is
     * scrolling the week view. The week view stores the events of three months: the visible month,
     * the previous month, the next month.
     *
     * @param day The day where the user is currently is.
     */
    private void getMoreEvents(Calendar day) {

        // Get more events if the month is changed.
        if (mEventRects == null)
            mEventRects = new ArrayList<EventRect>();

        if (allDaySortEvents == null)
            allDaySortEvents = new ArrayList<EventRect>();
        if (mWeekViewLoader == null && !isInEditMode())
            throw new IllegalStateException("You must provide a MonthChangeListener");

        // If a refresh was requested then reset some variables.
        if (mRefreshEvents) {
            mEventRects.clear();
            allDaySortEvents.clear();
            mPreviousPeriodEvents = null;
            mCurrentPeriodEvents = null;
            mNextPeriodEvents = null;
            mFetchedPeriod = -1;
        }

        if (mWeekViewLoader != null) {
            int periodToFetch = (int) mWeekViewLoader.toWeekViewPeriodIndex(day);
            if (!isInEditMode() && (mFetchedPeriod < 0 || mFetchedPeriod != periodToFetch || mRefreshEvents)) {
                List<? extends WeekViewEvent> previousPeriodEvents = null;
                List<? extends WeekViewEvent> currentPeriodEvents = null;
                List<? extends WeekViewEvent> nextPeriodEvents = null;

                if (mPreviousPeriodEvents != null && mCurrentPeriodEvents != null && mNextPeriodEvents != null) {
                    if (periodToFetch == mFetchedPeriod - 1) {
                        currentPeriodEvents = mPreviousPeriodEvents;
                        nextPeriodEvents = mCurrentPeriodEvents;
                    } else if (periodToFetch == mFetchedPeriod) {
                        previousPeriodEvents = mPreviousPeriodEvents;
                        currentPeriodEvents = mCurrentPeriodEvents;
                        nextPeriodEvents = mNextPeriodEvents;
                    } else if (periodToFetch == mFetchedPeriod + 1) {
                        previousPeriodEvents = mCurrentPeriodEvents;
                        currentPeriodEvents = mNextPeriodEvents;
                    }
                }
                if (currentPeriodEvents == null)
                    currentPeriodEvents = mWeekViewLoader.onLoad(periodToFetch);


                if (!isTimeSet()) {
                    if (previousPeriodEvents == null)
                        previousPeriodEvents = mWeekViewLoader.onLoad(periodToFetch - 1);
                    if (nextPeriodEvents == null)
                        nextPeriodEvents = mWeekViewLoader.onLoad(periodToFetch + 1);
                } else {
                    previousPeriodEvents = new ArrayList<>();
                    nextPeriodEvents = new ArrayList<>();
                }


                // Clear events.
                mEventRects.clear();
                allDaySortEvents.clear();
                sortAndCacheEvents(previousPeriodEvents);
                sortAndCacheEvents(currentPeriodEvents);
                sortAndCacheEvents(nextPeriodEvents);
                calculateHeaderHeight();

                mPreviousPeriodEvents = previousPeriodEvents;
                mCurrentPeriodEvents = currentPeriodEvents;
                mNextPeriodEvents = nextPeriodEvents;
                mFetchedPeriod = periodToFetch;
            }
        }
        calculatePositionsEvents();
        sortEventAllDay();
    }


    public interface SortAllDayEvents {
        List<EventRect> sort(List<EventRect> lists);
    }


    private SortAllDayEvents sortAllDayEvents;

    public void setSortAllDayEvents(SortAllDayEvents sortEvents) {
        this.sortAllDayEvents = sortEvents;
    }

    private void sortEventAllDay() {
        List<EventRect> tempEvents = mEventRects;
        if (sortAllDayEvents != null) {
            allDaySortEvents = sortAllDayEvents.sort(tempEvents);
        }
    }

    private void calculatePositionsEvents() {
        // Prepare to calculate positions of each events.
        List<EventRect> tempEvents = mEventRects;
//        if (comparator != null){
//            Collections.sort(tempEvents, comparator);
//        }

        mEventRects = new ArrayList<EventRect>();

        // Iterate through each day with events to calculate the position of the events.
        while (tempEvents.size() > 0) {
            ArrayList<EventRect> eventRects = new ArrayList<>(tempEvents.size());

            // Get first event for a day.
            EventRect eventRect1 = tempEvents.remove(0);
            eventRects.add(eventRect1);

            int i = 0;
            while (i < tempEvents.size()) {
                // Collect all other events for same day.
                EventRect eventRect2 = tempEvents.get(i);
                if (isSameDay(eventRect1.event.getStartTime(), eventRect2.event.getStartTime())) {
                    tempEvents.remove(i);
                    eventRects.add(eventRect2);
                } else {
                    i++;
                }
            }
            computePositionOfEvents(eventRects);
        }
    }

    /**
     * Cache the event for smooth scrolling functionality.
     *
     * @param event The event to cache.
     */
    private void cacheEvent(WeekViewEvent event) {
        int hour = event.getEndTime().get(Calendar.HOUR_OF_DAY);
        int minute = event.getEndTime().get(Calendar.MINUTE);
        if (event.getStartTime().compareTo(event.getEndTime()) >= 0 && !(hour == 0 && minute == 0))
            return;
        List<WeekViewEvent> splitedEvents = event.splitWeekViewEvents();
        for (WeekViewEvent splitedEvent : splitedEvents) {
            mEventRects.add(new EventRect(splitedEvent, event, null));
        }
    }

    /**
     * Sort and cache events.
     *
     * @param events The events to be sorted and cached.
     */
    private void sortAndCacheEvents(List<? extends WeekViewEvent> events) {
        sortEvents(events);
        for (WeekViewEvent event : events) {
            cacheEvent(event);
        }
    }


    private Comparator comparator;


    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Sorts the events in ascending order.
     *
     * @param events The events to be sorted.
     */
    private void sortEvents(List<? extends WeekViewEvent> events) {
        Comparator comparator = new Comparator<WeekViewEvent>() {
            @Override
            public int compare(WeekViewEvent event1, WeekViewEvent event2) {
                long start1 = event1.getStartTime().getTimeInMillis();
                long start2 = event2.getStartTime().getTimeInMillis();
                int comparator = start1 > start2 ? 1 : (start1 < start2 ? -1 : 0);
                if (comparator == 0) {
                    long end1 = event1.getEndTime().getTimeInMillis();
                    long end2 = event2.getEndTime().getTimeInMillis();
                    comparator = end1 > end2 ? 1 : (end1 < end2 ? -1 : 0);
                }
                return comparator;
            }
        };
        Collections.sort(events, comparator);
    }

    /**
     * Calculates the left and right positions of each events. This comes handy specially if events
     * are overlapping.
     *
     * @param eventRects The events along with their wrapper class.
     */
    private void computePositionOfEvents(List<EventRect> eventRects) {
        // Make "collision groups" for all events that collide with others.
        List<List<EventRect>> collisionGroups = new ArrayList<List<EventRect>>();
        for (EventRect eventRect : eventRects) {
            boolean isPlaced = false;

            outerLoop:
            for (List<EventRect> collisionGroup : collisionGroups) {
                for (EventRect groupEvent : collisionGroup) {
                    if (isEventsCollide(groupEvent.event, eventRect.event) && groupEvent.event.isAllDay() == eventRect.event.isAllDay()) {
                        collisionGroup.add(eventRect);
                        isPlaced = true;
                        break outerLoop;
                    }
                }
            }

            if (!isPlaced) {
                List<EventRect> newGroup = new ArrayList<EventRect>();
                newGroup.add(eventRect);
                collisionGroups.add(newGroup);
            }
        }

        for (List<EventRect> collisionGroup : collisionGroups) {
            expandEventsToMaxWidth(collisionGroup);
        }
    }

    /**
     * Expands all the events to maximum possible width. The events will try to occupy maximum
     * space available horizontally.
     *
     * @param collisionGroup The group of events which overlap with each other.
     */
    private void expandEventsToMaxWidth(List<EventRect> collisionGroup) {
        // Expand the events to maximum possible width.
        List<List<EventRect>> columns = new ArrayList<List<EventRect>>();
        columns.add(new ArrayList<EventRect>());
        for (EventRect eventRect : collisionGroup) {
            boolean isPlaced = false;
            for (List<EventRect> column : columns) {
                if (column.size() == 0) {
                    column.add(eventRect);
                    isPlaced = true;
                } else if (!isEventsCollide(eventRect.event, column.get(column.size() - 1).event)) {
                    column.add(eventRect);
                    isPlaced = true;
                    break;
                }
            }
            if (!isPlaced) {
                List<EventRect> newColumn = new ArrayList<EventRect>();
                newColumn.add(eventRect);
                columns.add(newColumn);
            }
        }


        // Calculate left and right position for all the events.
        // Get the maxRowCount by looking in all columns.
        int maxRowCount = 0;
        for (List<EventRect> column : columns) {
            maxRowCount = Math.max(maxRowCount, column.size());
        }
        for (int i = 0; i < maxRowCount; i++) {
            // Set the left and right values of the event.
            float j = 0;
            for (List<EventRect> column : columns) {
                if (column.size() >= i + 1) {
                    EventRect eventRect = column.get(i);
                    eventRect.width = 1f / columns.size();
                    eventRect.left = j / columns.size();
                    rectMeasure(eventRect);
                    mEventRects.add(eventRect);
                }
                j++;
            }
        }
    }

    private void rectMeasure(EventRect eventRect) {
        if (!eventRect.event.isAllDay()) {
            eventRect.top = eventRect.event.getStartTime().get(Calendar.HOUR_OF_DAY) * 60 + eventRect.event.getStartTime().get(Calendar.MINUTE);
            eventRect.bottom = eventRect.event.getEndTime().get(Calendar.HOUR_OF_DAY) * 60 + eventRect.event.getEndTime().get(Calendar.MINUTE);
        } else {
            eventRect.top = 0;
            eventRect.bottom = mAllDayEventHeight;
        }
    }


    /**
     * Checks if two events overlap.
     *
     * @param event1 The first event.
     * @param event2 The second event.
     * @return true if the events overlap.
     */
    private boolean isEventsCollide(WeekViewEvent event1, WeekViewEvent event2) {
        long start1 = event1.getStartTime().getTimeInMillis();
        long end1 = event1.getEndTime().getTimeInMillis();
        long start2 = event2.getStartTime().getTimeInMillis();
        long end2 = event2.getEndTime().getTimeInMillis();
        return !((start1 >= end2) || (end1 <= start2));
    }


    /**
     * Checks if time1 occurs after (or at the same time) time2.
     *
     * @param time1 The time to check.
     * @param time2 The time to check against.
     * @return true if time1 and time2 are equal or if time1 is after time2. Otherwise false.
     */
    private boolean isTimeAfterOrEquals(Calendar time1, Calendar time2) {
        return !(time1 == null || time2 == null) && time1.getTimeInMillis() >= time2.getTimeInMillis();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        mAreDimensionsInvalid = true;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to setting and getting the properties.
    //
    /////////////////////////////////////////////////////////////////

    public void setOnEventClickListener(EventClickListener listener) {
        this.mEventClickListener = listener;
    }

    public EventClickListener getEventClickListener() {
        return mEventClickListener;
    }

    public
    @Nullable
    MonthLoader.MonthChangeListener getMonthChangeListener() {
        if (mWeekViewLoader instanceof MonthLoader)
            return ((MonthLoader) mWeekViewLoader).getOnMonthChangeListener();
        return null;
    }

    public void setMonthChangeListener(MonthLoader.MonthChangeListener monthChangeListener) {
        this.mWeekViewLoader = new MonthLoader(monthChangeListener);
    }

    /**
     * Get event loader in the week view. Event loaders define the  interval after which the events
     * are loaded in week view. For a MonthLoader events are loaded for every month. You can define
     * your custom event loader by extending WeekViewLoader.
     *
     * @return The event loader.
     */
    public WeekViewLoader getWeekViewLoader() {
        return mWeekViewLoader;
    }

    /**
     * Set event loader in the week view. For example, a MonthLoader. Event loaders define the
     * interval after which the events are loaded in week view. For a MonthLoader events are loaded
     * for every month. You can define your custom event loader by extending WeekViewLoader.
     *
     * @param loader The event loader.
     */
    public void setWeekViewLoader(WeekViewLoader loader) {
        this.mWeekViewLoader = loader;
    }

    public EventLongPressListener getEventLongPressListener() {
        return mEventLongPressListener;
    }

    public void setEventLongPressListener(EventLongPressListener eventLongPressListener) {
        this.mEventLongPressListener = eventLongPressListener;
    }

    public void setEmptyViewClickListener(EmptyViewClickListener emptyViewClickListener) {
        this.mEmptyViewClickListener = emptyViewClickListener;
    }

    public EmptyViewClickListener getEmptyViewClickListener() {
        return mEmptyViewClickListener;
    }

    public void setEmptyViewLongPressListener(EmptyViewLongPressListener emptyViewLongPressListener) {
        this.mEmptyViewLongPressListener = emptyViewLongPressListener;
    }

    public EmptyViewLongPressListener getEmptyViewLongPressListener() {
        return mEmptyViewLongPressListener;
    }

    public void setScrollListener(ScrollListener scrolledListener) {
        this.mScrollListener = scrolledListener;
    }

    public ScrollListener getScrollListener() {
        return mScrollListener;
    }

    /**
     * Get the interpreter which provides the text to show in the header column and the header row.
     *
     * @return The date, time interpreter.
     */
    public DateTimeInterpreter getDateTimeInterpreter() {
        if (mDateTimeInterpreter == null) {
            mDateTimeInterpreter = new DateTimeInterpreter() {
                @Override
                public String interpretDate(Calendar date) {
                    try {
                        SimpleDateFormat sdf = mDayNameLength == LENGTH_SHORT ? new SimpleDateFormat("EEEEE M/dd", Locale.getDefault()) : new SimpleDateFormat("EEE M/dd", Locale.getDefault());
                        return sdf.format(date.getTime()).toUpperCase();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public String interpretWeek(Calendar date) {
                    return "";
                }

                @Override
                public String interpretTime(int hour) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, 0);

                    try {
                        SimpleDateFormat sdf = DateFormat.is24HourFormat(getContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                        return sdf.format(calendar.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            };
        }
        return mDateTimeInterpreter;
    }

    /**
     * Set the interpreter which provides the text to show in the header column and the header row.
     *
     * @param dateTimeInterpreter The date, time interpreter.
     */
    public void setDateTimeInterpreter(DateTimeInterpreter dateTimeInterpreter) {
        this.mDateTimeInterpreter = dateTimeInterpreter;

        // Refresh time column width.
        initTextTimeWidth();
    }


    /**
     * Get the number of visible days in a week.
     *
     * @return The number of visible days in a week.
     */
    public int getNumberOfVisibleDays() {
        return mNumberOfVisibleDays;
    }

    /**
     * Set the number of visible days in a week.
     *
     * @param numberOfVisibleDays The number of visible days in a week.
     */
    public void setNumberOfVisibleDays(int numberOfVisibleDays) {
        this.mNumberOfVisibleDays = numberOfVisibleDays;
        mCurrentOrigin.x = 0;
        mCurrentOrigin.y = 0;
        invalidate();
    }

    public int getHourHeight() {
        return mHourHeight;
    }

    public void setHourHeight(int hourHeight) {
        mNewHourHeight = hourHeight;
        invalidate();
    }

    public int getColumnGap() {
        return mColumnGap;
    }

    public void setColumnGap(int columnGap) {
        mColumnGap = columnGap;
        invalidate();
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * Set the first day of the week. First day of the week is used only when the week view is first
     * drawn. It does not of any effect after user starts scrolling horizontally.
     * <p>
     * <b>Note:</b> This method will only work if the week view is set to display more than 6 days at
     * once.
     * </p>
     *
     * @param firstDayOfWeek The supported values are {@link java.util.Calendar#SUNDAY},
     *                       {@link java.util.Calendar#MONDAY}, {@link java.util.Calendar#TUESDAY},
     *                       {@link java.util.Calendar#WEDNESDAY}, {@link java.util.Calendar#THURSDAY},
     *                       {@link java.util.Calendar#FRIDAY}.
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        invalidate();
    }

    public boolean isShowFirstDayOfWeekFirst() {
        return mShowFirstDayOfWeekFirst;
    }

    public void setShowFirstDayOfWeekFirst(boolean show) {
        mShowFirstDayOfWeekFirst = show;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mHeaderTextPaint.setTextSize(mTextSize);
        mTimeTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public int getHeaderColumnPadding() {
        return mHeaderColumnPadding;
    }

    public void setHeaderColumnPadding(int headerColumnPadding) {
        mHeaderColumnPadding = headerColumnPadding;
        invalidate();
    }

    public int getHeaderColumnTextColor() {
        return mHeaderColumnTextColor;
    }

    public void setHeaderColumnTextColor(int headerColumnTextColor) {
        mHeaderColumnTextColor = headerColumnTextColor;
        mHeaderTextPaint.setColor(mHeaderColumnTextColor);
        mTimeTextPaint.setColor(mHeaderColumnTextColor);
        invalidate();
    }

    public int getHeaderRowPadding() {
        return mHeaderRowPadding;
    }

    public void setHeaderRowPadding(int headerRowPadding) {
        mHeaderRowPadding = headerRowPadding;
        invalidate();
    }

    public int getHeaderRowBackgroundColor() {
        return mHeaderRowBackgroundColor;
    }

    public void setHeaderRowBackgroundColor(int headerRowBackgroundColor) {
        mHeaderRowBackgroundColor = headerRowBackgroundColor;
        mHeaderBackgroundPaint.setColor(mHeaderRowBackgroundColor);
        invalidate();
    }

    public int getDayBackgroundColor() {
        return mDayBackgroundColor;
    }

    public void setDayBackgroundColor(int dayBackgroundColor) {
        mDayBackgroundColor = dayBackgroundColor;
        mDayBackgroundPaint.setColor(mDayBackgroundColor);
        invalidate();
    }

    public int getHourSeparatorColor() {
        return mHourSeparatorColor;
    }

    public void setHourSeparatorColor(int hourSeparatorColor) {
        mHourSeparatorColor = hourSeparatorColor;
        mHourSeparatorPaint.setColor(mHourSeparatorColor);
        invalidate();
    }

    public int getTodayBackgroundColor() {
        return mTodayBackgroundColor;
    }

    public void setTodayBackgroundColor(int todayBackgroundColor) {
        mTodayBackgroundColor = todayBackgroundColor;
        mTodayBackgroundPaint.setColor(mTodayBackgroundColor);
        invalidate();
    }

    public int getHourSeparatorHeight() {
        return mHourSeparatorHeight;
    }

    public void setHourSeparatorHeight(int hourSeparatorHeight) {
        mHourSeparatorHeight = hourSeparatorHeight;
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorHeight);
        invalidate();
    }

    public int getTodayHeaderTextColor() {
        return mTodayHeaderTextColor;
    }

    public void setTodayHeaderTextColor(int todayHeaderTextColor) {
        mTodayHeaderTextColor = todayHeaderTextColor;
        mTodayHeaderTextPaint.setColor(mTodayHeaderTextColor);
        invalidate();
    }

    public int getEventTextSize() {
        return mEventTextSize;
    }


    public EventRect getEventRect(int pos) {
        return mEventRects.get(pos);
    }

    public void setEventTextSize(int eventTextSize) {
        mEventTextSize = eventTextSize;
        mEventTextPaint.setTextSize(mEventTextSize);
        invalidate();
    }

    public int getEventTextColor() {
        return mEventTextColor;
    }

    public void setEventTextColor(int eventTextColor) {
        mEventTextColor = eventTextColor;
        mEventTextPaint.setColor(mEventTextColor);
        invalidate();
    }

    public int getEventPadding() {
        return mEventPadding;
    }

    public void setEventPadding(int eventPadding) {
        mEventPadding = eventPadding;
        invalidate();
    }

    public int getHeaderColumnBackgroundColor() {
        return mHeaderColumnBackgroundColor;
    }

    public void setHeaderColumnBackgroundColor(int headerColumnBackgroundColor) {
        mHeaderColumnBackgroundColor = headerColumnBackgroundColor;
        mHeaderColumnBackgroundPaint.setColor(mHeaderColumnBackgroundColor);
        invalidate();
    }

    public int getDefaultEventColor() {
        return mDefaultEventColor;
    }

    public void setDefaultEventColor(int defaultEventColor) {
        mDefaultEventColor = defaultEventColor;
        invalidate();
    }

    /**
     * <b>Note:</b> Use {@link #setDateTimeInterpreter(DateTimeInterpreter)} and
     * {@link #getDateTimeInterpreter()} instead.
     *
     * @return Either long or short day name is being used.
     */
    @Deprecated
    public int getDayNameLength() {
        return mDayNameLength;
    }

    /**
     * Set the length of the day name displayed in the header row. Example of short day names is
     * 'M' for 'Monday' and example of long day names is 'Mon' for 'Monday'.
     * <p>
     * <b>Note:</b> Use {@link #setDateTimeInterpreter(DateTimeInterpreter)} instead.
     * </p>
     *
     * @param length Supported values are {@link com.alamkanak.weekview.WeekView#LENGTH_SHORT} and
     *               {@link com.alamkanak.weekview.WeekView#LENGTH_LONG}.
     */
    @Deprecated
    public void setDayNameLength(int length) {
        if (length != LENGTH_LONG && length != LENGTH_SHORT) {
            throw new IllegalArgumentException("length parameter must be either LENGTH_LONG or LENGTH_SHORT");
        }
        this.mDayNameLength = length;
    }

    public int getOverlappingEventGap() {
        return mOverlappingEventGap;
    }

    /**
     * Set the gap between overlapping events.
     *
     * @param overlappingEventGap The gap between overlapping events.
     */
    public void setOverlappingEventGap(int overlappingEventGap) {
        this.mOverlappingEventGap = overlappingEventGap;
        invalidate();
    }

    public int getEventCornerRadius() {
        return mEventCornerRadius;
    }

    /**
     * Set corner radius for event rect.
     *
     * @param eventCornerRadius the radius in px.
     */
    public void setEventCornerRadius(int eventCornerRadius) {
        mEventCornerRadius = eventCornerRadius;
    }

    public int getEventMarginVertical() {
        return mEventMarginVertical;
    }

    /**
     * Set the top and bottom margin of the event. The event will release this margin from the top
     * and bottom edge. This margin is useful for differentiation consecutive events.
     *
     * @param eventMarginVertical The top and bottom margin.
     */
    public void setEventMarginVertical(int eventMarginVertical) {
        this.mEventMarginVertical = eventMarginVertical;
        invalidate();
    }

    /**
     * Returns the first visible day in the week view.
     *
     * @return The first visible day in the week view.
     */
    public Calendar getFirstVisibleDay() {
        return mFirstVisibleDay;
    }

    /**
     * Returns the last visible day in the week view.
     *
     * @return The last visible day in the week view.
     */
    public Calendar getLastVisibleDay() {
        return mLastVisibleDay;
    }

    /**
     * Get the scrolling speed factor in horizontal direction.
     *
     * @return The speed factor in horizontal direction.
     */
    public float getXScrollingSpeed() {
        return mXScrollingSpeed;
    }

    /**
     * Sets the speed for horizontal scrolling.
     *
     * @param xScrollingSpeed The new horizontal scrolling speed.
     */
    public void setXScrollingSpeed(float xScrollingSpeed) {
        this.mXScrollingSpeed = xScrollingSpeed;
    }

    /**
     * Whether weekends should have a background color different from the normal day background
     * color. The weekend background colors are defined by the attributes
     * `futureWeekendBackgroundColor` and `pastWeekendBackgroundColor`.
     *
     * @return True if weekends should have different background colors.
     */
    public boolean isShowDistinctWeekendColor() {
        return mShowDistinctWeekendColor;
    }

    /**
     * Set whether weekends should have a background color different from the normal day background
     * color. The weekend background colors are defined by the attributes
     * `futureWeekendBackgroundColor` and `pastWeekendBackgroundColor`.
     *
     * @param showDistinctWeekendColor True if weekends should have different background colors.
     */
    public void setShowDistinctWeekendColor(boolean showDistinctWeekendColor) {
        this.mShowDistinctWeekendColor = showDistinctWeekendColor;
        invalidate();
    }

    /**
     * Whether past and future days should have two different background colors. The past and
     * future day colors are defined by the attributes `futureBackgroundColor` and
     * `pastBackgroundColor`.
     *
     * @return True if past and future days should have two different background colors.
     */
    public boolean isShowDistinctPastFutureColor() {
        return mShowDistinctPastFutureColor;
    }

    /**
     * Set whether weekends should have a background color different from the normal day background
     * color. The past and future day colors are defined by the attributes `futureBackgroundColor`
     * and `pastBackgroundColor`.
     *
     * @param showDistinctPastFutureColor True if past and future should have two different
     *                                    background colors.
     */
    public void setShowDistinctPastFutureColor(boolean showDistinctPastFutureColor) {
        this.mShowDistinctPastFutureColor = showDistinctPastFutureColor;
        invalidate();
    }

    /**
     * Get whether "now" line should be displayed. "Now" line is defined by the attributes
     * `nowLineColor` and `nowLineThickness`.
     *
     * @return True if "now" line should be displayed.
     */
    public boolean isShowNowLine() {
        return mShowNowLine;
    }

    /**
     * Set whether "now" line should be displayed. "Now" line is defined by the attributes
     * `nowLineColor` and `nowLineThickness`.
     *
     * @param showNowLine True if "now" line should be displayed.
     */
    public void setShowNowLine(boolean showNowLine) {
        this.mShowNowLine = showNowLine;
        invalidate();
    }

    /**
     * Get the "now" line color.
     *
     * @return The color of the "now" line.
     */
    public int getNowLineColor() {
        return mNowLineColor;
    }

    /**
     * Set the "now" line color.
     *
     * @param nowLineColor The color of the "now" line.
     */
    public void setNowLineColor(int nowLineColor) {
        this.mNowLineColor = nowLineColor;
        invalidate();
    }

    /**
     * Get the "now" line thickness.
     *
     * @return The thickness of the "now" line.
     */
    public int getNowLineThickness() {
        return mNowLineThickness;
    }

    /**
     * Set the "now" line thickness.
     *
     * @param nowLineThickness The thickness of the "now" line.
     */
    public void setNowLineThickness(int nowLineThickness) {
        this.mNowLineThickness = nowLineThickness;
        invalidate();
    }

    /**
     * Get whether the week view should fling horizontally.
     *
     * @return True if the week view has horizontal fling enabled.
     */
    public boolean isHorizontalFlingEnabled() {
        return mHorizontalFlingEnabled;
    }

    /**
     * Set whether the week view should fling horizontally.
     *
     * @return True if it should have horizontal fling enabled.
     */
    public void setHorizontalFlingEnabled(boolean enabled) {
        mHorizontalFlingEnabled = enabled;
    }

    /**
     * Get whether the week view should fling vertically.
     *
     * @return True if the week view has vertical fling enabled.
     */
    public boolean isVerticalFlingEnabled() {
        return mVerticalFlingEnabled;
    }

    /**
     * Set whether the week view should fling vertically.
     *
     * @return True if it should have vertical fling enabled.
     */
    public void setVerticalFlingEnabled(boolean enabled) {
        mVerticalFlingEnabled = enabled;
    }

    /**
     * Get the height of AllDay-events.
     *
     * @return Height of AllDay-events.
     */
    public int getAllDayEventHeight() {
        return mAllDayEventHeight;
    }

    /**
     * Set the height of AllDay-events.
     */
    public void setAllDayEventHeight(int height) {
        mAllDayEventHeight = height;
    }

    /**
     * Get scroll duration
     *
     * @return scroll duration
     */
    public int getScrollDuration() {
        return mScrollDuration;
    }

    /**
     * Set the scroll duration
     */
    public void setScrollDuration(int scrollDuration) {
        mScrollDuration = scrollDuration;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to scrolling.
    //
    /////////////////////////////////////////////////////////////////


    private boolean isTouchAllDay(MotionEvent event) {
        boolean result = false;
        for (ADayItem item : dayMap.values()) {
            RectF rect = item.getRectF();
            if (rect != null && rect.contains(event.getX(), event.getY())) {
                touchAllDayItem = item;
                result = true;
                break;
            }
        }
        return result;
    }

    private void goToNearestOrigin() {
        double leftDays = mCurrentOrigin.x / (mDayColumnWidth + mColumnGap);

        if (mCurrentFlingDirection != Direction.NONE) {
            // snap to nearest day
            leftDays = Math.round(leftDays);
        } else if (mCurrentScrollDirection == Direction.LEFT) {
            // snap to last day
            leftDays = Math.floor(leftDays);
        } else if (mCurrentScrollDirection == Direction.RIGHT) {
            // snap to next day
            leftDays = Math.ceil(leftDays);
        } else {
            // snap to nearest day
            leftDays = Math.round(leftDays);
        }

        int nearestOrigin = (int) (mCurrentOrigin.x - leftDays * (mDayColumnWidth + mColumnGap));

        if (nearestOrigin != 0) {
            // Stop current animation.
            mScroller.forceFinished(true);
            // Snap to date.
            mScroller.startScroll((int) mCurrentOrigin.x, (int) getCurrectOriginY(), -nearestOrigin, 0, (int) (Math.abs(nearestOrigin) / mDayColumnWidth * mScrollDuration));
            ViewCompat.postInvalidateOnAnimation(WeekView.this);
        }
        // Reset scrolling and fling direction.
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
    }


    /**
     * Check if scrolling should be stopped.
     *
     * @return true if scrolling should be stopped before reaching the end of animation.
     */
    private boolean forceFinishScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // current velocity only available since api 14
            return mScroller.getCurrVelocity() <= mMinimumFlingVelocity;
        } else {
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    //
    //      Public methods.
    //
    /////////////////////////////////////////////////////////////////

    /**
     * Show today on the week view.
     */
    public void goToToday() {
        Calendar today = Calendar.getInstance();
        goToDate(today);
    }

    /**
     * Show a specific day on the week view.
     *
     * @param date The date to show.
     */
    public void goToDate(Calendar date) {
        mScroller.forceFinished(true);
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        if (mAreDimensionsInvalid) {
            mScrollToDay = date;
            return;
        }

        mRefreshEvents = true;

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long day = 1000L * 60L * 60L * 24L;
        long dateInMillis = date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis());
        long todayInMillis = today.getTimeInMillis() + today.getTimeZone().getOffset(today.getTimeInMillis());
        long dateDifference = (dateInMillis / day) - (todayInMillis / day);
        mCurrentOrigin.x = -dateDifference * (mDayColumnWidth + mColumnGap);
        invalidate();
    }

    /**
     * Refreshes the view and loads the events again.
     */
    public void notifyDatasetChanged() {
        mRefreshEvents = true;
//        calculatePositionsEvents();
//        invalidate();
        ViewCompat.postInvalidateOnAnimation(WeekView.this);
    }


    /**
     * Vertically scroll to a specific hour in the week view.
     *
     * @param hour The hour to scroll to in 24-hour format. Supported values are 0-24.
     */
    public void goToHour(double hour) {
        if (mAreDimensionsInvalid) {
            mScrollToHour = hour;
            return;
        }

        int verticalOffset = 0;
        if (hour > 24)
            verticalOffset = mHourHeight * 24;
        else if (hour > 0)
            verticalOffset = (int) (mHourHeight * hour);

        if (verticalOffset > mHourHeight * 24 - getHeight() + mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom)
            verticalOffset = (int) (mHourHeight * 24 - getHeight() + mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom);

//        mCurrentOrigin.y = -verticalOffset;
        setCurrentOriginY(-verticalOffset);
        invalidate();
    }


    private void setCurrentOriginY(float y) {
        mCurrentOrigin.y = y;
    }

    /**
     * Get the first hour that is visible on the screen.
     *
     * @return The first hour that is visible.
     */
    public double getFirstVisibleHour() {
        return -getCurrectOriginY() / mHourHeight;
    }


/////////////////////////////////////////////////////////////////
//
//      Interfaces.
//
/////////////////////////////////////////////////////////////////

    public interface EventClickListener {
        /**
         * Triggered when clicked on one existing event
         *
         * @param event     :     event clicked.
         * @param eventRect : view containing the clicked event.
         */
        void onEventClick(EventRect event, RectF eventRect);
    }


    public interface OutCreateClickListener {

        void onOutCreateEventClick(EventRect createEvent);
    }

    public interface EventLongPressListener {
        /**
         * Similar to {@link com.alamkanak.weekview.WeekView.EventClickListener} but with a long press.
         *
         * @param e
         */
        void onEventLongPress(MotionEvent e, int pos);

//        void onEventLongPress(WeekViewEvent event, RectF eventRect);

    }


    public interface EventEditListener {
        void onUpListener(int pos, float topSum, float bottomSum);

        void onDragingListener(int editPos, float topSum, float bottomSum);
    }


    public interface EmptyViewClickListener {
        /**
         * Triggered when the users clicks on a empty space of the calendar.
         *
         * @param time              : {@link Calendar} object set with the date and time of the clicked position on the view.
         * @param pastEventEditAble
         */
        void onEmptyViewClicked(Calendar time, boolean pastEventEditAble);
    }

    public interface EmptyViewLongPressListener {
        /**
         * Similar to {@link com.alamkanak.weekview.WeekView.EmptyViewClickListener} but with long press.
         *
         * @param time: {@link Calendar} object set with the date and time of the long pressed position on the view.
         */
        void onEmptyViewLongPress(Calendar time);
    }

    public interface ScrollListener {
        /**
         * Called when the first visible day has changed.
         * <p>
         * (this will also be called during the first draw of the weekview)
         *
         * @param newFirstVisibleDay The new first visible day
         * @param oldFirstVisibleDay The old first visible day (is null on the first call).
         */
        void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay);

    }


    public boolean isRereshAble() {
        if (flingType == ALLDAY_TYPE || viewStatue == EDIT_STATUE) {
            return false;
        }


        return getCurrectOriginY() == 0;
    }


}
