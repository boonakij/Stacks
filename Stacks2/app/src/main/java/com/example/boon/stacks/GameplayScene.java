package com.example.boon.stacks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameplayScene implements Scene {
	private Rect baseRect;
	private Rect r = new Rect();

	private Rect border1;
	private Rect border2;

	public static boolean gameOver = false;
	public static boolean gameStarted = false;
	public static long gameOverTime;

	private long frameTime;
	private long startTime;
	private long initTime;

	private int timeCounter;

	private float dropFrequency;
	private float dropSpeed;
	public int counter;

	private ArrayList<FallingBlock> fallingBlocks;
	private ArrayList<FallenBlock> baseBlocks;

	private boolean movingBlock;
	private FallingBlock draggedBlock;

	public GameplayScene() {
		baseRect = new Rect(0, 19*Constants.SCREEN_HEIGHT/20, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

		border1 = new Rect(Constants.SCREEN_WIDTH/3 - Constants.SCREEN_WIDTH/100, 0,
				Constants.SCREEN_WIDTH/3 + Constants.SCREEN_WIDTH/100, Constants.SCREEN_HEIGHT);
		border2 = new Rect(2*Constants.SCREEN_WIDTH/3 - Constants.SCREEN_WIDTH/100, 0,
				2*Constants.SCREEN_WIDTH/3 + Constants.SCREEN_WIDTH/100, Constants.SCREEN_HEIGHT);

		counter = 0;
		frameTime = System.currentTimeMillis();
		startTime = initTime = System.currentTimeMillis();

		baseBlocks = new ArrayList<FallenBlock>();
		baseBlocks.add(new FallenBlock(5, Constants.SCREEN_WIDTH/6));
		baseBlocks.add(new FallenBlock(5, Constants.SCREEN_WIDTH/2));
		baseBlocks.add(new FallenBlock(5, 5*Constants.SCREEN_WIDTH/6));

		fallingBlocks = new ArrayList<FallingBlock>();

		movingBlock = false;


		dropFrequency = 2.0f; // drop a block every dropFrequency seconds
		dropSpeed = 6.0f; // blocks take dropSpeed seconds to reach the bottom
		timeCounter = 0; // count in milliseconds
	}

	public void reset() {
		gameOverTime = 0;
		counter = 0;
		timeCounter = 0;
		fallingBlocks = new ArrayList<FallingBlock>();
		baseBlocks = new ArrayList<FallenBlock>();
		baseBlocks.add(new FallenBlock(5, Constants.SCREEN_WIDTH/6));
		baseBlocks.add(new FallenBlock(5, Constants.SCREEN_WIDTH/2));
		baseBlocks.add(new FallenBlock(5, 5*Constants.SCREEN_WIDTH/6));
	}

	@Override
	public void terminate() {
		SceneManager.ACTIVE_SCENE = 0;
	}

	@Override
	public void receiveTouch(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(!gameOver && gameStarted) {
					for (FallingBlock fb : fallingBlocks) {
						if (fb.getRectangle().contains((int)event.getX(), (int)event.getY())) {
							movingBlock = true;
							draggedBlock = fb;
							break;
						}
					}
				}
				if(!gameStarted && !gameOver) {
					gameStarted = true;
				}
				else if(gameOver && System.currentTimeMillis() - gameOverTime >= 1000) {
					reset();
					gameOver = false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
					if(!gameOver && movingBlock){
						draggedBlock.changePosition((int) event.getX(), (int) event.getY());
					}
					break;
			case MotionEvent.ACTION_UP:
					movingBlock = false;
					if (draggedBlock != null) {
						moveBlockAfterDrop(draggedBlock);
						draggedBlock = null;
					}
					break;

		}
	}

	void moveBlockAfterDrop(FallingBlock draggedBlock) {
		Log.i("move block", "move block after drop");
		int positionX = draggedBlock.getRectangle().centerX();
		int newPositionX = Constants.SCREEN_WIDTH/6;
		int minDistance = Math.abs(positionX - Constants.SCREEN_WIDTH/6);
		if (Math.abs(positionX - Constants.SCREEN_WIDTH/2) < minDistance) {
			newPositionX = Constants.SCREEN_WIDTH/2;
			minDistance = Math.abs(positionX - Constants.SCREEN_WIDTH/2);
		}
		if (Math.abs(positionX - 5*Constants.SCREEN_WIDTH/6) < minDistance) {
			newPositionX = 5*Constants.SCREEN_WIDTH/6;
			minDistance = Math.abs(positionX - 5*Constants.SCREEN_WIDTH/6);
		}
		draggedBlock.changePosition(newPositionX, draggedBlock.getRectangle().centerY());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.argb(255, 41, 44, 45));
		Paint basePaint = new Paint();
		basePaint.setARGB(255, 212, 214, 216);
		Paint borderPaint = new Paint();
		borderPaint.setARGB(255, 96, 192, 247);
		canvas.drawRect(baseRect, basePaint);
		canvas.drawRect(border1, borderPaint);
		canvas.drawRect(border2, borderPaint);

		if(!gameStarted && !gameOver) {
			Paint paint = new Paint();
			paint.setTextSize(100);
			paint.setColor(Color.MAGENTA);
			Paint titlePaint = new Paint();
			titlePaint.setTextSize(300);
			titlePaint.setColor(Color.WHITE);
			Paint outlinePaint = new Paint();
			outlinePaint.setStyle(Paint.Style.STROKE);
			outlinePaint.setStrokeWidth(20);
			outlinePaint.setTextSize(300);
			outlinePaint.setARGB(255, 255, 255, 255);
			drawAboveCenterText(canvas, titlePaint, "Stacks", 300);
			drawAboveCenterText(canvas, outlinePaint, "Stacks", 300);
			for(FallenBlock fb : baseBlocks){
				fb.draw(canvas);
			}
		}

		if(gameStarted) {
			Paint scorePaint = new Paint();
			scorePaint.setTextSize(150);
			scorePaint.setColor(Color.WHITE);
			Paint outlinePaint = new Paint();
			outlinePaint.setStyle(Paint.Style.STROKE);
			outlinePaint.setStrokeWidth(2);
			outlinePaint.setTextSize(150);
			outlinePaint.setColor(Color.BLACK);
			for(FallenBlock fb : baseBlocks){
				fb.draw(canvas);
			}
			for(FallingBlock fb : fallingBlocks){
				fb.draw(canvas);
			}
		}

		if(gameOver) {
			Paint titlePaint = new Paint();
			titlePaint.setTextSize(200);
			titlePaint.setColor(Color.WHITE);
			Paint outlinePaint = new Paint();
			outlinePaint.setStyle(Paint.Style.STROKE);
			outlinePaint.setStrokeWidth(20);
			outlinePaint.setTextSize(200);
			outlinePaint.setARGB(255, 255, 255, 255);
			Paint titlePaint2 = new Paint();
			titlePaint2.setTextSize(80);
			titlePaint2.setColor(Color.WHITE);
			Paint outlinePaint2 = new Paint();
			outlinePaint2.setStyle(Paint.Style.STROKE);
			outlinePaint2.setStrokeWidth(20);
			outlinePaint2.setTextSize(200);
			outlinePaint2.setARGB(255, 255, 255, 255);
			drawAboveCenterText(canvas, titlePaint, "Game Over", 200);
			drawAboveCenterText(canvas, outlinePaint, "Game Over", 200);
			if((System.currentTimeMillis() - gameOverTime) % 1000 <= 700) {
				drawBelowCenterText(canvas, titlePaint2, "Click to Play Again", 80);
			}
		}

	}

	@Override
	public void update() {
		if(!gameOver) {
			if (frameTime == 0) {
				frameTime = System.currentTimeMillis();
			}
			int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
			frameTime = System.currentTimeMillis();
			timeCounter += elapsedTime;
			if (gameStarted) {
				ArrayList<Integer> toRemove = new ArrayList<Integer>();
				for (int i=0; i < fallingBlocks.size(); i++) {
					if (movingBlock && fallingBlocks.get(i) != draggedBlock && draggedBlock.fallingCollide(fallingBlocks.get(i))) {
						draggedBlock.changeValue(addValues(draggedBlock.getNumValue(), fallingBlocks.get(i).getNumValue()));
						toRemove.add(i);
					}
					for (int j=0; j < baseBlocks.size(); j++) {
						if (fallingBlocks.get(i).fallenCollide(baseBlocks.get(j))) {
							baseBlocks.get(j).changeValue(getNewBaseValue(baseBlocks.get(j).getNumValue(), fallingBlocks.get(i).getNumValue()));
							checkGameOver();
							toRemove.add(i);
						}
					}
				}
				for (int removeIndex : toRemove) {
					fallingBlocks.remove(removeIndex);
				}

				if (1f*timeCounter/1000 > dropFrequency) {
					Log.i("counter", "event now");
					timeCounter = 0;

					int[] baseValues = new int[baseBlocks.size()];
					for(int i = 0; i < baseBlocks.size(); i++) {
						if (baseBlocks.get(i) != null) {
							baseValues[i] = baseBlocks.get(i).getNumValue();
						}
					}
					int[] blockValues = new int[fallingBlocks.size()];
					for(int i = 0; i < fallingBlocks.size(); i++) {
						if (fallingBlocks.get(i) != null) {
							blockValues[i] = fallingBlocks.get(i).getNumValue();
						}
					}
					Log.i("creating new value", ""+ createNewValue(baseValues, blockValues));

					Random random = new Random();
					int min = 1;
					int max = 3;
					int randomValue = random.nextInt(max - min + 1) + min;
					int xPosition = Constants.SCREEN_WIDTH/6;
					if (randomValue == 2) {
						xPosition = Constants.SCREEN_WIDTH/2;
					}
					else if (randomValue == 3) {
						xPosition = 5*Constants.SCREEN_WIDTH/6;
					}
					randomValue = createNewValue(baseValues, blockValues);
					fallingBlocks.add(new FallingBlock(randomValue, xPosition, dropSpeed));
				}
				moveBlocks(elapsedTime);
			}
		}
	}

	private void moveBlocks(int elapsedTime) {
		for(FallingBlock fb : fallingBlocks){
			Log.i("fb info", "" + fb.getRectangle().top + " " + fb.getRectangle().bottom + " " + fb.getRectangle().left + " " + fb.getRectangle().right);
			if (!movingBlock || draggedBlock != fb ) {
				fb.incrementY(Math.round(1f * elapsedTime / 1000 / fb.getFallSpeed() * 19 * Constants.SCREEN_HEIGHT / 20));
			}
		}
	}

	private void checkGameOver() {
		for(FallenBlock fb : baseBlocks){
			if (fb.getNumValue() >= 100) {
				gameOver = true;
			}
		}
	}

	private void drawCenterText(Canvas canvas, Paint paint, String text) {
		paint.setTextAlign(Paint.Align.LEFT);
		canvas.getClipBounds(r);
		int cHeight = r.height();
		int cWidth = r.width();
		paint.getTextBounds(text, 0, text.length(), r);
		float x = cWidth / 2f - r.width() / 2f - r.left;
		float y = cHeight / 2f + r.height() / 2f - r.bottom;
		canvas.drawText(text, x, y, paint);
	}

	private void drawBelowCenterText(Canvas canvas, Paint paint, String text, int below) {
		paint.setTextAlign(Paint.Align.LEFT);
		canvas.getClipBounds(r);
		int cHeight = r.height();
		int cWidth = r.width();
		paint.getTextBounds(text, 0, text.length(), r);
		float x = cWidth / 2f - r.width() / 2f - r.left;
		float y = cHeight / 2f + r.height() / 2f - r.bottom;
		canvas.drawText(text, x, y+below, paint);
	}

	private void drawAboveCenterText(Canvas canvas, Paint paint, String text, int above) {
		paint.setTextAlign(Paint.Align.LEFT);
		canvas.getClipBounds(r);
		int cHeight = r.height();
		int cWidth = r.width();
		paint.getTextBounds(text, 0, text.length(), r);
		float x = cWidth / 2f - r.width() / 2f - r.left;
		float y = cHeight / 2f + r.height() / 2f - r.bottom;
		canvas.drawText(text, x, y-above, paint);
	}

//	public native String stringFromJNI(int i);

	public native int getNewBaseValue(int baseValue, int blockValue);

	public native int addValues(int blockValue1, int blockValue2);

	public native int createNewValue(int[] baseValues, int[] blockValues);

}
