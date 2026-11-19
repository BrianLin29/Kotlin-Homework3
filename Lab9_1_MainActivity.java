package com.example.lab9_1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int progressRabbit = 0;
    private int progressTurtle = 0;

    private Button btnStart;
    private SeekBar sbRabbit;
    private SeekBar sbTurtle;

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

        btnStart = findViewById(R.id.btnStart);
        sbRabbit = findViewById(R.id.sbRabbit);
        sbTurtle = findViewById(R.id.sbTurtle);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setEnabled(false);
                progressRabbit = 0;
                progressTurtle = 0;
                sbRabbit.setProgress(0);
                sbTurtle.setProgress(0);
                runRabbit();
                runTurtle();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // Java Thread 不可直接更新 UI，必須建立 Handler 接收 Message
    private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                sbRabbit.setProgress(progressRabbit);
                if (progressRabbit >= 100 && progressTurtle < 100) {
                    showToast("兔子勝利");
                    btnStart.setEnabled(true);
                }
            } else if (msg.what == 2) {
                sbTurtle.setProgress(progressTurtle);
                if (progressTurtle >= 100 && progressRabbit < 100) {
                    showToast("烏龜勝利");
                    btnStart.setEnabled(true);
                }
            }
            return true;
        }
    });

    // 使用 Java Thread + Thread.sleep() 模擬兔子移動
    private void runRabbit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean[] sleepProbability = {true, true, false};
                Random random = new Random();
                while (progressRabbit < 100 && progressTurtle < 100) {
                    try {
                        Thread.sleep(100); // 阻塞執行緒 0.1 秒，必須使用 try-catch
                        if (sleepProbability[random.nextInt(3)]) {
                            Thread.sleep(300); // 兔子偷懶 0.3 秒
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressRabbit += 3;

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 使用 Java Thread + Thread.sleep() 模擬烏龜移動
    private void runTurtle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressTurtle < 100 && progressRabbit < 100) {
                    try {
                        Thread.sleep(100); // 阻塞執行緒 0.1 秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressTurtle += 1;

                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
}
