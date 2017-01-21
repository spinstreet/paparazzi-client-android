package com.spinstreet.paparazzi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GridView gv = (GridView) findViewById(R.id.image_grid);
        String[] images = {
            "http://i.imgur.com/FSDDkMR.jpg",
            "http://i.imgur.com/qQh26DE.jpg",
            "http://i.imgur.com/MZ3srpv.jpg",
            "http://i.imgur.com/fRcsH1Z.jpg"
        };
        gv.setAdapter(new ImageAdapter(this, images));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.set_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}