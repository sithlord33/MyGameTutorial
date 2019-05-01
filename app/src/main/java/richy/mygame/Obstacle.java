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
import java.util.Random;

public class Obstacle implements GameObject {
    private Rect rectangle;
    private Rect rectangle2;
    private int color;
    BitmapFactory bf = new BitmapFactory();
    Bitmap rock = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.rock);
    private Bitmap Rrock = Bitmap.createScaledBitmap(rock, 100, 100, false);
    int diff = new Random().ints(1, 0, 700).findFirst().getAsInt();


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
        rectangle = new Rect(startX - 100, startY, startX, startY + rectHeight);
        diff = new Random().ints(1, 0, 700).findFirst().getAsInt();

        rectangle2 = new Rect(startX + playerGap, startY + diff, startX + playerGap + 100, startY + rectHeight + diff);

    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawBitmap(Rrock, rectangle.left, rectangle.top, paint);
        canvas.drawBitmap(Rrock, rectangle2.left, rectangle2.top, paint);
    }

    @Override
    public void update() {

    }
}
