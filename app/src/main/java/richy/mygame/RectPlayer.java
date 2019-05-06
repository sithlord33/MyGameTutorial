package richy.mygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    private Rect rectangle;

    private AnimationManager animManager;

    Rect getRectangle() {
        return rectangle;
    }

    RectPlayer(Rect rectangle) {
        this.rectangle = rectangle;

        Bitmap idleImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.standing);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.walk_right);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.standing_right);

        Animation idle = new Animation(new Bitmap[]{idleImg}, 1);
        Animation walkRight = new Animation((new Bitmap[]{walk1, walk2}), 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        Animation walkLeft = new Animation((new Bitmap[]{walk1, walk2}), 0.5f);

        animManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});
    }

    @Override
    public void draw(Canvas canvas) {
        /*Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);*/
        animManager.draw(canvas, rectangle);

    }

    @Override
    public void update() {
        animManager.update();
    }

    public void update(Point point) {
        float oldLeft = rectangle.left;

        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);

        int state = 0;
        if (rectangle.left - oldLeft > 5)
            state = 1;
        else if (rectangle.left - oldLeft < -5)
            state = 2;

        animManager.playAnim(state);
        animManager.update();
    }
}