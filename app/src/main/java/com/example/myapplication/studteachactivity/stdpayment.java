package com.example.myapplication.studteachactivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.myapplication.R;

public class stdpayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdpayment);

        Button bkashPaymentButton = findViewById(R.id.bkashPaymentButton);
        Button nagadPaymentButton = findViewById(R.id.nagadPaymentButton);

        // Handle bKash Payment Button Click
        bkashPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the bKash app is installed
                if (isAppInstalled("com.bKash.customerapp")) {
                    // Open the bKash app
                    openApp("com.bKash.customerapp");
                } else {
                    // Prompt the user to download the bKash app from Google Play
                    showDownloadPrompt("bKash");
                }
            }
        });

        // Handle Nagad Payment Button Click
        nagadPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the Nagad app is installed
                if (isAppInstalled("com.nagad.customerapp")) {
                    // Open the Nagad app
                    openApp("com.nagad.customerapp");
                } else {
                    // Prompt the user to download the Nagad app from Google Play
                    showDownloadPrompt("Nagad");
                }
            }
        });
    }

    private boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void openApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void showDownloadPrompt(String appName) {
        // You can customize the message and action here.
        String message = "The " + appName + " app is not installed. Would you like to download it from Google Play?";
        Uri uri = Uri.parse("market://details?id=" + getAppPackageName(appName));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Show a dialog or toast to ask the user to download the app
        // You can also use a library for a more sophisticated dialog.
    }

    private String getAppPackageName(String appName) {
        if ("bKash".equals(appName)) {
            return "com.bKash.customerapp";
        } else if ("Nagad".equals(appName)) {
            return "com.nagad.customerapp";
        }
        return "";
    }
}
