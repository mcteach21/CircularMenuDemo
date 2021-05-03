package mchou.apps.main;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


import mchou.apps.main.widgets.CircularMenuWidget;
import mchou.apps.main.widgets.OnItemMenuPressed;
import mchou.apps.main.tools.Animations;


public class MainActivity extends AppCompatActivity {

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

        // Animations.AnimateActionBar(getApplicationContext(), getActionBar(),2000);
    }

    private void HandleMenuItem(int id) {
        Intent intent=null;

        switch (id) {
            case 1:
                intent= new Intent(getApplicationContext(), TestActivity.class);
                break;
            case 2:
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:123"));

               /* if (context.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }*/
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                break;
            case 4:
                intent = new Intent(getApplicationContext(), TestActivity.class);
                break;
            case 5:
                intent = new Intent(getApplicationContext(), TestActivity.class);
                break;
            default:
                //Toaster("Not implemented yet!");
                break;
        }
        if(intent!=null)
            startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent= new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
        return true;
    }

    private void Toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}