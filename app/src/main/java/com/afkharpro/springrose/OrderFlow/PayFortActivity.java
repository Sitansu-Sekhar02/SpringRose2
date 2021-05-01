package com.afkharpro.springrose.OrderFlow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.R;
import com.payfort.start.Card;
import com.payfort.start.Start;
import com.payfort.start.Token;
import com.payfort.start.TokenCallback;
import com.payfort.start.error.CardVerificationException;
import com.payfort.start.error.StartApiException;

import java.util.EnumSet;

public class PayFortActivity extends AppCompatActivity implements TokenCallback {
    private static final String API_OPEN_KEY = "live_open_k_55e06cde7fe8d3141a7e";
    private static final String LOG_TAG = PayFortActivity.class.getSimpleName();
    App app = App.getInstance();
    private EditText numberEditText;
    private EditText monthEditText;
    private EditText yearEditText;
    private EditText cvcEditText;
    private EditText ownerEditText;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Button payButton;
    Start start = new Start(API_OPEN_KEY);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fort);
        numberEditText = (EditText) findViewById(R.id.numberEditText);
        monthEditText = (EditText) findViewById(R.id.monthEditText);
        yearEditText = (EditText) findViewById(R.id.yearEditText);
        cvcEditText = (EditText) findViewById(R.id.cvcEditText);
        ownerEditText = (EditText) findViewById(R.id.ownerEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        payButton = (Button) findViewById(R.id.payButton);
        Typeface face = Typeface.createFromAsset(getAssets(),"tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Payment");
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        tv.setTextSize((float) 24);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);
    }


    public void pay(View view) {
        try {
            Card card = unbindCard();

            errorTextView.setText(null);
            hideKeyboard();
            showProgress(true);

            start.createToken(this, card, this, 10 * 100, app.getCurrency());
        } catch (CardVerificationException e) {
            setErrors(e.getErrorFields());
        }
    }

    private Card unbindCard() throws CardVerificationException {
        clearErrors();
        String number = unbindString(numberEditText);
        int year = unbindInteger(yearEditText);
        int month = unbindInteger(monthEditText);
        String cvc = unbindString(cvcEditText);
        String owner = unbindString(ownerEditText);
        return new Card(number, cvc, month, year, owner);
    }

    private void clearErrors() {
        numberEditText.setError(null);
        monthEditText.setError(null);
        yearEditText.setError(null);
        cvcEditText.setError(null);
        ownerEditText.setError(null);
    }

    private void setErrors(EnumSet<Card.Field> errors) {
        String error = "invalid";

        if (errors.contains(Card.Field.NUMBER)) {
            numberEditText.setError(error);
        }
        if (errors.contains(Card.Field.EXPIRATION_YEAR)) {
            yearEditText.setError(error);
        }
        if (errors.contains(Card.Field.EXPIRATION_MONTH)) {
            monthEditText.setError(error);
        }
        if (errors.contains(Card.Field.CVC)) {
            cvcEditText.setError(error);
        }
        if (errors.contains(Card.Field.OWNER)) {
            ownerEditText.setError(error);
        }
    }

    private String unbindString(EditText editText) {
        return editText.getText().toString().trim();
    }

    private int unbindInteger(EditText editText) {
        try {
            String text = unbindString(editText);
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showProgress(boolean progressVisible) {
        payButton.setEnabled(!progressVisible);
        progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(Token token) {
        Log.d(LOG_TAG, "Token is received: " + token);
        Toast.makeText(this, "Congrads", Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onError(StartApiException e) {
        Log.e(LOG_TAG, "Error getting token", e);
        errorTextView.setText("error");
        showProgress(false);
    }

    @Override
    public void onCancel() {
        Log.e(LOG_TAG, "Getting token is canceled by user");
        showProgress(false);
    }
}
