package richy.mygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class EnemyManager {
    //higher index = lower on screen = higher y value
    private ArrayList<Enemy> enemies;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;

    private int rand;

    private long startTime;
    private long initTime;

    private int score = 0;

    float Rspeed = 0;

    EnemyManager(int playerGap, int obstacleGap, int obstacleHeight) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;

        startTime = initTime = System.currentTimeMillis();

        enemies = new ArrayList<>();

        populateObstacles();

        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        Constants.HIGH_SCORE = prefs.getInt("key", 0);
    }

    boolean playerCollide(RectPlayer player) {
        for (Enemy ob : enemies)
            if (ob.playerCollide(player))
                return true;
        return false;
    }

    private void populateObstacles() {
        int currY = -5 * Constants.SCREEN_HEIGHT / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            rand = (int) (Math.random() * 4);
            switch (rand) {
                case 0:
                    enemies.add(new Enemy(obstacleHeight, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
                    break;
                case 1:
                    enemies.add(new Enemy(obstacleHeight, xStart, currY, playerGap, R.drawable.ghost, R.drawable.ghost_normal));
                    break;
                case 2:
                    enemies.add(new Enemy(obstacleHeight, xStart, currY, playerGap, R.drawable.spinner, R.drawable.spinner_spin));
                    break;
                case 3:
                    enemies.add(new Enemy(obstacleHeight, xStart, currY, playerGap, R.drawable.snakelava, R.drawable.snakelava_ani));
                    break;
                default:
                    enemies.add(new Enemy(obstacleHeight, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
            }
            //enemies.add(new Enemy(obstacleHeight, color, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) Math.sqrt(1 + (startTime - initTime) / 2000) * Constants.SCREEN_HEIGHT / (10000.0f);
        for (Enemy ob : enemies) {
            ob.incrementY(Rspeed = speed * elapsedTime);
            ob.update();
        }
        if (enemies.get(enemies.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            int yStart = -1 * Constants.SCREEN_HEIGHT / 2;
            rand = (int) (Math.random() * 4);
            switch (rand) {
                case 0:
                    enemies.add(0, new Enemy(obstacleHeight, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
                    break;
                case 1:
                    enemies.add(0, new Enemy(obstacleHeight, xStart, yStart, playerGap, R.drawable.ghost, R.drawable.ghost_normal));
                    break;
                case 2:
                    enemies.add(0, new Enemy(obstacleHeight, xStart, yStart, playerGap, R.drawable.spinner, R.drawable.spinner_spin));
                    break;
                case 3:
                    enemies.add(0, new Enemy(obstacleHeight, xStart, yStart, playerGap, R.drawable.snakelava, R.drawable.snakelava_ani));
                    break;
                default:
                    enemies.add(0, new Enemy(obstacleHeight, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));

            }
            //enemies.add(0, new Enemy(obstacleHeight, color, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
            enemies.remove(enemies.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas) {
        for (Enemy ob : enemies)
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