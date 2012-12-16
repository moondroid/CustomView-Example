package it.moondroid.customview.example;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

	private static final String DEFAULT_TEXT = "Hello Big World!";
	private static final float DEFAULT_TEXT_SIZE = 24.0f;

	private static final int DEFAULT_BOX_BORDER_COLOR = Color.BLUE;
	private static final float DEFAULT_BOX_RADIUS = 10.0f;
	private static final float DEFAULT_BOX_WIDTH = 0.0f;

	
	
	private Paint mTextPaint;
	private Paint boundsPaint;

	private String text = DEFAULT_TEXT;
	private float textSize = DEFAULT_TEXT_SIZE;
	private float realTextSize; // scaled text size (density independent)
		
	private float boxRadius = DEFAULT_BOX_RADIUS;
	private float realBoxRadius;
	
	private int boxColor = DEFAULT_BOX_BORDER_COLOR;
	private float boxWidth = DEFAULT_BOX_WIDTH;
	
	private float textWidth;
	
	// Constructor required for in-code creation
	public CustomView(Context context) {
		super(context);

		init();
	}

	// Constructor required for inflation from resource file
	public CustomView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);

		readAttributes(context, attrs);
		init();
	}

	// Constructor required for inflation from resource file
	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);

		readAttributes(context, attrs);
		init();
	}

	// Read Attributes from the xml
	// custom attributes are defined in attrs.xml as declare-styleable resources	
	private void readAttributes (Context context, AttributeSet attrs){
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
		
		text = ta.getString(R.styleable.CustomView_text);
		if (text==null){ text = DEFAULT_TEXT; }
		textSize = ta.getFloat(R.styleable.CustomView_textSize, DEFAULT_TEXT_SIZE);
		boxColor = ta.getInt(R.styleable.CustomView_borderColor, DEFAULT_BOX_BORDER_COLOR);
		boxRadius = ta.getFloat(R.styleable.CustomView_borderRadius, DEFAULT_BOX_RADIUS);
		
		//Don't forget this
	    ta.recycle();
	}
	
	// The onMeasure method is called when the control’s parent is laying out
	// its child controls
	@Override
	protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {

		int measuredWidth = measureWidth(wMeasureSpec);
		int measuredHeight = measureHeight(hMeasureSpec);
		// MUST make this call to setMeasuredDimension
		// or you will cause a runtime exception when
		// the control is laid out.
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	// [ ... Calculate the view width ... ]
	private int measureWidth(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified (specMode ==
		// MeasureSpec.UNSPECIFIED)
		int result = 100;

		if (specMode == MeasureSpec.AT_MOST) {
			// Calculate the ideal size of your
			// control within this maximum size.
			// If your control fills the available
			// space return the outer bound.
			if (specSize > textWidth) {
				result = (int) (textWidth + 0.5f);
			} else {
				result = specSize;
			}

			Log.d("measureWidth", "MeasureSpec.AT_MOST " + specSize);

		} else if (specMode == MeasureSpec.EXACTLY) {
			
			result = specSize;
			Log.d("measureWidth", "MeasureSpec.EXACTLY " + specSize);
		}
		return result;

	}

	// [ ... Calculate the view height ... ]
	private int measureHeight(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified (specMode == //
		// MeasureSpec.UNSPECIFIED)
		int result = 100;

		if (specMode == MeasureSpec.AT_MOST) {
			// Calculate the ideal size of your
			// control within this maximum size.
			// If your control fills the available
			// space return the outer bound.

			// result = specSize;
			result = (int) (realTextSize + 0.5f);
			Log.d("measureHeight", "MeasureSpec.AT_MOST");
		} else if (specMode == MeasureSpec.EXACTLY) {
			// If your control can fit within these bounds return that value.
			result = specSize;
			Log.d("measureHeight", "MeasureSpec.EXACTLY");
		}
		return result;

	}

	// For efficiency reasons the creation of new objects (particularly
	// instances of Paint and Drawable)
	// should be done in the views’s constructor
	private void init() {

				
		// Create the text paint brush.
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// Set the paint color and text size
		mTextPaint.setColor(Color.BLACK);

		realTextSize = densityMultiplier(textSize);
		mTextPaint.setTextSize(realTextSize);

		textWidth = mTextPaint.measureText(text);

		// Create the bounding box paint brush.
		boundsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		boundsPaint.setColor(boxColor);
		boundsPaint.setStyle(Paint.Style.STROKE);
		boundsPaint.setStrokeWidth(boxWidth);

		realBoxRadius = densityMultiplier(boxRadius);

		Log.d("init", "realTextSize: " + realTextSize);
	}

	private float densityMultiplier(float desiredPx) {
		final float densityMultiplier = getContext().getResources()
				.getDisplayMetrics().density;
		final float scaledPx = desiredPx * densityMultiplier;

		return scaledPx;
	}

	// The onDraw method is where to draw your visual interface
	@Override
	protected void onDraw(Canvas canvas) {

		Log.d("onDraw", "ONDRAW CALLED");
		
		// Get the size of the control based on the last call to onMeasure.
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		// Find the center
		float px = width / 2.0f;
		float py = height / 2.0f;

		// Measure the width of the text string.
		// float textWidth = mTextPaint.measureText(TEXT);

		// Draw the text string in the center of the control.
		float textX = px - textWidth / 2.0f;
		float textY = py + realTextSize / 2.0f - mTextPaint.descent();
		canvas.drawText(text, textX, textY, mTextPaint);

		// Draw the bounding rectangle
		// canvas.drawRect(0, 0, width-0.5f, height-0.5f, boundsPaint);
		canvas.drawRoundRect(new RectF(0, 0, width - 0.5f, height - 0.5f),
				realBoxRadius, realBoxRadius, boundsPaint);
	}

	
	/**
	 * Sets the string value of the view.
	 * @param text the string to write
	 */
	public void setText(String text) {
		this.text = text;
		textWidth = mTextPaint.measureText(text);
		
		
		//force re-calculating the layout dimension and the redraw of the view
		requestLayout();
		invalidate();
	}

	/**
	 * Return the text the view is displaying.
	 * @return the string value of the view
	 */
	public String getText(){
		return text;
	}
	
	
	/**
	 * Sets the text size of the view.
	 * @param size of the text in sp
	 */
	public void setTextSize (float size){
		textSize = size;
		realTextSize = densityMultiplier(textSize);
		mTextPaint.setTextSize(realTextSize);
		textWidth = mTextPaint.measureText(text);
		
		//force re-calculating the layout dimension and the redraw of the view
		requestLayout();
		invalidate();
	}
	
	/**
	 * Return the text size of the view.
	 * @return the text size in sp
	 */
	public float getTextSize(){
		return textSize;
	}
	
	
	// override onTouchEvent to handle touch events on the custom view
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// Get the type of action this event represents
		int action = event.getAction();
		
		switch (action){
		
		
		
		case MotionEvent.ACTION_DOWN:
			// Touch screen pressed
			float initX = event.getX();
			float initY = event.getY();
			return true;
		
		case MotionEvent.ACTION_MOVE:
			// Contact has moved across screen
			float x = event.getX();
			float y = event.getY();
			return true;
			
		case MotionEvent.ACTION_UP:
			// Touch screen touch ended
			float finalX = event.getX();
			float finalY = event.getY();
			return true;
		
		case (MotionEvent.ACTION_CANCEL):
			// Touch event cancelled
			return true;
		
		case (MotionEvent.ACTION_OUTSIDE):
			// Movement has occurred outside the
			// bounds of the current screen element
			return true;
		}
		
		
		// return false if the event is not handled, true otherwise
		return false;
	}
}
