package com.smlab.mytictactoe.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.smlab.mytictactoe.R;
import com.smlab.mytictactoe.TicTacToePage;
import com.smlab.mytictactoe.utils.ScaleListenerExperimental;

public class BoardViewExperimental extends View {
	private static final String TAG = BoardViewExperimental.class.getSimpleName();

	private Bitmap oBitmap;
	private Bitmap xBitmap;
	private Paint linesPaint;
	private Paint backgroundPaint;
	private Paint winLinePaint;
	private int boardWidth;
	private int boardHeight;
	private int cellWidth;

	private TicTacToePage game;

	/**
	 * Initial fling velocity for pan operations, in screen widths (or heights) per second.
	 *
	 * @see #panLeft()
	 * @see #panRight()
	 * @see #panUp()
	 * @see #panDown()
	 */
	private static final float PAN_VELOCITY_FACTOR = 2f;

	/**
	 * The scaling factor for a single zoom 'step'.
	 *
	 * @see #zoomIn()
	 * @see #zoomOut()
	 */
	private static final float ZOOM_AMOUNT = 0.25f;

	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = -1;

	private ScaleGestureDetector mScaleDetector;
	public float mScaleFactor = 1.f;
	private float mLastTouchX;
	private float mLastTouchY;
	private float mPosX;
	private float mPosY;
	private float length;
	private boolean wasMoved = false;
	private boolean wasScaled = false;
	private Rect clipBounds_canvas;
	public static boolean canMove = false;
	private final Rect selRect = new Rect();
	private int selX;
	private int selY;
	private float constX;
	private float constY;
//	private int boardLength;
	public int boardX;
	public int boardY;

	public BoardViewExperimental(Context context) {
		super(context);
		init();
	}

	public BoardViewExperimental(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BoardViewExperimental(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi") 
	public BoardViewExperimental(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void setGame(TicTacToePage game) {
		this.game = game;
	}

	public void init() {
		//		android.graphics.BitmapFactory.Options options = new Options();
		//		options.inScaled = false;
		//		options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
		//		oBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o, options);
		//		xBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x, options);
		oBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o);
		xBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x);

		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(getResources().getColor(R.color.white));

		linesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linesPaint.setColor(getResources().getColor(R.color.blue));
		linesPaint.setStrokeWidth(1.5f);

		winLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		winLinePaint.setColor(getResources().getColor(R.color.fioletiviy));
		winLinePaint.setStrokeWidth(3f);
		//		paint.setARGB(255, 250, 128, 4);
		//		paint.setStrokeJoin(Paint.Join.ROUND);

		setFocusable(true);
		setFocusableInTouchMode(true);
		
		mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListenerExperimental(this));
	}

	private void drawBackground(Canvas canvas) {
//		canvas.drawRect(0.0F, 0.0F, boardWidth, boardHeight, backgroundPaint);
		canvas.drawRect(0.0F, 0.0F, length, length, backgroundPaint);
	}

	private void drawBoard(Canvas canvas) {
		canvas.drawRect(boardX, boardY, (float)boardX + length, (float)boardY + length, backgroundPaint);
		
		int cellsCount = TicTacToePage.cellsCount;
		cellWidth = boardWidth / cellsCount;
		for (int i = 1; i <= cellsCount; i++) {
			canvas.drawLine(cellWidth * i, 0, cellWidth * i, boardHeight, linesPaint);
			canvas.drawLine(0, cellWidth * i, boardWidth, cellWidth * i, linesPaint);
		}
	}

