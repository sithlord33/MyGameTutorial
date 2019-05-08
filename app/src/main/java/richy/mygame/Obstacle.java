package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Rect;

import java.util.Random;

public class Obstacle implements GameObject {
    private Rect rectangle;
    private AnimationManager animManager;
    //BitmapFactory bf = new BitmapFactory();
    /*Bitmap barnacle = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.barnacle);
    Bitmap barnacle_bite = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.barnacle_bite);
    Bitmap ghost = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ghost);
    Bitmap ghost_normal = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ghost_normal);
    Bitmap spinner = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spinner);
    Bitmap spinner_spin = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spinner_spin);
    private Bitmap Bbarnacle = Bitmap.createScaledBitmap(barnacle, 100, 100, false);*/
    private int diff = 0;


    Rect getRectangle() {
        return rectangle;
    }

    void incrementY(float y) {

        rectangle.top += y;
        rectangle.bottom += y;
    }

    Obstacle(int rectHeight, int startX, int startY, int playerGap, int walk1, int walk2) {
        //l,t,r,b
        startX= new Random().ints(1, 0, Constants.SCREEN_WIDTH).findFirst().getAsInt();
        startY += new Random().ints(1, 0, 700).findFirst().getAsInt();
        rectangle = new Rect(startX - 100, startY, startX, startY + rectHeight);

        Bitmap bWalk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), walk1);
        Bitmap bWalk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), walk2);

        Animation attack = new Animation(new Bitmap[]{bWalk1, bWalk2}, 0.5f);
        animManager = new AnimationManager(new Animation[]{attack});

        //

    }

    boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle());
    }

    void kill() {
        rectangle = new Rect(Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 20, Constants.SCREEN_HEIGHT + 200);

    }

    @Override
    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
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