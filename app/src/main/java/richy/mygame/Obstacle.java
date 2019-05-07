package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Enemy implements GameObject {
    private Rect rectangle;
    private Rect rectangle2;
    private Rect rectangle3;
    private AnimationManager animManager;
    private int diff = new Random().ints(1, 0, 700).findFirst().getAsInt();


    Rect getRectangle() {
        return rectangle;
    }

    void incrementY(float y) {

        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
        rectangle3.top += y;
        rectangle3.bottom += y;
    }

    Enemy(int rectHeight, int startX, int startY, int playerGap, int walk1, int walk2) {
        //l,t,r,b
        rectangle = new Rect(startX - 100, startY, startX, startY + rectHeight);

        Bitmap bWalk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), walk1);
        Bitmap bWalk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), walk2);

        Animation attack = new Animation(new Bitmap[]{bWalk1, bWalk2}, 0.5f);
        animManager = new AnimationManager(new Animation[]{attack});

        //

        diff = new Random().ints(1, 0, 700).findFirst().getAsInt();
        rectangle2 = new Rect(startX + playerGap, startY + diff, startX + playerGap + 100, startY + rectHeight + diff);

        diff = new Random().ints(1, 0, 700).findFirst().getAsInt();
        rectangle3 = new Rect(startX - 200, startY + diff + 100, startX - 100, startY + rectHeight + diff + 200);

    }

    boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle()) || Rect.intersects(rectangle3, player.getRectangle());
    }

    void kill() {
        rectangle = new Rect(Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 200);

    }

    @Override
    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
        animManager.draw(canvas, rectangle2);
        animManager.draw(canvas, rectangle3);
        //Paint paint = new Paint();
        //paint.setColor(color);
        //canvas.drawBitmap(Bbarnacle, rectangle.left, rectangle.top, paint);
        //canvas.drawBitmap(Bbarnacle, rectangle2.left, rectangle2.top, paint);
    }

    @Override
    public void update() {
        animManager.playAnim(0);
        animManager.update();
    }
}