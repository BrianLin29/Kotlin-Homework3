package com.example.lab9_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnCalculate;
    private EditText edHeight, edWeight, edAge;
    private TextView tvWeightResult, tvFatResult, tvBmiResult, tvProgress;
    private ProgressBar progressBar;
    private LinearLayout llProgress;
    private RadioButton btnBoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCalculate = findViewById(R.id.btnCalculate);
        edHeight = findViewById(R.id.edHeight);
        edWeight = findViewById(R.id.edWeight);
        edAge = findViewById(R.id.edAge);
        tvWeightResult = findViewById(R.id.tvWeightResult);
        tvFatResult = findViewById(R.id.tvFatResult);
        tvBmiResult = findViewById(R.id.tvBmiResult);
        tvProgress = findViewById(R.id.tvProgress);
        progressBar = findViewById(R.id.progressBar);
        llProgress = findViewById(R.id.llProgress);
        btnBoy = findViewById(R.id.btnBoy);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edHeight.getText().toString().isEmpty()) {
                    showToast("請輸入身高");
                } else if (edWeight.getText().toString().isEmpty()) {
                    showToast("請輸入體重");
                } else if (edAge.getText().toString().isEmpty()) {
                    showToast("請輸入年齡");
                } else {
                    runThread();
                }
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 用 Java Thread + runOnUiThread 模擬檢測過程
    private void runThread() {
        tvWeightResult.setText("標準體重\n無");
        tvFatResult.setText("體脂肪\n無");
        tvBmiResult.setText("BMI\n無");

        progressBar.setProgress(0);
        tvProgress.setText("0%");
        llProgress.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress < 100) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {
                    }
                    progress++;
                    final int currentProgress = progress;
                    // 切換到 Main Thread 執行進度更新
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(currentProgress);
                            tvProgress.setText(currentProgress + "%");
                        }
                    });
                }

                double height = Double.parseDouble(edHeight.getText().toString());
                double weight = Double.parseDouble(edWeight.getText().toString());
                double age = Double.parseDouble(edAge.getText().toString());
                final double bmi = weight / Math.pow(height / 100.0, 2);

                final double standWeight;
                final double bodyFat;
                if (btnBoy.isChecked()) {
                    standWeight = (height - 80) * 0.7;
                    bodyFat = 1.39 * bmi + 0.16 * age - 19.34;
                } else {
                    standWeight = (height - 70) * 0.6;
                    bodyFat = 1.39 * bmi + 0.16 * age - 9;
                }

                // 切換到 Main Thread 更新畫面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llProgress.setVisibility(View.GONE);
                        tvWeightResult.setText(String.format("標準體重 \n%.2f", standWeight));
                        tvFatResult.setText(String.format("體脂肪 \n%.2f", bodyFat));
                        tvBmiResult.setText(String.format("BMI \n%.2f", bmi));
                    }
                });
            }
        }).start();
    }
}
