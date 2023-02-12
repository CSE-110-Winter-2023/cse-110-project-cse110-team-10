package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity {
    static int radius;

    /*
     * TODO: Update locations and angles for compass_N, compass_E, compass_S, compass_W
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        final ImageView compass = (ImageView) findViewById(R.id.compass_face);
        ViewTreeObserver observer = compass.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TextView north = (TextView) findViewById(R.id.compass_N);
                TextView east = (TextView) findViewById(R.id.compass_E);
                TextView south = (TextView) findViewById(R.id.compass_S);
                TextView west = (TextView) findViewById(R.id.compass_W);

                int rad = compass.getHeight() / 2;
                radius = rad;

                ConstraintLayout.LayoutParams north_lp = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                north_lp.circleRadius = rad;
                north.setLayoutParams(north_lp);

                ConstraintLayout.LayoutParams east_lp = (ConstraintLayout.LayoutParams) east.getLayoutParams();
                east_lp.circleRadius = rad;
                east.setLayoutParams(east_lp);

                ConstraintLayout.LayoutParams south_lp = (ConstraintLayout.LayoutParams) south.getLayoutParams();
                south_lp.circleRadius = rad;
                south.setLayoutParams(south_lp);

                ConstraintLayout.LayoutParams west_lp = (ConstraintLayout.LayoutParams) west.getLayoutParams();
                west_lp.circleRadius = rad;
                west.setLayoutParams(west_lp);

                displaySingleLocation(0, rad-64);

                compass.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public void onAddLocationClicked(View view) {
        Intent intent = new Intent(this, NewLocationActivity.class);
        startActivity(intent);
    }

    private void displaySingleLocation(int loc_id, int rad) {
        float deg = 100 /* SavedLocation.getDegrees(loc_id)*/;

        //create view
        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();


        //TODO: Need to switch to CircleView
        TextView loc_view = new TextView(this);
        loc_view.setText("test");
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        cl.addView(loc_view, loc_id);
        loc_view.setId(View.generateViewId());

        cs.clone(cl);

        cs.constrainCircle(loc_view.getId(), R.id.compass_face, rad, deg);

        cs.applyTo(cl);
    }
}