	private void drawOXs(Canvas canvas) {
		int[][] table = TicTacToePage.table;

		/*********** stub ***************/
		//		table[0][0] = 2;
		//		table[0][1] = 1;
		//		table[0][2] = 1;
		//		table[0][3] = 1;
		//		table[0][4] = 1;
		//		table[0][5] = 2;
		//		table[1][5] = 2;
		//		table[2][6] = 2;
		//		table[3][7] = 2;
		//		table[4][8] = 2;
		//		table[5][9] = 2;
		/*********** END stub ***************/

		int boardSize = table.length;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (table[i][j] == 1 || table[i][j] == 2) {
					drawPoint(canvas, i, j, table[i][j]);
				}
			}
		}
	}

	private void drawPoint(Canvas canvas, int i, int j, int gamerId) {
		int left = j * cellWidth;
		int top = i * cellWidth;
		int right = left + cellWidth;
		int bottom = top + cellWidth;

		if (gamerId == 1) {
			canvas.drawBitmap(oBitmap, null, new Rect(left, top, right, bottom), null);
		} else {
			canvas.drawBitmap(xBitmap, null, new Rect(left, top, right, bottom), null);
		}
	}

	private void drawWinLine(Canvas canvas, int i, int j, int k, int l) {
		int left = j * cellWidth;
		int top = i * cellWidth;
		int right = (l + 1) * cellWidth;
		int bottom = (k + 1) * cellWidth;
		canvas.drawLine(left, top, right, bottom, winLinePaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		clipBounds_canvas = canvas.getClipBounds();
		rebound(canvas);
		
		canvas.translate(mPosX, mPosY);
		canvas.scale(mScaleFactor, mScaleFactor);

		boardWidth = getWidth();
		boardHeight = getHeight();

		drawBackground(canvas);
		drawBoard(canvas);
		if (!canMove) {
			invalidate();
		}
		drawOXs(canvas);
//		drawWinLine(canvas, 1, 5, 5, 9);
	}

	public void rebound(Canvas canvas) {
		mPosX = 0.0F;
		mPosY = 0.0F;

		int i = (int)(-mScaleFactor * length + (float)getWidth());
		int j = (int)(-mScaleFactor * length + (float)getHeight());
		
		if (i >= 0) mPosX = Math.min(0, Math.max(i, mPosX));
		if (j >= 0) mPosY = Math.min(0, Math.max(j, mPosY));

		Log.i(TAG, "ReBound i:" + i + " j:" + j + " mPosX:" + mPosX + " mPosY:" + mPosY);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i(TAG, "OnSizeChanged: w:" + w + " h:" + h + " oldW:" + oldw + " oldH:" + oldh);
		length = Math.min(w, h);
		mPosX = (float)(-getWidth()) / 1.7F;
		mPosY = (float)(-getWidth()) / 1.7F;
		getRect(selX, selY, selRect);
		constX = w / 50;
		constY = h / 50;
	}
	private void getRect(int paramInt1, int paramInt2, Rect paramRect) {
		paramRect.set((int)(paramInt1 * this.length / TicTacToePage.cellsCount + 1), (int)(paramInt2 * this.length / TicTacToePage.cellsCount), (int)((paramInt1 + 1) * this.length / TicTacToePage.cellsCount), (int)((paramInt2 + 1) * this.length / TicTacToePage.cellsCount));
	}
	 
	public void rebound() {
		int i = (int)(-mScaleFactor * length + (float)getWidth());
		int j = (int)(-mScaleFactor * length + (float)getHeight());
		if (i >= 0) {
			mPosX = Math.min(0, Math.max(i, mPosX));
		} else {
			mPosX = 0.0F;
		} if (j >= 0) {
			mPosY = Math.min(0, Math.max(j, mPosY));
			return;
		} else {
			mPosY = 0.0F;
			return;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean scaleHandled = mScaleDetector.onTouchEvent(event);

		//		if(event.getAction() == MotionEvent.ACTION_DOWN) {
		//			float xPos = event.getX();
		//			float yPos = event.getY();
		//			Log.i(TAG, "xPos:" + xPos + " yPos:" + yPos);
		//
		////			int x = (int)((xPos / boardWidth) * TicTacToePage.cellsCount);
		////			int y = (int)((yPos / boardHeight) * TicTacToePage.cellsCount);
		//			int x = (int)((xPos / (boardWidth / mScaleFactor)) * TicTacToePage.cellsCount);
		//			int y = (int)((yPos / (boardHeight / mScaleFactor)) * TicTacToePage.cellsCount);
		//
		//			if(x < 0 || x > (boardWidth - 1) || y < 0 || y > (boardHeight - 1)) {
		//				Log.i(TAG, "Invalid positions i=" + x + " j=" + y);
		//			} else {
		//				Log.i(TAG, "i=" + x + " j=" + y + " scaleHandled:" + scaleHandled);
		////				game.movePoint(y, x, 1);
		//				game.movePoint(x, y, 1);
		//			}
		//		}
		//		return true;
		//		return super.onTouchEvent(event);

		final int action = MotionEventCompat.getActionMasked(event); 

		switch (action) { 
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event); 
			final float x = MotionEventCompat.getX(event, pointerIndex); 
			final float y = MotionEventCompat.getY(event, pointerIndex); 

			// Remember where we started (for dragging)
			mLastTouchX = x;
			mLastTouchY = y;
			// Save the ID of this pointer (for dragging)
			mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// Find the index of the active pointer and fetch its position
			final int pointerIndex =  MotionEventCompat.findPointerIndex(event, mActivePointerId);  

			final float x = MotionEventCompat.getX(event, pointerIndex);
			final float y = MotionEventCompat.getY(event, pointerIndex);

			// Calculate the distance moved
			final float dx = x - mLastTouchX;
			final float dy = y - mLastTouchY;

			mPosX += dx;
			mPosY += dy;

			invalidate();

			// Remember this touch position for the next move event
			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = -1;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = -1;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event); 
			final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex); 

			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = MotionEventCompat.getX(event, newPointerIndex); 
				mLastTouchY = MotionEventCompat.getY(event, newPointerIndex); 
				mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
			}
			break;
		}
		}       
		return true;
	}
}
