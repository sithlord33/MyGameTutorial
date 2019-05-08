package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Powerup implements GameObject {
    private Rect rectangle;
    private Bitmap rType;
    private String color;
    MyThread thread = new MyThread();

    Rect getRectangle() {
        return rectangle;
    }

    void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    Powerup(int powerupHeight, int startX, int startY, int playerGap, String color, int type) {
        rectangle = new Rect(startX - 50, startY, startX, startY + 50);
        this.color = color;

        Bitmap bType = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), type);
        rType = Bitmap.createScaledBitmap(bType, 100, 100, false);
    }

    String getColor() {
        return color;
    }

    boolean playerGrab(RectPlayer player) {
        //rType = null;
        return Rect.intersects(rectangle, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(rType, rectangle.left, rectangle.top, paint);
    }

    @Override
    public void update() {
    }

    public void remove() {
        thread.start();
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            rectangle = new Rect(Constants.SCREEN_HEIGHT, Constants.SCREEN_HEIGHT, Constants.SCREEN_HEIGHT, Constants.SCREEN_HEIGHT);
        }
    }
}