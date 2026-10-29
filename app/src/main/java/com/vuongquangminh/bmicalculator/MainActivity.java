package com.vuongquangminh.bmicalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rgUnit;
    private TextInputLayout tilWeight, tilHeight;
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

        // Listen for unit conversion events to update the hint label accordingly.
        rgUnit.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMetric) {
                tilWeight.setHint("Weight (kg)");
                tilHeight.setHint("Height (cm)");
            } else {
                tilWeight.setHint("Weight (lbs)");
                tilHeight.setHint("Height (inches)");
            }
            // Delete old data or hide results when changing the unit of measurement
            cardResult.setVisibility(View.GONE);
        });

        btnCalculate.setOnClickListener(v -> calculateBMI());
    }

    private void initViews() {
        rgUnit = findViewById(R.id.rgUnit);
        tilWeight = findViewById(R.id.tilWeight);
        tilHeight = findViewById(R.id.tilHeight);
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
            double weightInput = Double.parseDouble(weightStr);
            double heightInput = Double.parseDouble(heightStr);

            // Validation 2: Check if the value is > 0
            if (weightInput <= 0) {
                etWeight.setError("Weight must be greater than 0!");
                etWeight.requestFocus();
                return;
            }

            if (heightInput <= 0) {
                etHeight.setError("Height must be greater than 0!");
                etHeight.requestFocus();
                return;
            }

            double bmi;
            double heightMForIdeal; // Used to calculate the ideal weight range based on the standard metric system (kg & meters)

            // Check whether the Metric or Imperial system is selected.
            if (rgUnit.getCheckedRadioButtonId() == R.id.rbMetric) {
                // Metric System: weight (kg), height (cm)
                double heightM = heightInput / 100.0;
                bmi = weightInput / (heightM * heightM);
                heightMForIdeal = heightM;
            } else {
                // Imperial: weight (lbs), height (inches)
                // Standard Imperial BMI formula = 703 * weight / (height)^2
                bmi = 703 * weightInput / (heightInput * heightInput);

                // Convert height from inches to meters to calculate the ideal weight range in kilograms
                heightMForIdeal = (heightInput * 0.0254);
            }

            // Show results
            tvBmiResult.setText(String.format(Locale.getDefault(), "%.1f", bmi));
            evaluateBMI(bmi, heightMForIdeal);
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
            advice = "💡 Advice: You should check with a doctor or nutrition expert to get a weight loss plan.";
            color = Color.parseColor("#EF4444");
        }

        // Calculate ideal weight range
        double minWeight = 18.5 * (heightM * heightM);
        double maxWeight = 24.9 * (heightM * heightM);

        String idealRangeStr = String.format(Locale.getDefault(), "Ideal weight range: %.1f kg - %.1f kg", minWeight, maxWeight);

        tvBmiCategory.setText(category);
        tvBmiCategory.setTextColor(color);
        tvIdealWeight.setText(idealRangeStr);
        tvAdvice.setText(advice);
    }
}