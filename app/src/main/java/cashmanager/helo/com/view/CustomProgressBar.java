package cashmanager.helo.com.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cashmanager.helo.com.R;

@SuppressLint({"DrawAllocation", "HandlerLeak"})
public class CustomProgressBar extends View {

    public static int BIG = 64;

    public static int MEDIUM = 48;

    public static int SMALL = 32;

    public interface OnDiagramComplete {
        public void onDiagramComplete(float sum, int[] colors);
    }

    private final double ONE_PERCENT_IN_DEGREES = 3.6;

    private int mainColor;
    private int borderColor;
    private int backgroundColor;

    private int startGradientColor;
    private int endGradientColor;

    private int textColor;

    private int speed;
    private int increment;

    private float padding;

    private int centerX;
    private int centerY;

    private float width;
    private float textSize;

    private float currentProgress;
    private float progressToUpdate;

    private float outsideRadius;
    private float insideRadius;
    private float middleRadius;

    private Paint borderPaint;
    private Paint backgroundPaint;
    private Paint mainPaint;
    private Paint textPaint;

    private boolean isSpinning;
    private boolean isGradientAvailable;
    private boolean isShowProgress;
    private boolean isUpdateProgress;
    private boolean isDiagram;

    private String text;

    private RectF outside;
    private RectF middle;
    private RectF inside;

    private float[] diagramAngles;
    private int[] colors;
    private float sum;

    private OnDiagramComplete diagramComplete;

