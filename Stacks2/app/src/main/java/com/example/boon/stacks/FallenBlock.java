package com.example.boon.stacks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

public class FallenBlock {
	private Rect rectangle;
	private int numValue;

	public FallenBlock(int numValue, int startX) {
		this.numValue = numValue;
		int blockHeight = Math.round((1f*numValue/Constants.MAX_STACK*19*Constants.SCREEN_HEIGHT/20));
		this.rectangle = new Rect(startX - Constants.SCREEN_WIDTH/7, 19*Constants.SCREEN_HEIGHT/20 - blockHeight, startX + Constants.SCREEN_WIDTH/7, 19*Constants.SCREEN_HEIGHT/20);
	}

	public Rect getRectangle() {
		return rectangle;
	}

	public int getNumValue() { return numValue; }

	public void changeValue(int newNumValue) {
		this.numValue = newNumValue;
		int blockHeight =Math.round((1f*newNumValue/Constants.MAX_STACK*19*Constants.SCREEN_HEIGHT/20));
		rectangle.top = 19*Constants.SCREEN_HEIGHT/20 - blockHeight;
	}

	public boolean fallingCollide(FallingBlock fallingBlock) {
		return Rect.intersects(rectangle, fallingBlock.getRectangle());
	}

	public void draw(Canvas canvas) {
		Paint backgroundPaint = new Paint();
		backgroundPaint.setARGB(255, 224, 114, 214);
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(100);
		textPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawRect(rectangle, backgroundPaint);
		canvas.drawText(""+numValue, rectangle.left + (rectangle.right - rectangle.left)/2,
				rectangle.top - (rectangle.top - rectangle.bottom)/2 + 30, textPaint);
	}

	private void drawCenterText(Canvas canvas, Rect rectangle, Paint paint, String text) {
		paint.setTextAlign(Paint.Align.LEFT);
		int cHeight = rectangle.height();
		int cWidth = rectangle.width();
		paint.getTextBounds(text, 0, text.length(), rectangle);
		float x = cWidth / 2f - rectangle.width() / 2f - rectangle.left;
		float y = cHeight / 2f + rectangle.height() / 2f - rectangle.bottom;
		canvas.drawText(text, x, y, paint);
	}

}
