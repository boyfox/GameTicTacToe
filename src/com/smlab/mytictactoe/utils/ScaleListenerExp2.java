package com.smlab.mytictactoe.utils;

import android.view.ScaleGestureDetector;

import com.smlab.mytictactoe.customviews.BoardViewExp2;

public class ScaleListenerExp2 extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	private final float MAX_SCALE = 3.0f;
	private final float MIN_SCALE = 1.0f;
	private BoardViewExp2 view;
	
	public ScaleListenerExp2(BoardViewExp2 view) {
		this.view = view;
	}

	@Override
    public boolean onScale(ScaleGestureDetector detector) {
        view.scaleFactor *= detector.getScaleFactor();
        view.scaleFactor = Math.max(MIN_SCALE, Math.min(view.scaleFactor, MAX_SCALE));
        return true;
    }
}
