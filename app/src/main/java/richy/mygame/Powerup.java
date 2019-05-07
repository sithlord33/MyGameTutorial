package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Powerup implements GameObject{
    private Rect rectangle;
    private Bitmap rType;

    Rect getRectangle() {
        return rectangle;
    }

    void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    Powerup(int powerupHeight, int startX, int startY, int playerGap, String color, int type) {
        rectangle = new Rect(startX - 50, startY, startX, startY + 50);

        Bitmap bType = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), type);
        rType = Bitmap.createScaledBitmap(bType, 100, 100, false);
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
        rectangle = new Rect(Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 200);
    }
}
