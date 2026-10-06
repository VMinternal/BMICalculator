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
    private TextView tvIdealWeight, tvAdvice;

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
        tvIdealWeight = findViewById(R.id.tvIdealWeight);
        tvAdvice = findViewById(R.id.tvAdvice);
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
            evaluateBMI(bmi, heightM);
            cardResult.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
        }
    }

    private void evaluateBMI(double bmi, double heightM) {
        String category;
        String advice;
        int color;

        if (bmi < 18.5) {
            category = "Underweight";
            advice = "💡 Advice: You should boost your nutrition and do workouts to build muscle.";
            color = Color.parseColor("#3B82F6");
        } else if (bmi < 25.0) {
            category = "Normal";
            advice = "💡 Advice: Awesome! Keep up your current diet and exercise routine.";
            color = Color.parseColor("#10B981");
        } else if (bmi < 30.0) {
            category = "Overweight";
            advice = "💡 Advice: You should cut down on sweets and carbs, and exercise more.";
            color = Color.parseColor("#F59E0B");
        } else {
            category = "Obese";
            advice = " 💡 Advice: You should check with a doctor or nutrition expert to get a weight loss plan.";
            color = Color.parseColor("#EF4444");
        }

        // Calculate ideal weight range
        double minWeight = 18.5 * (heightM * heightM);
        double maxWeight = 24.9 * (heightM * heightM);
        String idealRangeStr = String.format(Locale.getDefault(), "Ideal weight for this height: %.1f kg - %.1f kg", minWeight, maxWeight);

        tvBmiCategory.setText(category);
        tvBmiCategory.setTextColor(color);
        tvIdealWeight.setText(idealRangeStr);
        tvAdvice.setText(advice);
    }
}