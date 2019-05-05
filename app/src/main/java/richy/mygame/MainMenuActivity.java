package richy.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {
    private Button start, options, quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_main_menu);

        start = findViewById(R.id.start);
        options = findViewById(R.id.options);
        quit = findViewById(R.id.quit);

        start.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        options.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, OptionsActivity.class);
            startActivity(intent);
        });

        quit.setOnClickListener(v -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainMenuActivity.this);
            adb.setTitle("Quit?");
            adb.setMessage("Are you sure you want to QUIT?");
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Ok", (dialog, which) -> {
                finish();
                System.exit(0);
            });
            adb.show();
        });
    }
}
