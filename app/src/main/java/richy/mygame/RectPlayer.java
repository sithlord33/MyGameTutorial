package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    public int powerup = 0;
    private Rect rectangle;

    private AnimationManager animManager;

    boolean Sword = false;

    boolean Shield = false;

    Rect getRectangle() {
        return rectangle;
    }

    RectPlayer(Rect rectangle) {
        this.rectangle = rectangle;

        Bitmap idleImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.standing);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.walk_right);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.standing_right);
        Bitmap straigh1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.straight1);
        Bitmap straigh2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.straight2);
        Bitmap sword1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sword1);
        Bitmap sword2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sword2);
        Bitmap sword3 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sword3);
        Bitmap sword4 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sword4);


        Animation idle = new Animation(new Bitmap[]{idleImg, straigh1, idleImg, straigh2}, 1);
        Animation walkRight = new Animation((new Bitmap[]{walk1, walk2}), 0.5f);
        Animation attack = new Animation((new Bitmap[]{sword1, sword2, sword3, sword4}), 1.5f);
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        Animation walkLeft = new Animation((new Bitmap[]{walk1, walk2}), 0.5f);

        animManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft, attack});
    }

    @Override
    public void draw(Canvas canvas) {
        /*Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);*/
        animManager.draw(canvas, rectangle);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.WHITE);
        canvas.drawText("Power-Ups: " + powerup, 50, 30 + paint.descent() - paint.ascent() + 175, paint);
        if (Sword) {
            paint.setTextSize(60);
            paint.setColor(Color.BLUE);
            canvas.drawText("Sword Activated", 50, 50 + paint.descent() - paint.ascent() + 175, paint);
        }

    }

    @Override
    public void update() {
        animManager.update();
    }

    public void update(Point point) {
        float oldLeft = rectangle.left;

        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
        MyThread thread = new MyThread();
        int state = 0;
        if(Sword) {
            thread.start();
            state = 3;
        }
        else {
            if (rectangle.left - oldLeft > 5)
                state = 1;
            else if (rectangle.left - oldLeft < -5)
                state = 2;
        }

        animManager.playAnim(state);
        animManager.update();
    }

    public void setShield(boolean b) {
        this.Shield = b;
    }

    public void setSword(boolean b) {
        this.Sword = b;
    }

    public boolean getSword() {
        return Sword;
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            try {
                this.join(1500);
                Sword = false;
                Shield = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}