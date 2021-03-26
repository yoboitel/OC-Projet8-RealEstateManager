package com.openclassrooms.realestatemanager.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;

public class FragmentLoan extends Fragment {

    private TextInputLayout etLoanAmount, etLoanTerm, etInterestRate, etMonthlyPay, etTotalInterest;
    private FloatingActionButton fabResult;

    public FragmentLoan() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loan, container, false);

        initialization(v);

        fabResult.setOnClickListener(view -> {

            //Display toast if one of the fields is empty.
            if (etLoanAmount.getEditText().getText().toString().isEmpty() || etLoanTerm.getEditText().getText().toString().isEmpty() || etInterestRate.getEditText().getText().toString().isEmpty())
                Toast.makeText(getActivity(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            //Display toast if the term or interest is filled with "0".
            else if (etLoanTerm.getEditText().getText().toString().equals("0") || etInterestRate.getEditText().getText().toString().equals("0"))
                Toast.makeText(getActivity(), "Can't be filled with 0", Toast.LENGTH_SHORT).show();
            //Call the respective methods to calculate the monthly pay and total interest costs.
            else {
                etMonthlyPay.getEditText().setText(String.format("%.2f", calculateLoanMonthlyPay(Integer.valueOf(etLoanAmount.getEditText().getText().toString()), Integer.valueOf(etLoanTerm.getEditText().getText().toString()), Double.valueOf(etInterestRate.getEditText().getText().toString()))));
                etTotalInterest.getEditText().setText(String.format("%.2f", calculateLoanTotalInterest(Integer.valueOf(etLoanAmount.getEditText().getText().toString()), Integer.valueOf(etLoanTerm.getEditText().getText().toString()), Double.valueOf(etInterestRate.getEditText().getText().toString()))));
            }
        });
        return v;
    }

    public void initialization(View v){
        etLoanAmount = v.findViewById(R.id.textInputLayoutLoanAmount);
        etLoanTerm = v.findViewById(R.id.textInputLayoutLoanTerm);
        etInterestRate = v.findViewById(R.id.textInputLayoutLoanInterest);
        etMonthlyPay = v.findViewById(R.id.textInputLayoutLoanMonthlyPay);
        etTotalInterest = v.findViewById(R.id.textInputLayoutLoanTotalInterest);
        fabResult = v.findViewById(R.id.fabLoanResult);
    }

    //Calculate Monthly Pay Cost
    public static Double calculateLoanMonthlyPay(Integer loanAmount, Integer loanTerm, Double loanInterestRate){
        return loanAmount * ((loanInterestRate / (100)) / (12)) / (1 - Math.pow(1 + ((loanInterestRate / 100) / 12), -loanTerm * 12));
    }

    //Calculate Total Interest Cost
    public static Double calculateLoanTotalInterest(Integer loanAmount,Integer loanTerm, Double loanInterestRate){
        return 12 * loanTerm * calculateLoanMonthlyPay(loanAmount, loanTerm, loanInterestRate) - loanAmount;
    }
}

