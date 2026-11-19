package com.example.lab7;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

        // 宣告元件
        Spinner spinner = findViewById(R.id.spinner);
        ListView listView = findViewById(R.id.listView);
        GridView gridView = findViewById(R.id.gridView);

        // 儲存購買數量資訊
        ArrayList<String> count = new ArrayList<>();
        // 儲存水果資訊
        ArrayList<Item> item = new ArrayList<>();
        Random random = new Random();

        // 從 R 類別讀取圖檔
        TypedArray array = getResources().obtainTypedArray(R.array.image_list);
        for (int index = 0; index < array.length(); index++) {
            // 水果圖片 Id
            int photo = array.getResourceId(index, 0);
            // 水果名稱
            String name = "水果" + (index + 1);
            // 亂數產生價格 (10~100)
            int price = random.nextInt(91) + 10;
            // 新增可購買數量資訊
            count.add((index + 1) + "個");
            // 新增水果資訊
            item.add(new Item(photo, name, price));
        }
        // 釋放圖檔資源
        array.recycle();

        // 建立 ArrayAdapter 物件，並傳入字串與 simple_list_item_1.xml
        spinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                count
        ));
        // 設定橫向顯示列數
        gridView.setNumColumns(3);
        // 建立 MyAdapter 物件，並傳入 adapter_vertical 作為畫面
        gridView.setAdapter(new MyAdapter(this, item, R.layout.adapter_vertical));
        // 建立 MyAdapter 物件，並傳入 adapter_horizontal 作為畫面
        listView.setAdapter(new MyAdapter(this, item, R.layout.adapter_horizontal));
    }
}