    private Handler spinHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if (isSpinning) {
                currentProgress += increment;
                if (currentProgress > 360) {
                    currentProgress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, speed);
            } else if (isUpdateProgress) {
                if (currentProgress < progressToUpdate) {
                    currentProgress += increment;
                    spinHandler.sendEmptyMessageDelayed(0, speed);
                } else {
                    isUpdateProgress = false;
                }
            }
        }
    };

    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onDraw(Canvas canvas) {

        if (!isDiagram) {
            canvas.drawArc(outside, 0, 360, false, borderPaint);
            canvas.drawArc(inside, 0, 360, false, borderPaint);
            canvas.drawArc(middle, 0, 360, false, backgroundPaint);

            if (isSpinning) {
                canvas.drawArc(middle, currentProgress, 50, false, mainPaint);
            } else {
                int currentProgress = (int) (this.currentProgress * ONE_PERCENT_IN_DEGREES);
                canvas.drawArc(middle, 0, currentProgress, false, mainPaint);
                if (isShowProgress) {
                    canvas.drawText(Integer.toString((int) (currentProgress / ONE_PERCENT_IN_DEGREES)), centerX, centerY + 5, textPaint);
                }
            }
            if (text != null) canvas.drawText(text, centerX, centerY + textSize / 2, textPaint);
        } else {
            if (diagramAngles != null) {
                for (int i = 0; i < diagramAngles.length; i++) {
                    if(colors[i] == 0){
                        colors[i] = getRandomColor();
                    }
                    backgroundPaint.setColor(colors[i]);
                    if (i == 0) {
                        canvas.drawArc(middle, 0, diagramAngles[i] + 2, false, backgroundPaint);
                    } else {
                        canvas.drawArc(middle, diagramAngles[i - 1], Math.abs(diagramAngles[i] - diagramAngles[i - 1]) + 2, false, backgroundPaint);
                    }
                }
                diagramComplete.onDiagramComplete(sum, colors);
                sum = 0;
            }
        }
        super.onDraw(canvas);
    }

    public void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setProgress(int progress) {
        isSpinning = false;
        currentProgress = progress;
        invalidate();
    }

    public void updateProgressTo(int progress) {
        isSpinning = false;
        isUpdateProgress = true;
        progressToUpdate = progress;
        spinHandler.sendEmptyMessage(0);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    public void setColor(int color) {
        mainColor = color;
    }

    public void setTextSize(int size) {
        textSize = size;
    }

    public void setRadius(int radius) {
        outsideRadius = radius;
        width = outsideRadius / 5;
        insideRadius = outsideRadius - width * 2;
        middleRadius = outsideRadius - width;
    }

    public void setWidth(int width) {
        this.width = width;
        insideRadius = outsideRadius - width * 2;
        middleRadius = outsideRadius - width;
    }

    public void setOutSideColor(int color) {
        borderPaint.setColor(color);
    }

    public void setMiddleColor(int color) {
        borderPaint.setColor(color);
    }

    private void setUpPaints() {


        backgroundPaint = new Paint();
        backgroundPaint.setDither(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(width * 2);
        backgroundPaint.setAntiAlias(true);

        mainPaint = new Paint();
        mainPaint.setDither(true);
        if (isGradientAvailable) {
            RadialGradient gradient = new RadialGradient(centerX, centerY, outsideRadius, startGradientColor, endGradientColor, android.graphics.Shader.TileMode.MIRROR);
            mainPaint.setShader(gradient);
        } else {
            mainPaint.setColor(mainColor);
        }

        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setStrokeWidth(width * 2);
        mainPaint.setAntiAlias(true);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(mainColor);
        borderPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setTextSize(outsideRadius - middleRadius);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
    }

    public void setGradient(RadialGradient gradient) {
        mainPaint.setShader(gradient);
    }

    public void setGradient(LinearGradient gradient) {
        mainPaint.setShader(gradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int viewWidth;
        int viewHeight;
        int desiredWidth = getDesiredWidth();
        int desiredHeight = getDesiredHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            viewWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            viewWidth = Math.min(desiredWidth, widthSize);
        } else {
            viewWidth = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            viewHeight = Math.min(desiredHeight, heightSize);
        } else {
            viewHeight = desiredHeight;
        }

        centerX = viewWidth / 2;
        centerY = viewHeight / 2;

        setUpPaints();
        initArcs();
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private int getDesiredWidth() {
        return (int) (outsideRadius * 2 + padding);
    }

    private int getDesiredHeight() {
        return (int) (outsideRadius * 2 + padding);
    }

    private void initArcs() {
        outside = new RectF(centerX - outsideRadius, centerY - outsideRadius, centerX + outsideRadius, centerY + outsideRadius);
        middle = new RectF(centerX - middleRadius, centerY - middleRadius, centerX + middleRadius, centerY + middleRadius);
        inside = new RectF(centerX - insideRadius, centerY - insideRadius, centerX + insideRadius, centerY + insideRadius);
    }

    public CustomProgressBar(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public enum ProgressBarStyle {
        SMALL, MEDIUM, BIG
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomProgressBar,
                0, 0);

        try {
            isSpinning = a.getBoolean(R.styleable.CustomProgressBar_isSpinning, true);
            isDiagram = a.getBoolean(R.styleable.CustomProgressBar_isDiagram, false);

            outsideRadius = a.getDimension(R.styleable.CustomProgressBar_radius, MEDIUM);
            width = a.getDimension(R.styleable.CustomProgressBar_width, outsideRadius / 5);

            insideRadius = outsideRadius - width * 2;
            middleRadius = outsideRadius - width;

            textSize = a.getDimension(R.styleable.CustomProgressBar_textSize, width * 2);

            padding = a.getDimension(R.styleable.CustomProgressBar_padding, 2);

            mainColor = a.getColor(R.styleable.CustomProgressBar_progressColor, Color.GRAY);
            backgroundColor = a.getColor(R.styleable.CustomProgressBar_backgroundColor, Color.TRANSPARENT);
            borderColor = a.getColor(R.styleable.CustomProgressBar_borderColor, Color.GRAY);
            textColor = a.getColor(R.styleable.CustomProgressBar_textColor, Color.DKGRAY);

            startGradientColor = a.getColor(R.styleable.CustomProgressBar_startGradientColor, Color.BLACK);
            endGradientColor = a.getColor(R.styleable.CustomProgressBar_endGradientColor, mainColor);

            text = a.getString(R.styleable.CustomProgressBar_text);

            speed = a.getInteger(R.styleable.CustomProgressBar_speed, 30);
            increment = a.getInteger(R.styleable.CustomProgressBar_increment, 1);

            isShowProgress = a.getBoolean(R.styleable.CustomProgressBar_isShowProgress, false);
            isGradientAvailable = a.getBoolean(R.styleable.CustomProgressBar_isShowProgress, true);

            spinHandler.sendEmptyMessage(0);

        } finally {
            a.recycle();
        }
    }

    public void setDiagramParams(List<Float> integers, OnDiagramComplete diagramComplete) {
        this.diagramComplete = diagramComplete;
        isDiagram = true;
        isSpinning = false;
        diagramAngles = new float[integers.size()];
        colors = new int[integers.size()];
        for (Float number : integers) {
            sum += number;
        }
        for (int i = 0; i < integers.size(); i++) {
            if (i == 0) {
                diagramAngles[i] = (float) ((integers.get(i) * 100.0 / sum) * ONE_PERCENT_IN_DEGREES);
            } else {
                diagramAngles[i] = (float) ((integers.get(i) * 100.0 / sum) * ONE_PERCENT_IN_DEGREES + diagramAngles[i - 1]);
            }
        }
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(128) + 128, rnd.nextInt(128) + 128, rnd.nextInt(128) + 128);
    }
}