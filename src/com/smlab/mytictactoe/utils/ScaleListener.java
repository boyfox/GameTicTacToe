package com.smlab.mytictactoe.utils;

import android.view.ScaleGestureDetector;

import com.smlab.mytictactoe.customviews.BoardView;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	private final float MAX_SCALE = 1.5f;
	private final float MIN_SCALE = 1.0f;
	private BoardView view;
	
	public ScaleListener(BoardView view) {
		this.view = view;
	}

	@Override
    public boolean onScale(ScaleGestureDetector detector) {
        view.mScaleFactor *= detector.getScaleFactor();
        // Don't let the object get too small or too large.
        view.mScaleFactor = Math.max(MIN_SCALE, Math.min(view.mScaleFactor, MAX_SCALE));
//        view.invalidate();
        return true;
    }
}
