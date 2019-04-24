package richy.mygame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import java.io.File;
import java.io.FileDescriptor;

public class Obstacle implements GameObject {
    private Rect rectangle;
    private Rect rectangle2;
    private int color;
    BitmapFactory bf = new BitmapFactory();
    Bitmap wall = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.wall);
    Bitmap wall2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.wall);


    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top+=y;
        rectangle.bottom+=y;
        rectangle2.top+=y;
        rectangle2.bottom+=y;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        BitmapFactory bf = new BitmapFactory();
        this.color = color;
        //l,t,r,b
        rectangle = new Rect(0, startY, startX, startY + rectHeight);

        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
        wall = wall.copy(Bitmap.Config.ARGB_8888,true);
        wall2 = wall2.copy(Bitmap.Config.ARGB_8888,true);
        wall.setHeight(rectangle.bottom - rectangle.top);
        wall.setWidth(rectangle.right - rectangle.left);
        wall2.setHeight(rectangle2.bottom - rectangle2.top);
        wall2.setWidth(rectangle2.right - rectangle2.left);
    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawBitmap(wall, rectangle.left, rectangle.bottom, paint);
        canvas.drawBitmap(wall2, rectangle2.left, rectangle2.bottom, paint);
    }

    @Override
    public void update() {

    }
}
