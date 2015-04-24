package com.smlab.mytictactoe.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.smlab.mytictactoe.R;
import com.smlab.mytictactoe.TicTacToePage;
import com.smlab.mytictactoe.utils.ScaleListenerExp2;

public class BoardViewExp2 extends View {
	private static final String TAG = BoardView.class.getSimpleName();

	private Bitmap oBitmap;
	private Bitmap xBitmap;
	private Paint linesPaint;
	private Paint backgroundPaint;
	private Paint winLinePaint;
	private int boardWidth;
	private int boardHeight;
	private int cellWidth;

	private TicTacToePage game;

    public float scaleFactor = 1.f;
    private ScaleGestureDetector detector;
    private GestureDetectorCompat compactGesDetector;

    //These constants specify the mode that we're in
    private static int NONE = 0;
    private static int DRAG = 1;
    private static int ZOOM = 2;

    private int mode;

    //These two variables keep track of the X and Y coordinate of the finger when it first
    //touches the screen
    private float startX = 0f;
    private float startY = 0f;

    //These two variables keep track of the amount we need to translate the canvas along the X
    //and the Y coordinate
    private float translateX = 0f;
    private float translateY = 0f;

    //These two variables keep track of the amount we translated the X and Y coordinates, the last time we
    //panned.
    private float previousTranslateX = 0f;
    private float previousTranslateY = 0f;    
    private boolean dragged = false;
    private Rect clipBoundsBoard; // this is coordinates game view board terms of display

    private float movedDX = 0;
    private float movedDY = 0;    
    
    public BoardViewExp2(Context context) {
		super(context);
		init();
	}

	public BoardViewExp2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BoardViewExp2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi") 
	public BoardViewExp2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
		
		detector = new ScaleGestureDetector(getContext(), new ScaleListenerExp2(this));
		compactGesDetector = new GestureDetectorCompat(getContext(), new OnGestureListener() {
			@Override public boolean onSingleTapUp(MotionEvent e) {

//				/****** variant - 3 *******/				
				int px = (int)(((e.getX() / scaleFactor + clipBoundsBoard.left + (-1 * movedDX)) / cellWidth));
				int py = (int)(((e.getY() / scaleFactor + clipBoundsBoard.top + (-1 * movedDY)) / cellWidth));
				Log.i(TAG, "movePoint2, px:" + px + " py:" + py + " movedDX:" + movedDX + " movedDY:" + movedDY);
				game.movePoint2(px, py, 1);
				
				return false;
			}
			@Override public void onShowPress(MotionEvent e) {}
			@Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//				Log.i(TAG, "OnScroll distanceX:" + distanceX + " distanceY:" + distanceY);
				return false;
			}
			@Override public void onLongPress(MotionEvent e) {}
			@Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false;}
			@Override public boolean onDown(MotionEvent e) { return false; }
		});
	}

	private void drawBackground(Canvas canvas) {
		canvas.drawRect(0.0F, 0.0F, boardWidth, boardHeight, backgroundPaint);
		canvas.drawLine(0, 0, boardWidth, 0, linesPaint);
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
		int left = i * cellWidth;
		int top = j * cellWidth;
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
		canvas.getClipBounds(clipBoundsBoard);

        //We're going to scale the X and Y coordinates by the same amount
        canvas.scale(scaleFactor, scaleFactor);
//		canvas.scale(this.scaleFactor, this.scaleFactor, this.detector.getFocusX(), this.detector.getFocusY());

        //If translateX times -1 is lesser than zero, let's set it to zero. This takes care of the left bound
        if((translateX * -1) < 0) {
           translateX = 0;
        }

        //This is where we take care of the right bound. We compare translateX times -1 to (scaleFactor - 1) * displayWidth.
        //If translateX is greater than that value, then we know that we've gone over the bound. So we set the value of 
        //translateX to (1 - scaleFactor) times the display width. Notice that the terms are interchanged; it's the same
        //as doing -1 * (scaleFactor - 1) * displayWidth
        else if((translateX * -1) > (scaleFactor - 1) * TicTacToePage.displaySize.x) {
           translateX = (1 - scaleFactor) * TicTacToePage.displaySize.x;
        }

        if(translateY * -1 < 0) {
           translateY = 0;
        }

        //We do the exact same thing for the bottom bound, except in this case we use the height of the display
        else if((translateY * -1) > (scaleFactor - 1) * TicTacToePage.displaySize.x) {
           translateY = (1 - scaleFactor) * TicTacToePage.displaySize.x;
        }

        //We need to divide by the scale factor here, otherwise we end up with excessive panning based on our zoom level
        //because the translation amount also gets scaled according to how much we've zoomed into the canvas.
        movedDX = translateX / scaleFactor;
        movedDY = translateY / scaleFactor;
        canvas.translate(movedDX, movedDY);

		boardWidth = getWidth();
		boardHeight = getHeight();

		drawBackground(canvas);
		drawBoard(canvas);
		drawOXs(canvas);
//		drawWinLine(canvas, 1, 5, 5, 9);
        
        /* The rest of your canvas-drawing code */
        canvas.restore();
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                //We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
                //amount for each coordinates This works even when we are translating the first time because the initial
                //values for these two variables is zero.               
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;
                break;

            case MotionEvent.ACTION_MOVE:               
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;
 
                //We cannot use startX and startY directly because we have adjusted their values using the previous translation values. 
                //This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) + 
                                            Math.pow(event.getY() - (startY + previousTranslateY), 2)
                                           );
                if(distance > 0) {
                   dragged = true;
                }               
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;
   
            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;

                //All fingers went up, so let's save the value of translateX and translateY into previousTranslateX and 
                //previousTranslate           
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;

                //This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
                //and previousTranslateY when the second finger goes up
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;       
        }

        detector.onTouchEvent(event);
        compactGesDetector.onTouchEvent(event);

        //We redraw the canvas only in the following cases:
        // o The mode is ZOOM 
        //        OR
        // o The mode is DRAG and the scale factor is not equal to 1 (meaning we have zoomed) and dragged is
        //   set to true (meaning the finger has actually moved)
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
            invalidate();
        }
        return true;
    }
}
