package com.example.budgetbliss;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.Toast;


import com.example.budgetbliss.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Refresh data when returning to this activity
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        transactionModelArrayList = new ArrayList<>();


        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);



        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddTransactionActivity.class);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        // Add logout button click listener
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase Auth
                firebaseAuth.signOut();

                // Show a toast message
                Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect to login activity
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loadData();


    }


    private void loadData() {
        firebaseFirestore.collection("DataHistory").document(firebaseAuth.getUid()).collection("Notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                transactionModelArrayList.clear(); // Clear existing data
                double totalIncome = 0;
                double totalExpense = 0;

                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot ds : task.getResult()) {
                        TransactionModel model = new TransactionModel(
                                ds.getString("id"),
                                ds.getString("note"),
                                ds.getString("amount"),
                                ds.getString("type"),
                                ds.getString("date"));

                        transactionModelArrayList.add(model);

                        // Calculate totals
                        try {
                            double amount = Double.parseDouble(model.getAmount());
                            if ("Income".equals(model.getType())) {
                                totalIncome += amount;
                            } else if ("Expense".equals(model.getType())) {
                                totalExpense += amount;
                            }
                        } catch (NumberFormatException e) {
                            // Handle parsing error
                        }
                    }

                    // Update UI with totals
                    binding.totalIncome.setText(String.format("$%.2f", totalIncome));
                    binding.totalExpense.setText(String.format("$%.2f", totalExpense));
                    binding.totalBalance.setText(String.format("$%.2f", totalIncome - totalExpense));

                    // Update adapter
                    transactionAdapter = new TransactionAdapter(DashboardActivity.this, transactionModelArrayList);
                    binding.historyRecyclerView.setAdapter(transactionAdapter);


                }

            }
        });
    }
}
