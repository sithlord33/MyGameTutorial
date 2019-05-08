package richy.mygame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.Toast;

import java.util.ArrayList;

public class PowerupManager {
    private ArrayList<Powerup> powerups;
    private int playerGap;
    private int powerupGap;
    private int powerupHeight;

    private int rand;

    private long startTime;
    private long initTime;

    private int score = 0;
    float Rspeed = 0;

    PowerupManager(int playerGap, int powerupGap, int powerupHeight) {
        this.playerGap = playerGap;
        this.powerupGap = powerupGap;
        this.powerupHeight = powerupHeight;

        startTime = initTime = System.currentTimeMillis();

        powerups = new ArrayList<>();

        populatePowerups();
    }

    boolean playerGrab(RectPlayer player) {
        for (Powerup pu : powerups)
            if (pu.playerGrab(player) && pu.grabable) {
                pu.remove();
                return true;
            }
        return false;
    }

    private void populatePowerups() {
        int currY = -5 * Constants.SCREEN_HEIGHT / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH));
            rand = (int) (Math.random() * 2);            switch (rand) {
                case 0:
                    powerups.add(new Powerup(powerupHeight, xStart, currY, playerGap, "blue", R.drawable.sphere_light_blue));
                    break;
                case 1:
                    powerups.add(new Powerup(powerupHeight, xStart, currY, playerGap, "blue", R.drawable.sphere_red));
                    break;
                default:
                    powerups.add(new Powerup(powerupHeight, xStart, currY, playerGap, "blue", R.drawable.sphere_light_blue));
            }
            currY += powerupGap + powerupHeight;
        }
    }

    public void update() {
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) Math.sqrt(1 + (startTime - initTime) / 2000) * Constants.SCREEN_HEIGHT / (10000.0f);
        for (Powerup pu : powerups) {
            pu.incrementY(Rspeed = speed * elapsedTime);
            pu.update();
        }
        if (powerups.get(powerups.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH));
            int yStart = -1 * Constants.SCREEN_HEIGHT / 4;
            rand = (int) (Math.random() * 3);
            switch (rand) {
                case 0:
                    powerups.add(0, new Powerup(powerupHeight, xStart, yStart, playerGap, "blue", R.drawable.sphere_light_blue));
                    break;
                case 1:
                    powerups.add(0, new Powerup(powerupHeight, xStart, yStart, playerGap, "blue", R.drawable.sphere_red));
                    break;
                default:
                    powerups.add(0, new Powerup(powerupHeight, xStart, yStart, playerGap, "blue", R.drawable.sphere_light_blue));

            }
            powerups.remove(powerups.size() - 1);
        }
    }

    public void draw(Canvas canvas) {
        for (Powerup pu : powerups)
            pu.draw(canvas);
    }
}