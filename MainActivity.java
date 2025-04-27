package com.example.budgetbliss;
 
 import android.content.Context;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.*;
 import androidx.appcompat.app.AppCompatActivity;
 public class MainActivity extends AppCompatActivity {
 
     private static final String PREFS_NAME = "BudgetBlissPrefs";
     private static final String PIN_KEY = "user_pin";
 
     private EditText pinEditText;
     Button submitButton ;
     private TextView pinMessage;
 
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
 
         SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
 
 
         pinEditText = findViewById(R.id.pinEditText);
         submitButton = findViewById(R.id.submitButton);
         pinMessage = findViewById(R.id.pinMessage);
 
         String savedPin = sharedPreferences.getString(PIN_KEY, null);
 
         if (savedPin == null) {
             pinMessage.setText(getString(R.string.create_pin_message));
         } else {
             pinMessage.setText(getString(R.string.enter_pin_message));
         }
 
         submitButton.setOnClickListener(v -> {
             String inputPin = pinEditText.getText().toString();
 
             if (inputPin.length() != 4 || !inputPin.matches("\\d{4}")) {
                 Toast.makeText(this, getString(R.string.invalid_pin), Toast.LENGTH_SHORT).show();
                 return;
             }
 
             if (savedPin == null) {
                 sharedPreferences.edit().putString(PIN_KEY, inputPin).apply();
                 Toast.makeText(this, getString(R.string.pin_saved), Toast.LENGTH_SHORT).show();
                 pinMessage.setText(getString(R.string.enter_pin_message));
                 pinEditText.setText("");
             } else {
                 if (inputPin.equals(savedPin)) {
                     Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                     // Proceed to next screen
                 } else {
                     Toast.makeText(this, getString(R.string.incorrect_pin), Toast.LENGTH_SHORT).show();
                 }
             }
         });
     }
 }
