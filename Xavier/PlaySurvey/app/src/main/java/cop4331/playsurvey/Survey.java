package cop4331.playsurvey;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;


public class Survey extends ActionBarActivity {

    private ViewPager pager;
    private PagerAdapter adapter;

    private LinkedList<String> strings;

    private int numPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        strings = new LinkedList<>();
        strings.add("hello world");
        strings.add("my name is");
        strings.add("Xavier Banks");

        numPages = strings.size();

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


//        System.err.append(findViewById(R.id.fillinTextView).toString());
    }

    @Override
    public void onBackPressed()
    {
        if(pager.getCurrentItem() == 0)
        {
            super.onBackPressed();
        }
        else
        {
            pager.setCurrentItem(pager.getCurrentItem() - 1, true );
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {

        public ScreenSlidePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            TextView view = (TextView) findViewById(R.id.fillinTextView);
//            view.setText(strings.get(position));
            System.err.append("GetItem");
            return SurveyComponent.newInstance(R.layout.survey_component);
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey, menu);
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

    public void back(View view)
    {
        finish();
    }
}
