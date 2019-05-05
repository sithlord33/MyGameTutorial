package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Obstacle implements GameObject {
    private Rect rectangle;
    private Rect rectangle2;
    private int rectHeight;
    private int color;
    private Animation attack;
    private AnimationManager animManager;
    //BitmapFactory bf = new BitmapFactory();
    /*Bitmap barnacle = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.barnacle);
    Bitmap barnacle_bite = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.barnacle_bite);
    Bitmap ghost = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ghost);
    Bitmap ghost_normal = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ghost_normal);
    Bitmap spinner = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spinner);
    Bitmap spinner_spin = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spinner_spin);
    private Bitmap Bbarnacle = Bitmap.createScaledBitmap(barnacle, 100, 100, false);*/
    private Bitmap obstacle;
    int diff = new Random().ints(1, 0, 700).findFirst().getAsInt();


    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {

        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap, int walk1, int walk2) {
        //BitmapFactory bf = new BitmapFactory();
        this.color = color;
        this.rectHeight = rectHeight;
        //l,t,r,b
        rectangle = new Rect(startX - 100, startY, startX, startY + rectHeight);

        BitmapFactory bf = new BitmapFactory();
        Bitmap bWalk1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), walk1);
        Bitmap bWalk2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), walk2);

        attack = new Animation(new Bitmap[]{bWalk1, bWalk2}, 0.5f);
        animManager = new AnimationManager(new Animation[]{attack});

        //

        diff = new Random().ints(1, 0, 700).findFirst().getAsInt();

        rectangle2 = new Rect(startX + playerGap, startY + diff, startX + playerGap + 100, startY + rectHeight + diff);

    }

    public void setX(int x) {
        rectangle.left = x - 100;
        rectangle.right = x;
    }

    public void setY(int y) {
        rectangle.top = y;
        rectangle.bottom = y + rectHeight;
    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
        animManager.draw(canvas, rectangle2);
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