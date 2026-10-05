package com.vuongquangminh.bmicalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etWeight, etHeight;
    private MaterialButton btnCalculate;
    private MaterialCardView cardResult;
    private TextView tvBmiResult, tvBmiCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        btnCalculate.setOnClickListener(v -> calculateBMI());
    }

    private void initViews() {
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculate = findViewById(R.id.btnCalculate);
        cardResult = findViewById(R.id.cardResult);
        tvBmiResult = findViewById(R.id.tvBmiResult);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);
    }

    private void calculateBMI() {
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();

        // Validation 1: Check to leave blank
        if (weightStr.isEmpty()) {
            etWeight.setError("Please enter your weight!");
            etWeight.requestFocus();
            return;
        }

        if (heightStr.isEmpty()) {
            etHeight.setError("Please enter your height!");
            etHeight.requestFocus();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double heightCm = Double.parseDouble(heightStr);

            // Validation 2: Check if the value is > 0
            if (weight <= 0) {
                etWeight.setError("Weight must be greater than 0!");
                etWeight.requestFocus();
                return;
            }

            if (heightCm <= 0) {
                etHeight.setError("Height must be greater than 0!");
                etHeight.requestFocus();
                return;
            }

            // Calculate BMI
            double heightM = heightCm / 100.0;
            double bmi = weight / (heightM * heightM);

            // Show results
            tvBmiResult.setText(String.format(Locale.getDefault(), "%.1f", bmi));
            evaluateBMI(bmi);
            cardResult.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
        }
    }

    private void evaluateBMI(double bmi) {
        String category;
        int color;

        if (bmi < 18.5) {
            category = "Underweight";
            color = Color.parseColor("#3B82F6");
        } else if (bmi < 25.0) {
            category = "Normal";
            color = Color.parseColor("#10B981");
        } else if (bmi < 30.0) {
            category = "Overweight";
            color = Color.parseColor("#F59E0B");
        } else {
            category = "Obese";
            color = Color.parseColor("#EF4444");
        }

        tvBmiCategory.setText(category);
        tvBmiCategory.setTextColor(color);
    }
}