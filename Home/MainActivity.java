package com.example.budget.views.activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budget.databinding.ActivityHomeBinding;
import com.example.budget.views.fragments.AddTransactionFragment;
import com.example.budget.R;
import com.example.budget.databinding.ActivityHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.material.tabs.TabLayout;




public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.materialToolbar);
        getSupportActionBar().setTitle("Transactions");

        calendar = Calendar.getInstance();
        updateDate();

        // Fix TabLayout access
        TabLayout tabLayout = binding.tabLayout;

        // Modify the "Notes" tab (Index 3)
        TabLayout.Tab notesTab = tabLayout.getTabAt(3);
        if (notesTab != null) {
            notesTab.setText("Updated Notes");
        }

        binding.nextDateBtn.setOnClickListener(c-> {
            calendar.add(Calendar.DATE,1);
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, -1);
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c-> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });
    }



    void updateDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        binding.currentDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
