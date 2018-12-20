package com.example.boon.stacks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class FallingBlock {
	private Rect rectangle;
	private int numValue;
	private float fallSpeed;

	public FallingBlock(int numValue, int startX, float fallSpeed) {
		this.numValue = numValue;
		this.fallSpeed = fallSpeed;
		int blockHeight = Math.round(1f * numValue/Constants.MAX_STACK*19*Constants.SCREEN_HEIGHT/20);
		Log.i("block height", ""+blockHeight);
		rectangle = new Rect(startX - Constants.SCREEN_WIDTH/7, -1 * blockHeight, startX + Constants.SCREEN_WIDTH/7, 0);
	}

	public Rect getRectangle() {
		return rectangle;
	}

	public int getNumValue() { return numValue; }

	public void changeValue(int newNumValue) {
		this.numValue = newNumValue;
		int blockHeight = Math.round((1f*newNumValue/Constants.MAX_STACK*19*Constants.SCREEN_HEIGHT/20));
		int newBottom = rectangle.bottom + (blockHeight - (rectangle.bottom - rectangle.top))/2;
		int newTop = rectangle.top - (blockHeight - (rectangle.bottom - rectangle.top))/2;
		rectangle.bottom = newBottom;
		rectangle.top = newTop;
	}

	public float getFallSpeed() { return fallSpeed; }

	public void incrementY(float y) {
		rectangle.top += y;
		rectangle.bottom +=y;
	}

	public void changePosition(int x, int y) {
		int newBottom = y - (rectangle.top - rectangle.bottom)/2;
		int newTop = y + (rectangle.top - rectangle.bottom)/2;
		int newRight = x + (rectangle.right - rectangle.left)/2;
		int newLeft = x - (rectangle.right - rectangle.left)/2;
		rectangle.bottom = newBottom;
		rectangle.top = newTop;
		rectangle.right = newRight;
		rectangle.left = newLeft;
	}

	public boolean fallingCollide(FallingBlock fallingBlock) {
		return Rect.intersects(rectangle, fallingBlock.getRectangle());
	}

	public boolean fallenCollide(FallenBlock fallenBlock) {
		return Rect.intersects(rectangle, fallenBlock.getRectangle());
	}

	public void draw(Canvas canvas) {
		Paint backgroundPaint = new Paint();
		backgroundPaint.setARGB(255, 96, 192, 247);
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
