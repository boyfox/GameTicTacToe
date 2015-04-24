package com.smlab.mytictactoe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;

import com.smlab.mytictactoe.customviews.BoardViewExp2;

public class TicTacToePage extends Activity {
	private static final String TAG = TicTacToePage.class.getSimpleName();
	public static int cellsCount = 15;
	private static boolean upperHand = false;
	public static int table[][] = new int[cellsCount][cellsCount];
//	private BoardView gameBoardView;
//	private BoardViewExperimental gameBoardView;
	private BoardViewExp2 gameBoardView;
	public static Point displaySize;
	
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tic_tac_toe_page);
//        setContentView(R.layout.activity_tic_tac_toe_page_experimental);
        setContentView(R.layout.activity_tic_tac_toe_page_experimental2);
        
        gameBoardView = (BoardViewExp2) findViewById(R.id.gameBoardViewExperimental2);
//        gameBoardView = (BoardViewExperimental) findViewById(R.id.gameBoardViewExperimental);
//        gameBoardView = (BoardView) findViewById(R.id.gameBoardView);
        gameBoardView.setGame(this);
        
        displaySize = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        if(android.os.Build.VERSION.SDK_INT < 13) {
            displaySize.set(display.getWidth(), display.getHeight());
        } else {
            display.getSize(displaySize);
        }
        LayoutParams fLp = new LayoutParams(displaySize.x, displaySize.x, Gravity.CENTER);
        gameBoardView.setLayoutParams(fLp);
    }
    
    public boolean getUpperHand() {
        return upperHand;
    }

    public int[][] getTable() {
        return table;
    }

	public int getCellsCount() {
		return cellsCount;
	}

	public void movePoint(int x, int y, int gamerOrderId) {
		table[y][x] = gamerOrderId;
		gameBoardView.invalidate();
	}	

	public void movePoint2(int x, int y, int gamerOrderId) {
		table[x][y] = gamerOrderId;
		gameBoardView.invalidate();
	}	
}
