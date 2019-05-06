package richy.mygame;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    static int ACTIVE_SCENE;

    SceneManager() {
        ACTIVE_SCENE = 0;
        scenes.add(new GameplayScene());
    }

    void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }
}
