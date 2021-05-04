package mchou.apps.main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


import mchou.apps.main.widgets.CircularMenuWidget;
import mchou.apps.main.widgets.OnItemMenuPressed;
import mchou.apps.main.tools.Animations;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        LinearLayout container = (LinearLayout) findViewById(R.id.menuContainer);
        OnItemMenuPressed callback = new  OnItemMenuPressed() {
            @Override
            public void OnItemPressed(int id) {
                HandleMenuItem(id);
            }
        };
        CircularMenuWidget cmw= new CircularMenuWidget(this, true, false, callback);
        container.addView(cmw);

        ActionBar actionbar = getSupportActionBar();
        Animations.AnimateActionBar(getApplicationContext(), actionbar,2000);
    }

    private void HandleMenuItem(int id) {
        Intent intent=null;

        switch (id) {
            case 1:
                intent= new Intent(getApplicationContext(), TestActivity.class);
                break;
            case 2:
                Log.i(TAG , "HandleMenuItem: tel");
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:123"));

                if (getApplicationContext().checkSelfPermission(
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                break;
            default:
                Toaster("Not implemented yet!");
                break;
        }
        if(intent!=null)
            startActivity(intent);
    }

    private void Toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}