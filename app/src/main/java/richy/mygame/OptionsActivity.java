package richy.mygame;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reset = findViewById(R.id.reset);

        reset.setOnClickListener(v -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(OptionsActivity.this);
            adb.setTitle("Reset HighScore?");
            adb.setMessage("Are you sure you want to Reset the HighScore? (This can't be undone)");
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Ok", (dialog, which) -> {
                resetHighScore();
            });
            adb.show();
        });
    }

    private void resetHighScore() {
        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        int score = prefs.getInt("key", 0);
        if (score != 0) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear().apply();
        }
        showToast("HighScore Reset");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
