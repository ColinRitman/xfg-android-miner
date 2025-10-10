// Copyright (c) 2021 Scala
// Copyright (c) 2020, uPlexa
// Please see the included LICENSE file for more information.

// Note: This file contains some code taken from Scala, a project that had
// forked uPlexa's original android miner and stripped all copyright and
// and released the miner as their own without any credit to the uPlexa
// contributors. Since then, the only thing the Scala team has completed in their original
// whitepaper from 2018 is the android miner (after we were able to
// get one working for them) Their new UI is shiny, and thus, some of their code has
// been used.

package xfg.androidminer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class WizardPoolActivity extends BaseActivity {
    private static final String LOG_TAG = "WizardPoolActivity";

    private int selectedPoolIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        setContentView(R.layout.fragment_wizard_pool);

        View view = findViewById(android.R.id.content).getRootView();

        RequestQueue queue = Volley.newRequestQueue(this);

        // LOUD Mining (NodeJS pool API)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://loudmining.com/xfg/api/pool/stats",
                response -> {
                    try {
                        Log.i(LOG_TAG, "response: " + response);

                        JSONObject obj = new JSONObject(response);
                        JSONObject objStats = obj.getJSONObject("pool_statistics");

                        TextView tvMinersGNTL = view.findViewById(R.id.minersuPlexa);
                        tvMinersGNTL.setText(String.format("%s %s", objStats.getString("miners"), getResources().getString(R.string.miners)));

                        TextView tvHrGNTL = view.findViewById(R.id.hruPlexa);
                        float fHrGNTL = Utils.convertStringToFloat(objStats.getString("hashRate")) / 1000.0f;
                        tvHrGNTL.setText(String.format("%s kH/s", new DecimalFormat("##.#").format(fHrGNTL)));

                    } catch (Exception e) {
                        //Do nothing
                    }
                }
        , this::parseVolleyError);

        queue.add(stringRequest);
    }

    private void parseVolleyError(VolleyError error) {
        // Do nothing
    }

    public void onClickuPlexa(View view) {
        View view2 = findViewById(android.R.id.content).getRootView();

        selectedPoolIndex = 0;

        LinearLayout lluPlexa = view2.findViewById(R.id.lluPlexa);
        int bottom = lluPlexa.getPaddingBottom();
        int top = lluPlexa.getPaddingTop();
        int right = lluPlexa.getPaddingRight();
        int left = lluPlexa.getPaddingLeft();
        lluPlexa.setBackgroundResource(R.drawable.corner_radius_lighter_border_blue);
        lluPlexa.setPadding(left, top, right, bottom);

        LinearLayout llMR = view2.findViewById(R.id.llMR);
        bottom = llMR.getPaddingBottom();
        top = llMR.getPaddingTop();
        right = llMR.getPaddingRight();
        left = llMR.getPaddingLeft();
        llMR.setBackgroundResource(R.drawable.corner_radius_lighter);
        llMR.setPadding(left, top, right, bottom);

        LinearLayout llHM = view2.findViewById(R.id.llHM);
        bottom = llHM.getPaddingBottom();
        top = llHM.getPaddingTop();
        right = llHM.getPaddingRight();
        left = llHM.getPaddingLeft();
        llHM.setBackgroundResource(R.drawable.corner_radius_lighter);
        llHM.setPadding(left, top, right, bottom);

        LinearLayout llGNTL = view2.findViewById(R.id.llGNTL);
        bottom = llGNTL.getPaddingBottom();
        top = llGNTL.getPaddingTop();
        right = llGNTL.getPaddingRight();
        left = llGNTL.getPaddingLeft();
        llGNTL.setBackgroundResource(R.drawable.corner_radius_lighter);
        llGNTL.setPadding(left, top, right, bottom);
    }

    public void onClickMR(View view) { /* no-op, single pool */ }
    public void onClickHM(View view) { /* no-op, single pool */ }
    public void onClickGNTL(View view) { /* no-op, single pool */ }

    public void onNext(View view) {
        Config.write("selected_pool", Integer.toString(selectedPoolIndex));

        startActivity(new Intent(WizardPoolActivity.this, WizardSettingsActivity.class));
        finish();
    }
}
