package richy.mygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class ObstacleManager {
    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private Obstacle barnacle;
    private Obstacle ghost;
    private Obstacle spinner;
    private ArrayList<Obstacle> bank;

    private long startTime;
    private long initTime;

    private int score = 0;

    float Rspeed = 0;

    ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        bank = new ArrayList<>();

        barnacle = new Obstacle(obstacleHeight, color, 100, 100, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite);
        ghost = new Obstacle(obstacleHeight, color, 100, 100, playerGap, R.drawable.ghost, R.drawable.ghost_normal);
        spinner = new Obstacle(obstacleHeight, color, 100, 100, playerGap, R.drawable.spinner, R.drawable.spinner_spin);
        bank.add(barnacle);
        bank.add(ghost);
        bank.add(spinner);

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();

        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        Constants.HIGH_SCORE = prefs.getInt("key", 0);
    }

    boolean playerCollide(RectPlayer player) {
        for (Obstacle ob : obstacles)
            if (ob.playerCollide(player))
                return true;
        return false;
    }

    private void populateObstacles() {
        int currY = -5 * Constants.SCREEN_HEIGHT / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) Math.sqrt(1 + (startTime - initTime) / 2000) * Constants.SCREEN_HEIGHT / (10000.0f);
        for (Obstacle ob : obstacles) {
            ob.incrementY(Rspeed = speed * elapsedTime);
            ob.update();
        }
        if (obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            int yStart = -1 * Constants.SCREEN_HEIGHT / 4;
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(75);
        paint.setColor(Color.LTGRAY);
        canvas.drawText("SCORE: " + score, 50, 50 + paint.descent() - paint.ascent(), paint);

        if (score > Constants.HIGH_SCORE)
            Constants.HIGH_SCORE = score;

        paint.setTextSize(40);
        canvas.drawText("HIGH SCORE: " + Constants.HIGH_SCORE, 50, 50 + paint.descent() - paint.ascent() + 100, paint);

        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("key", Constants.HIGH_SCORE);
        editor.apply();
    }

    void setPlayerGap(int gap) {
        this.playerGap = gap;
    }
}