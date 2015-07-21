package android.anshul.com.expendableMenu;

import android.anshul.com.fireview.R;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ExpandableMenuView.MenuListener {


    private ExpandableMenuView mExpandableMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExpandableMenuView = (ExpandableMenuView) findViewById(R.id.fire_view);
        mExpandableMenuView.setOnMenuListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void rightPressed() {
        Toast.makeText(this, "Right Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void leftPressed() {
        Toast.makeText(this, "Left Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void topPressed() {
        Toast.makeText(this, "Top Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bottomPressed() {
        Toast.makeText(this, "Bottom Pressed", Toast.LENGTH_SHORT).show();
    }
}
