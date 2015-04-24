package com.smlab.mytictactoe.utils;

import android.view.ScaleGestureDetector;

import com.smlab.mytictactoe.customviews.BoardViewExperimental;

public class ScaleListenerExperimental extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	private final float MAX_SCALE = 5.0f;
	private final float MIN_SCALE = 1.0f;
	private BoardViewExperimental view;
	
	public ScaleListenerExperimental(BoardViewExperimental view) {
		this.view = view;
	}

	@Override
    public boolean onScale(ScaleGestureDetector detector) {
        view.mScaleFactor *= detector.getScaleFactor();
        // Don't let the object get too small or too large.
        view.mScaleFactor = Math.max(MIN_SCALE, Math.min(view.mScaleFactor, MAX_SCALE));
        view.invalidate();
        return true;
    }
}
