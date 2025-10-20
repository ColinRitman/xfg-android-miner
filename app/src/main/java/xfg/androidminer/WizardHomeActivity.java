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

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WizardHomeActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        setContentView(R.layout.fragment_wizard_home);

        View view = findViewById(android.R.id.content).getRootView();

        String sDisclaimerText = getResources().getString(R.string.disclaimer_agreement);
        String sDiclaimer = getResources().getString(R.string.disclaimer);

        SpannableString ss = new SpannableString(sDisclaimerText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                showDisclaimer();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int iStart = sDisclaimerText.indexOf(sDiclaimer);
        int iEnd = iStart + sDiclaimer.length();
        ss.setSpan(clickableSpan, iStart, iEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tvDisclaimer = view.findViewById(R.id.disclaimer);
        tvDisclaimer.setText(ss);
        tvDisclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        tvDisclaimer.setLinkTextColor(getResources().getColor(R.color.c_orange));
        tvDisclaimer.setHighlightColor(Color.TRANSPARENT);
    }

    public void onEnterAddress(View view) {
        startActivity(new Intent(WizardHomeActivity.this, WizardAddressActivity.class));
        finish();
    }

    public void onCreateWallet(View view) {
        // Show loading dialog first
        android.app.AlertDialog loadingDialog = new android.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.generating_wallet))
            .setMessage(getString(R.string.generating_wallet_message))
            .setCancelable(false)
            .create();
        loadingDialog.show();
        
        // Generate wallet in background thread to avoid blocking UI
        new Thread(() -> {
            String newAddress = Utils.generatePaperWallet();
            
            // Run UI updates on main thread
            runOnUiThread(() -> {
                loadingDialog.dismiss();
                
                // Show dialog with the generated address
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.generated_wallet_title));
                builder.setMessage(String.format(getString(R.string.generated_wallet_message), newAddress));
                builder.setPositiveButton(getString(R.string.use_this_address), (dialog, which) -> {
                    // Copy the address and proceed to address screen
                    Utils.copyToClipboard("Fuego Paper Wallet", newAddress);
                    startActivity(new Intent(WizardHomeActivity.this, WizardAddressActivity.class));
                    finish();
                });
                builder.setNegativeButton(getString(R.string.copy_and_close), (dialog, which) -> {
                    Utils.copyToClipboard("Fuego Paper Wallet", newAddress);
                    android.widget.Toast.makeText(this, getString(R.string.address_copied_to_clipboard), android.widget.Toast.LENGTH_SHORT).show();
                });
                builder.setNeutralButton(getString(R.string.cancel), null);
                
                // Auto-copy the address when dialog is shown
                Utils.copyToClipboard("Fuego Paper Wallet", newAddress);
                
                builder.show();
            });
        }).start();
    }

    public void onSkip(View view) {
        startActivity(new Intent(WizardHomeActivity.this, MainActivity.class));
        finish();

        Config.write("hide_setup_wizard", "1");
    }

    private void showDisclaimer() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.disclaimer);
        dialog.setTitle("Disclaimer");
        dialog.setCancelable(false);

        Button btnOK = dialog.findViewById(R.id.btnAgree);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.write("disclaimer_agreed", "1");
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
