package com.example.thesquapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.ForgotPasswordResult;

public class ResetPassword extends AppCompatActivity {

    private final String TAG = "well hey there";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }

    public void getNewCodePwordBtn(View view){

        final EditText signup_username = findViewById(R.id.signup_username);
        final String username = String.valueOf(signup_username.getText());

        AWSMobileClient.getInstance().forgotPassword(username, new Callback<ForgotPasswordResult>() {
            @Override
            public void onResult(final ForgotPasswordResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "forgot password state: " + result.getState());
                        switch (result.getState()) {
                            case CONFIRMATION_CODE:
                                //makeToast("Confirmation code is sent to reset password");
                                break;
                            default:
                                Log.e(TAG, "un-supported forgot password state");
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "forgot password error", e);
            }
        });
    }

    public void resetPasswordBtn(View view){


        final EditText new_pword = findViewById(R.id.new_pword_enter_new_password);
        final EditText verify_code = findViewById(R.id.new_pword_enter_code);

        final String code = String.valueOf(verify_code.getText());
        final String password = String.valueOf(new_pword.getText());

        AWSMobileClient.getInstance().confirmForgotPassword(password, code, new Callback<ForgotPasswordResult>() {
            @Override
            public void onResult(final ForgotPasswordResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "forgot password state: " + result.getState());
                        switch (result.getState()) {
                            case DONE:
                                //makeToast("Password changed successfully");
                                break;
                            default:
                                Log.e(TAG, "un-supported forgot password state");
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "forgot password error", e);
            }
        });
    }
}
