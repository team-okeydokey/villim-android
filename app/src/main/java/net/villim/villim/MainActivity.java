package net.villim.villim;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private String[] tabItems;
    private CharSequence toolBarTitle;
    private RelativeLayout bottomBar;
    private Button bottomButtonRight;
    private Button bottomButtonLeft;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

//        bottomBar = (RelativeLayout) findViewById(R.id.bottom_bar);
//        bottomButtonLeft = (Button) findViewById(R.id.bottom_button_left);
//        bottomButtonRight = (Button) findViewById(R.id.bottom_button_right);
//        bottomBar.setVisibility(View.INVISIBLE);

        /* Bototm tab */
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabItems = getResources().getStringArray(R.array.tab_items);
        // Set default screen to 방 찾기.
        toolBarTitle = tabItems[0];
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolBarTitle = tabItems[position];
                setTitle(toolBarTitle);
            }

        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabItems.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ExploreFragment.newInstance();
                case 1:
                    return MyRoomFragment.newInstance();
                case 2:
                    return ProfileFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabItems[position];
        }
    }


//    private void selectTab(int position) {
//        // Insert Fragment.
//        Fragment fragment = null;
//        Class fragmentClass;
//
//        switch (position) {
//            case 0:
//                fragmentClass = ProfileFragment.class;
//                break;
//            case 1:
//                fragmentClass = ExploreFragment.class;
//                break;
//            case 2:
//                fragmentClass = MyRoomFragment.class;
//                break;
//            case 3:
//                fragmentClass = VisitFragment.class;
//                break;
//            default:
//                fragmentClass = PolicyFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        // Pop all back stack and call fragment.
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        fragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit();
//    }

    @Override
    public void setTitle(CharSequence title) {
        toolBarTitle = title;
        getSupportActionBar().setTitle(toolBarTitle);
    }

//    @Override
//    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
//        if (fragment instanceof PasscodeFragment) {
//            animateBottomButton(true, true, false);
////            animateBottomButton(false, true, true);
//        }
//        super.onBackPressed();
//    }


    /* Bottom button interfaces */

//    public void showBottomButtons(boolean left, boolean right) {
//        int barVisibility = left | right ? View.VISIBLE : View.GONE;
//        int leftButtonVisibility = left ? View.VISIBLE : View.INVISIBLE;
//        int rightButtonVisibility = right ? View.VISIBLE : View.INVISIBLE;
//        bottomBar.setVisibility(barVisibility);
//        bottomButtonLeft.setVisibility(leftButtonVisibility);
//        bottomButtonRight.setVisibility(rightButtonVisibility);
//    }
//
//    public void setBottomButtonText(boolean left, CharSequence text) {
//        Button button = left ? bottomButtonLeft : bottomButtonRight;
//        button.setText(text);
//    }

    public void animateBottomButton(boolean left, boolean shrink, boolean expand) {
        final Animation circleShrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.circle_shrink);
        final Animation circleExpand = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.circle_expand);

        final Button button = left ? bottomButtonLeft : bottomButtonRight;
        if (shrink && expand) {
            circleShrink.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    button.startAnimation(circleExpand);
                }
            });
            button.startAnimation(circleShrink);
        } else if (shrink) {
            circleShrink.setAnimationListener(null);
            button.startAnimation(circleShrink);
        } else if (expand) {
            button.startAnimation(circleExpand);
        }
    }

//    public void registerBottomButtonListener(boolean left, View.OnClickListener listener) {
//        Button button = left ? bottomButtonLeft : bottomButtonRight;
//        button.setOnClickListener(listener);
//    }


}
