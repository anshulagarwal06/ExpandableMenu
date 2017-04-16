package android.anshul.com.expendableMenu;

import android.anshul.com.fireview.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import me.anshulagarwal.expandablemenuoption.ExpandableMenuView;


public class MainActivity extends AppCompatActivity {

    private ExpandableMenuView mExpandableMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExpandableMenuView = (ExpandableMenuView) findViewById(R.id.expanded_menu);

        mExpandableMenuView.setOnMenuListener(new ExpandableMenuView.MenuListener() {

            @Override
            public void rightPressed() {
                Toast.makeText(MainActivity.this, "Right Pressed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void leftPressed() {
                Toast.makeText(MainActivity.this, "Left Pressed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void topPressed() {
                Toast.makeText(MainActivity.this, "Top Pressed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void bottomPressed() {
                Toast.makeText(MainActivity.this, "Bottom Pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
