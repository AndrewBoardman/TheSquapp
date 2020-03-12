package com.example.thesquapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUpbtn(View view) {

        final EditText signup_name = findViewById(R.id.signup_name);
        final EditText signup_username = findViewById(R.id.signup_username);
        final EditText signup_email = findViewById(R.id.signup_email);
        final EditText signup_password = findViewById(R.id.signup_password);
        final EditText signup_repeat_password = findViewById(R.id.signup_repeat_password);

        final String username = String.valueOf(signup_username.getText());
        final String password = String.valueOf(signup_password.getText());
        final String email = String.valueOf(signup_email.getText());

        final Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);

        final Map<String, String> attributes = new HashMap<>();
        attributes.put("email", email);

        AWSMobileClient.getInstance().signUp(username, password, attributes, null, new Callback<SignUpResult>() {
            @Override
            public void onResult(final SignUpResult signUpResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                        if (!signUpResult.getConfirmationState()) {
                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                            Log.i(TAG, "balls");
                            //makeToast("Confirm sign-up with: " + details.getDestination());
                            startActivity(intent);
                        } else {
                            //makeToast("Sign-up done.");
                            Log.i(TAG, "balls2");
                            //startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Sign-up error", e);
            }
        });
    }
}
