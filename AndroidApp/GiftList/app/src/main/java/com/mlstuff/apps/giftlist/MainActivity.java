package com.mlstuff.apps.giftlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mlstuff.apps.giftlist.Entities.GiftItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Wait for it", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            OnReload();
        } else  if (id == R.id.action_powered) {

        }

        return super.onOptionsItemSelected(item);
    }

    private void OnReload() {
        LinearLayout pickList = (LinearLayout)findViewById(R.id.pickList);
        pickList.removeAllViews();
        new DownloadGiftList().execute("");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            OnReload();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.action_powered) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AddGiftToCurrentList(GiftItem item)
    {
        //Create the container
        LinearLayout someGift = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        someGift.setLayoutParams(params);
        someGift.setClickable(true);
        someGift.setBackground(ContextCompat.getDrawable(this, R.drawable.pick));
        someGift.setOrientation(LinearLayout.VERTICAL);
        //Add the elements
        TextView who = new TextView(this);
        who.setText("Who: "+ item.Who);
        someGift.addView(who);
        TextView why = new TextView(this);
        why.setText("Why: "+ item.Why);
        someGift.addView(why);

        TextView when = new TextView(this);
        when.setText("When: " + item.When);
        someGift.addView(when);

        TextView what = new TextView(this);
        what.setText("What: " + item.What);
        someGift.addView(what);

        LinearLayout pickList = (LinearLayout)findViewById(R.id.pickList);
        pickList.addView(someGift);
    }

    public class DownloadGiftList extends AsyncTask<Object, GiftItem, String> {
        @Override
        protected String doInBackground(Object... params) {

            String url = "http://mlstuff.netau.net/GiftList/MyPicks.php";
            String jsonItems;
            try
            {
                jsonItems = new WebConnection().GetJsonItems(url,"uid=1");
            }
            catch (Exception e)
            {
                return "Error requesting gifts";
            }

            try
            {
                GetGiftsItems(jsonItems);
            }
            catch (JSONException e)
            {
                return "Error getting gifts";
            }
            return "Ok";
        }

        @Override
        protected void onProgressUpdate(GiftItem... gift){
            AddGiftToCurrentList(gift[0]);
        }

        //Executed in the UI thread
        @Override
        protected void onPostExecute(String result) {
            if (result != "Ok") {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
        private void GetGiftsItems(String jsonItems) throws JSONException {

            JSONArray jsonGiftArray = new JSONArray(jsonItems);

            for (int i = 0; i < jsonGiftArray.length(); i++) {

                JSONObject jsonGift = jsonGiftArray.getJSONObject(i);
                GiftItem item= new GiftItem();
                item.Who = jsonGift.getString("Who");
                item.Why = jsonGift.getString("Why");
                item.When = jsonGift.getString("When");
                item.What = jsonGift.getString("What");
                publishProgress(item);
            }
        }
    }
}
