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
    private int height = Constants.SCREEN_HEIGHT;
    boolean grabable = true;
    private Bitmap gray  = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.grayorb);
    Bitmap grayOrb = Bitmap.createScaledBitmap(gray, 100, 100, false);

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
        rType = grayOrb;
        grabable = false;
    }
}