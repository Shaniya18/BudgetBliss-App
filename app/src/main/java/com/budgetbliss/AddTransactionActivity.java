package com.example.budgetbliss;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetbliss.databinding.ActivityAddTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {

    ActivityAddTransactionBinding binding;
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Checkbox listeners
        binding.expenseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Expense";
                binding.expenseCheckBox.setChecked(true);
                binding.incomeCheckBox.setChecked(false);
            }
        });

        binding.incomeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Income";
                binding.expenseCheckBox.setChecked(false);
                binding.incomeCheckBox.setChecked(true);
            }
        });

        // Add transaction button
        binding.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.userAmountAdd.getText().toString().trim();
                String note = binding.userNoteAdd.getText().toString().trim();

                if (amount.isEmpty()) {
                    binding.userAmountAdd.setError("Amount is required");
                    return;
                }

                if (type.isEmpty()) {
                    Toast.makeText(AddTransactionActivity.this, "Please select transaction type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (note.isEmpty()) {
                    note = "No description"; // Default note if empty
                }

                // Continue with adding transaction...
                String id = UUID.randomUUID().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());


                // Transaction map
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("id", id);
                transaction.put("amount", amount);
                transaction.put("note", note);
                transaction.put("type", type);
                transaction.put("date", currentDate);

                // Save to Firestore
                fStore.collection("DataHistory").document(firebaseAuth.getUid()).collection("Notes").document(id)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddTransactionActivity.this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                                // Clear fields
                                binding.userAmountAdd.setText("");
                                binding.userNoteAdd.setText("");
                                binding.expenseCheckBox.setChecked(false);
                                binding.incomeCheckBox.setChecked(false);
                                type = "";

                                // Finish activity
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTransactionActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
