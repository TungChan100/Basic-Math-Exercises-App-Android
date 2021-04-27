/*
 This file is part of Privacy Friendly Reckoning Skills.
 Privacy Friendly Reckoning Skills is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Reckoning Skills is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Reckoning Skills. If not, see <http://www.gnu.org/licenses/>.
 */

package osyoos.sofeeg.oukop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import osyoos.sofeeg.oukop.R;
import osyoos.sofeeg.oukop.gameInstance;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;

    private boolean add = true;
    private boolean sub = true;
    private boolean mul = true;
    private boolean div = true;
    private int activeOperators = 4;

    private Button addButton;
    private Button subButton;
    private Button mulButton;
    private Button divButton;
    private Button continueButton;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.app_bar_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.scroller);
        if(mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        int index = mSharedPreferences.getInt("lastChosenPage", 0);

        mViewPager.setCurrentItem(index);
        mArrowLeft = (ImageView) findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) findViewById(R.id.arrow_right);

        addButton = (Button) findViewById(R.id.button_add);
        subButton = (Button) findViewById(R.id.button_sub);
        mulButton = (Button) findViewById(R.id.button_mul);
        divButton = (Button) findViewById(R.id.button_div);

        continueButton = (Button) findViewById(R.id.game_button_continue);

        //care for initial postiton of the ViewPager
        mArrowLeft.setVisibility((index==0)?View.INVISIBLE:View.VISIBLE);
        mArrowRight.setVisibility((index==mSectionsPagerAdapter.getCount()-1)?View.INVISIBLE:View.VISIBLE);

        //Update ViewPager on change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mArrowLeft.setVisibility((position==0)?View.INVISIBLE:View.VISIBLE);
                mArrowRight.setVisibility((position==mSectionsPagerAdapter.getCount()-1)?View.INVISIBLE:View.VISIBLE);

                //save position in settings
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("lastChosenPage", position);
                editor.apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences hs = this.getSharedPreferences("pfa-math-highscore", Context.MODE_PRIVATE);
        if(hs.getBoolean("continue",false)){
            continueButton.setBackgroundResource(R.drawable.button_fullwidth);
            continueButton.setEnabled(true);
        } else {
            continueButton.setBackgroundResource(R.drawable.button_disabled);
            continueButton.setEnabled(false);
        }
    }


    public void onClick(View view) {


        switch(view.getId()) {
            case R.id.arrow_left:
                mViewPager.arrowScroll(View.FOCUS_LEFT);
                break;
            case R.id.arrow_right:
                mViewPager.arrowScroll(View.FOCUS_RIGHT);
                break;
            case R.id.button_add:
                if(add==true && activeOperators > 1) {
                    addButton.setTextColor(getResources().getColor(R.color.middlegrey));
                    add = false;
                    activeOperators--;
                } else if(!add) {
                    addButton.setTextColor(getResources().getColor(R.color.red));
                    add = true;
                    activeOperators++;
                } else {
                    oneActiveToast();
                }
                break;
            case R.id.button_sub:
                if(sub==true && activeOperators > 1) {
                    subButton.setTextColor(getResources().getColor(R.color.middlegrey));
                    sub = false;
                    activeOperators--;
                } else if(!sub) {
                    subButton.setTextColor(getResources().getColor(R.color.red));
                    sub = true;
                    activeOperators++;
                } else {
                    oneActiveToast();
                }
                break;
            case R.id.button_mul:
                if(mul==true && activeOperators > 1) {
                    mulButton.setTextColor(getResources().getColor(R.color.middlegrey));
                    mul = false;
                    activeOperators--;
                } else if(!mul) {
                    mulButton.setTextColor(getResources().getColor(R.color.red));
                    mul = true;
                    activeOperators++;
                } else {
                    oneActiveToast();
                }
                break;
            case R.id.button_div:
                if(div==true && activeOperators > 1) {
                    divButton.setTextColor(getResources().getColor(R.color.middlegrey));
                    div = false;
                    activeOperators--;
                } else if(!div) {
                    divButton.setTextColor(getResources().getColor(R.color.red));
                    div = true;
                    activeOperators++;
                } else {
                    oneActiveToast();
                }
                break;
            case R.id.button_start_game:
                Intent intent = new Intent(view.getContext(), ExerciseActivity.class);

                gameInstance game = new gameInstance();
                game.add = add;
                game.sub = sub;
                game.mul = mul;
                game.div = div;
                game.space = mViewPager.getCurrentItem();
                intent.putExtra("game", game);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.game_button_continue:
                Intent cont = new Intent(view.getContext(), ExerciseActivity.class);
                gameInstance game2 = new gameInstance();
                cont.putExtra("continue",true);
                game2.add = add;
                game2.sub = sub;
                game2.mul = mul;
                game2.div = div;
                game2.space = mViewPager.getCurrentItem();
                cont.putExtra("game", game2);
                cont.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cont);

            default:
        }
    }

    void oneActiveToast(){
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.main_button_inactive_toast);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }

    public static class PageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PageFragment newInstance(int sectionNumber) {
            PageFragment fragment = new PageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PageFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int id = 0;
            if(getArguments() != null) {
                id = getArguments().getInt(ARG_SECTION_NUMBER);
            }

            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            switch(id) {
                case(0): textView.setText(getResources().getString(R.string.number_range_one));
                    break;
                case(1): textView.setText(getResources().getString(R.string.number_range_two));;
                    break;
                case(2): textView.setText(getResources().getString(R.string.number_range_three));;
                    break;
                default: textView.setText(getResources().getString(R.string.number_range_four));;
                    break;
            }

            return rootView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;

        switch (item.getItemId()) {




            case R.id.action_about:

                AlertDialog.Builder dialog = new AlertDialog.Builder( this);
                dialog.setTitle("About");
                dialog.setMessage("Basic Math 2021\n\nThis Application is based on Privacy Friendly Reckoning Skills App\n\nsecuso.aifb.kit.edu/english/Reckoning_Skills.php");
                dialog.setPositiveButton("OK", null);
                dialog.create().show();


                return true;
                              /*

          case R.id.action_score:
                intent = new Intent(this, ScoreActivity.class);
                startActivity(intent);
                return true;


            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
                intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
                startActivity(intent);
                return true;

             */
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }

    }
}
