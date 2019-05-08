package richy.mygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;

    private int rand;

    private long startTime;
    private long initTime;

    private int score = 0;
    boolean generate = true;

    float Rspeed = 0;
    MyThread thread = new MyThread();

    ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        thread.start();
        populateObstacles();
        populateObstacles();

        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        Constants.HIGH_SCORE = prefs.getInt("key", 0);
    }

    boolean playerCollide(RectPlayer player) {
        for (Obstacle ob : obstacles)
            if (ob.playerCollide(player)) {
                if (player.Sword || player.Shield) {
                    ob.kill();
                    return false;
                }
                return true;
            }
        return false;
    }

    private void populateObstacles() {
        int currY = -5 * Constants.SCREEN_HEIGHT / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            rand = (int) (Math.random() * 4);
            switch (rand) {
                case 0:
                    obstacles.add(new Obstacle(obstacleHeight, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
                    break;
                case 1:
                    obstacles.add(new Obstacle(obstacleHeight, xStart, currY, playerGap, R.drawable.ghost, R.drawable.ghost_normal));
                    break;
                case 2:
                    obstacles.add(new Obstacle(obstacleHeight, xStart, currY, playerGap, R.drawable.spinner, R.drawable.spinner_spin));
                    break;
                case 3:
                    obstacles.add(new Obstacle(obstacleHeight, xStart, currY, playerGap, R.drawable.snakelava, R.drawable.snakelava_ani));
                    break;
                default:
                    obstacles.add(new Obstacle(obstacleHeight, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
            }
            //obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
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
            rand = (int) (Math.random() * 4);
            switch (rand) {
                case 0:
                    obstacles.add(0, new Obstacle(obstacleHeight, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
                    break;
                case 1:
                    obstacles.add(0, new Obstacle(obstacleHeight, xStart, yStart, playerGap, R.drawable.ghost, R.drawable.ghost_normal));
                    break;
                case 2:
                    obstacles.add(0, new Obstacle(obstacleHeight, xStart, yStart, playerGap, R.drawable.spinner, R.drawable.spinner_spin));
                    break;
                case 3:
                    obstacles.add(0, new Obstacle(obstacleHeight, xStart, yStart, playerGap, R.drawable.snakelava, R.drawable.snakelava_ani));
                    break;
                default:
                    obstacles.add(0, new Obstacle(obstacleHeight, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));

            }
            //obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, yStart, playerGap, R.drawable.barnacle, R.drawable.barnacle_bite));
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
    public class MyThread extends Thread {
        @Override
        public void run() {
            while (generate) {
                try {
                    this.join(1500);
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.interrupt();
        }
    }
}