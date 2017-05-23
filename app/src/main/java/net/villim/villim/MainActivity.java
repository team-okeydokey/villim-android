package net.villim.villim;

import android.animation.AnimatorSet;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private boolean animateBottomButtons;

    private Toolbar toolBar;
    private String[] drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence actionBarTitle;
    private RelativeLayout bottomBar;
    private Button bottomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animateBottomButtons = false;

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        bottomBar = (RelativeLayout) findViewById(R.id.bottom_bar);
        bottomButton = (Button) findViewById(R.id.bottom_button);
        bottomBar.setVisibility(View.INVISIBLE);

        /* Navigation Drawer. */
        actionBarTitle = getResources().getString(R.string.app_name);
        drawerItems = getResources().getStringArray(R.array.drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerListView = (ListView) findViewById(R.id.drawer_list);
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerItems));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(actionBarTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(actionBarTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener.
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set default screen to 방 찾기.
        selectItem(1);
    }


    /* Listener for navigation drawer items */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            setTitle(drawerItems[position]);
        }
    }

    private void selectItem(int position) {
        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerListView);

        // Insert Fragment.
        Fragment fragment = null;
        Class fragmentClass;

        animateBottomButtons = false;

        switch(position) {
            case 0:
                fragmentClass = ProfileFragment.class;
                break;
            case 1:
                fragmentClass = MyRoomFragment.class;
                break;
            case 2:
                fragmentClass = MyRoomFragment.class;
                break;
            case 3:
                fragmentClass = VisitFragment.class;
                break;
            default:
                fragmentClass = PolicyFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        // Pop all back stack and call fragment.
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        actionBarTitle = title;
        getSupportActionBar().setTitle(actionBarTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void setBottomButton(boolean visible, CharSequence text) {

        int visibility = visible ?  View.VISIBLE : View.INVISIBLE;
        bottomBar.setVisibility(visibility);

        // Animate buttons.
        if (animateBottomButtons) {

            final Animation circleShrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.circle_shrink);
            final Animation circleExpand = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.circle_expand);

            circleShrink.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) { bottomButton.startAnimation(circleExpand); }
            });
            bottomButton.startAnimation(circleShrink);
        }
        bottomButton.setText(text);
    }

    public void setAnimateBottomButtons(boolean animate) {
        animateBottomButtons = animate;
    }

    public void registerBottomButtonListener(View.OnClickListener listener) {
        bottomButton.setOnClickListener(listener);
    }

}
