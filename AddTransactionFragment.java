package com.example.budget.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.budget.R;
import com.example.budget.adapters.CategoryAdapter;
import com.example.budget.databinding.FragmentAddTransactionBinding;
import com.example.budget.databinding.ListDialogBinding;
import com.example.budget.models.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    FragmentAddTransactionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));
                });


        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                        String dateToShow = dateFormat.format(calendar.getTime());

                        binding.date.setText(dateToShow);
                    }
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(c-> {

            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category("Salary", R.drawable.salary, categoryColor));
            categories.add(new Category("Business", R.drawable.business));
            categories.add(new Category("Investment", R.drawable.invest));
            categories.add(new Category("Loan", R.drawable.loan));
            categories.add(new Category("Rent", R.drawable.rent));
            categories.add(new Category("Other", R.drawable.others));

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

            categoryDialog.show();

        });

        return binding.getRoot();
    }
}
