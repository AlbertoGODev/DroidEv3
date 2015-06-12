package com.guerritastudio.albertogarcia.droidev3.widget;

/**
 * Created by AlbertoGarcia on 6/5/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.guerritastudio.albertogarcia.droidev3.R;

public class JoystickView extends View implements Runnable {
    private static final String TAG = JoystickView.class.getSimpleName();
    private final double RAD = 57.2957795D;
    public static final long DEFAULT_LOOP_INTERVAL = 100L;
    public static final int FRONT = 3;
    public static final int FRONT_RIGHT = 2;
    public static final int RIGHT = 1;
    public static final int RIGHT_BOTTOM = 8;
    public static final int BOTTOM = 7;
    public static final int BOTTOM_LEFT = 6;
    public static final int LEFT = 5;
    public static final int LEFT_FRONT = 4;
    private JoystickView.OnJoystickMoveListener onJoystickMoveListener;
    private Thread thread = new Thread(this);
    private long loopInterval = 100L;
    private int xPosition = 0;
    private int yPosition = 0;
    private double centerX = 0.0D;
    private double centerY = 0.0D;
    private Paint mainCircle;
    private Paint secondaryCircle;
    private Paint button;
    private Paint horizontalLine;
    private Paint verticalLine;
    private int joystickRadius;
    private int buttonRadius;
    private int lastAngle = 0;
    private int lastPower = 0;

    public JoystickView(Context context) {
        super(context);
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initJoyTest();
    }

    public JoystickView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        this.initJoyTest();
    }

    protected void initJoyTest() {
        Log.d(TAG, "initJoystick");
        this.mainCircle = new Paint(1);
        this.mainCircle.setColor(getResources().getColor(R.color.White));
        this.mainCircle.setStyle(Style.FILL_AND_STROKE);
        this.secondaryCircle = new Paint();
        this.secondaryCircle.setColor(getResources().getColor(R.color.CadetBlue));
        this.secondaryCircle.setStyle(Style.STROKE);
        this.verticalLine = new Paint();
        this.verticalLine.setStrokeWidth(2.0F);
        this.verticalLine.setColor(getResources().getColor(R.color.Black));
        this.horizontalLine = new Paint();
        this.horizontalLine.setStrokeWidth(2.0F);
        this.horizontalLine.setColor(getResources().getColor(R.color.Black));
        this.button = new Paint(1);
        this.button.setColor(getResources().getColor(R.color.SteelBlue));
        this.button.setStyle(Style.FILL);
    }

    protected void onFinishInflate() {
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "onMeasure()");
        int d = Math.min(this.measure(widthMeasureSpec), this.measure(heightMeasureSpec));
        this.setMeasuredDimension(d, d);
        this.xPosition = this.getWidth() / 2;//200 Note2
        this.yPosition = this.getWidth() / 2;//200 Note2
        this.buttonRadius = (int) ((double) (d / 2) * 0.25D);
        this.joystickRadius = (int) ((double) (d / 2) * 0.75D);
    }

    private int measure(int measureSpec) {
        //Log.d(TAG, "measure()");
        boolean result = false;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result1;
        if (specMode == 0) {
            result1 = 200;
        } else {
            result1 = specSize;
        }

        return result1;
    }

    protected void onDraw(Canvas canvas) {
        //Log.d(TAG, "onDraw()");
        this.centerX = (double) (this.getWidth() / 2);
        this.centerY = (double) (this.getHeight() / 2);
        //Log.d(TAG, "onDraw() centerX = "+centerX);
        //Log.d(TAG, "onDraw() centerY = "+centerY);
        canvas.drawCircle((float) ((int) this.centerX), (float) ((int) this.centerY), (float) this.joystickRadius, this.mainCircle);
        canvas.drawCircle((float) ((int) this.centerX), (float) ((int) this.centerY), (float) (this.joystickRadius / 2), this.secondaryCircle);
        canvas.drawLine((float) this.centerX, (float) this.centerY, (float) this.centerX, (float) (this.centerY - (double) this.joystickRadius), this.verticalLine);
        canvas.drawLine((float) (this.centerX - (double) this.joystickRadius), (float) this.centerY, (float) (this.centerX + (double) this.joystickRadius), (float) this.centerY, this.horizontalLine);
        canvas.drawLine((float) this.centerX, (float) (this.centerY + (double) this.joystickRadius), (float) this.centerX, (float) this.centerY, this.horizontalLine);
        canvas.drawCircle((float) this.xPosition, (float) this.yPosition, (float) this.buttonRadius, this.button);
    }

    public boolean onTouchEvent(MotionEvent event) {
        //Log.d(TAG, "onTouchEvent()");
        this.xPosition = (int) event.getX();
        this.yPosition = (int) event.getY();
        double abs = Math.sqrt(((double) this.xPosition - this.centerX) * ((double) this.xPosition - this.centerX) + ((double) this.yPosition - this.centerY) * ((double) this.yPosition - this.centerY));
        if (abs > (double) this.joystickRadius) {
            this.xPosition = (int) (((double) this.xPosition - this.centerX) * (double) this.joystickRadius / abs + this.centerX);
            this.yPosition = (int) (((double) this.yPosition - this.centerY) * (double) this.joystickRadius / abs + this.centerY);
        }

        this.invalidate();
        if (event.getAction() == 1) {
            this.xPosition = (int) this.centerX;
            this.yPosition = (int) this.centerY;
            this.thread.interrupt();
            this.onJoystickMoveListener.onValueChanged(this.getAngle(), this.getPower(), this.getDirection());
        }

        if (this.onJoystickMoveListener != null && event.getAction() == 0) {
            if (this.thread != null && this.thread.isAlive()) {
                this.thread.interrupt();
            }

            this.thread = new Thread(this);
            this.thread.start();
            this.onJoystickMoveListener.onValueChanged(this.getAngle(), this.getPower(), this.getDirection());
        }

        return true;
    }

    private int getAngle() {
        return (double) this.xPosition > this.centerX ? ((double) this.yPosition < this.centerY ? (this.lastAngle = (int) (Math.atan(((double) this.yPosition - this.centerY) / ((double) this.xPosition - this.centerX)) * 57.2957795D + 90.0D)) : ((double) this.yPosition > this.centerY ? (this.lastAngle = (int) (Math.atan(((double) this.yPosition - this.centerY) / ((double) this.xPosition - this.centerX)) * 57.2957795D) + 90) : (this.lastAngle = 90))) : ((double) this.xPosition < this.centerX ? ((double) this.yPosition < this.centerY ? (this.lastAngle = (int) (Math.atan(((double) this.yPosition - this.centerY) / ((double) this.xPosition - this.centerX)) * 57.2957795D - 90.0D)) : ((double) this.yPosition > this.centerY ? (this.lastAngle = (int) (Math.atan(((double) this.yPosition - this.centerY) / ((double) this.xPosition - this.centerX)) * 57.2957795D) - 90) : (this.lastAngle = -90))) : ((double) this.yPosition <= this.centerY ? (this.lastAngle = 0) : (this.lastAngle < 0 ? (this.lastAngle = -180) : (this.lastAngle = 180))));
    }

    private int getPower() {
        return (int) (100.0D * Math.sqrt(((double) this.xPosition - this.centerX) * ((double) this.xPosition - this.centerX) + ((double) this.yPosition - this.centerY) * ((double) this.yPosition - this.centerY)) / (double) this.joystickRadius);
    }

    private int getDirection() {
        if (this.lastPower == 0 && this.lastAngle == 0) {
            return 0;
        } else {
            int a = 0;
            if (this.lastAngle <= 0) {
                a = this.lastAngle * -1 + 90;
            } else if (this.lastAngle > 0) {
                if (this.lastAngle <= 90) {
                    a = 90 - this.lastAngle;
                } else {
                    a = 360 - (this.lastAngle - 90);
                }
            }

            int direction = (a + 22) / 45 + 1;
            if (direction > 8) {
                direction = 1;
            }

            return direction;
        }
    }

    public void setOnJoystickMoveListener(JoystickView.OnJoystickMoveListener listener, long repeatInterval) {
        this.onJoystickMoveListener = listener;
        this.loopInterval = repeatInterval;
    }

    public void run() {
        while (true) {
            if (!Thread.interrupted()) {
                this.post(new Runnable() {
                    public void run() {
                        JoystickView.this.onJoystickMoveListener.onValueChanged(JoystickView.this.getAngle(), JoystickView.this.getPower(), JoystickView.this.getDirection());
                    }
                });

                try {
                    Thread.sleep(this.loopInterval);
                    continue;
                } catch (InterruptedException var2) {
                    ;
                }
            }

            return;
        }
    }

    public interface OnJoystickMoveListener {
        void onValueChanged(int var1, int var2, int var3);
    }
}