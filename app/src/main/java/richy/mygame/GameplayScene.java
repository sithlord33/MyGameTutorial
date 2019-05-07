package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Random;

public class GameplayScene implements Scene {
    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private PowerupManager powerupManager;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private int gap = new Random().ints(1, 100, Constants.SCREEN_WIDTH - 100).findFirst().getAsInt();

    private OrientationData orientationData;
    private long frameTime;
    private Paint paint = new Paint();
    private Bitmap floor1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.floor);
    private Bitmap floor2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.floor);
    private int posx = floor1.getHeight() - Constants.SCREEN_HEIGHT;
    private int posx2 = posx + 1 + floor1.getHeight();

    GameplayScene() {
        player = new RectPlayer(new Rect(100, 100, 200, 200));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(gap, 300, 100);
        powerupManager = new PowerupManager(gap, 100000000, 50);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }

    private void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(gap, 300, 100);
        powerupManager = new PowerupManager(gap, 100000000, 50);
        movingPlayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                    movingPlayer = true;
                if (gameOver) {
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (movingPlayer && !gameOver)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!gameOver) {
            if (posx < 0 - floor1.getHeight())
                posx = posx2 + 1 + floor1.getHeight();
            if (posx2 < 0 - floor1.getHeight())
                posx2 = posx + 1 + floor1.getHeight();
            posx = posx - (int) obstacleManager.Rspeed;
            posx2 = posx2 - (int) obstacleManager.Rspeed;
        }
        canvas.drawBitmap(floor1, 0, 0 - posx, paint);
        canvas.drawBitmap(floor2, 0, 0 - posx2, paint);
        obstacleManager.draw(canvas);
        powerupManager.draw(canvas);
        player.draw(canvas);

        gap = new Random().ints(1, 100, Constants.SCREEN_WIDTH - 100).findFirst().getAsInt();
        obstacleManager.setPlayerGap(gap);
        if (gameOver) {
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            drawCenterText(canvas, paint);
            drawSubText(canvas, paint);
        }
    }

    @Override
    public void update() {
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f;

                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
            }

            if (playerPoint.x < 0)
                playerPoint.x = 0;
            else if (playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if (playerPoint.y < 0)
                playerPoint.y = 0;
            else if (playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            powerupManager.update();
            player.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollide(player)) {
                gameOver = true;
            }

            if (powerupManager.playerGrab(player)) {

            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds("Game Over", 0, "Game Over".length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText("Game Over", x, y, paint);
    }

    private void drawSubText(Canvas canvas, Paint paint) {
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds("Tap to Restart", 0, "Tap to Restart".length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = (cHeight / 2f + r.height() / 2f - r.bottom) + 200;
        canvas.drawText("Tap to Restart", x, y, paint);
    }

    private void showToast(String msg) {
        Toast.makeText(Constants.CURRENT_CONTEXT, msg, Toast.LENGTH_SHORT).show();
    }
}