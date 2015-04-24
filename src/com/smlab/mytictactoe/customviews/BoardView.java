package com.smlab.mytictactoe.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.smlab.mytictactoe.R;
import com.smlab.mytictactoe.TicTacToePage;
import com.smlab.mytictactoe.utils.ScaleListener;

public class BoardView extends View {
	private static final String TAG = BoardView.class.getSimpleName();

	private Bitmap oBitmap;
	private Bitmap xBitmap;
	private Paint linesPaint;
	private Paint backgroundPaint;
	private Paint winLinePaint;
	private int boardWidth;
	private int boardHeight;
	private int cellWidth;
	private boolean firstTime = false;

	private TicTacToePage game;

	public BoardView(Context context) {
		super(context);
		init();
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi") 
	public BoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void setGame(TicTacToePage game) {
		this.game = game;
	}

	public void init() {
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
		
		mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener(this));
		firstTime = true;
	}

	private void drawBackground(Canvas canvas) {
		canvas.drawRect(0.0F, 0.0F, boardWidth, boardHeight, backgroundPaint);
	}

	private void drawBoard(Canvas canvas) {
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

		canvas.save();
		if (firstTime) {
			firstTime = false;
			matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
			matrix.postTranslate(0, 0);
			prevPos.x = getLeft();
			prevPos.y = getTop();
			prevPos2.x = getLeft();
			prevPos2.y = getTop();
		} else {
			canvas.setMatrix(matrix);
		}
	    
		boardWidth = getWidth();
		boardHeight = getHeight();

		drawBackground(canvas);
		drawBoard(canvas);
		drawOXs(canvas);
//		drawWinLine(canvas, 1, 5, 5, 9);
		
		canvas.restore();
	}

	//	@Override
	//	public boolean onTouchEvent(MotionEvent event) {
	//		if(event.getAction() == MotionEvent.ACTION_DOWN) {
	//			float xPos = event.getX();
	//			float yPos = event.getY();
	//			Log.i(TAG, "xPos:" + xPos + " yPos:" + yPos);
	//
	//			int x = (int)((xPos / boardWidth) * TicTacToePage.cellsCount);
	//			int y = (int)((yPos / boardHeight) * TicTacToePage.cellsCount);
	//
	//			if(x < 0 || x > (boardWidth - 1) || y < 0 || y > (boardHeight - 1)) {
	//				Log.i(TAG, "Invalid positions i=" + x + " j=" + y);
	//			} else {
	//				Log.i(TAG, "i=" + x + " j=" + y);
	//				game.movePoint(x, y, 1);
	//			}
	//		}
	//		return true;
	//	}

	private PointF prevPos = new PointF(), prevPos2 = new PointF();
	float scale = 1.0f;
	final float MIN_SCALE = 1.0f, MAX_SCALE = 2.0f;
//	float rotation = 0;
	Matrix matrix = new Matrix();
	private float prevDist = 0f;
	
	private ScaleGestureDetector mScaleDetector;
	public float mScaleFactor = 1.f;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "getPointerCount:" + event.getPointerCount() + " actionMove:" + (event.getAction() == MotionEvent.ACTION_MOVE));
		mScaleDetector.onTouchEvent(event);
		if (event.getPointerCount() == 2) {
			float d = dist(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
			float pivotX = (event.getX(0) + event.getX(1)) / 2;
			float pivotY = (event.getY(0) + event.getY(1)) / 2;
			float prevPivotX = (prevPos.x + prevPos2.x) / 2;
			float prevPivotY = (prevPos.y + prevPos2.y) / 2;
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				
				float newScale = scale * d / prevDist;
				newScale = Math.max(MIN_SCALE, Math.min(newScale, MAX_SCALE));
				float scaleFactor = newScale / scale;
				scale = newScale;
				matrix.postScale(scaleFactor, scaleFactor, pivotX, pivotY);
				
//				float prevAngle = (float) Math.atan2(
//						prevPos.x - prevPos2.x, prevPos.y - prevPos2.y);
//				float angle = (float) Math.atan2(
//						event.getX(0) - event.getX(1), event.getY(0)
//						- event.getY(1));
//				rotation += prevAngle - angle;
//				matrix.postRotate(
//						(float) ((prevAngle - angle) * 180.0f / Math.PI),
//						pivotX, pivotY);
				
//				matrix.postScale(mScaleFactor, mScaleFactor, pivotX, pivotY);
//				matrix.postTranslate(-prevPivotX + pivotX, -prevPivotY + pivotY);
			}
			prevPos.x = event.getX(0);
			prevPos.y = event.getY(0);
			prevPos2.x = event.getX(1);
			prevPos2.y = event.getY(1);
			prevDist = d;
			invalidate();
		}
		return true;
	}
	
	private float dist(float x, float y, float x2, float y2) {
		return (float) Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	}
}
