package com.shui520it.access.funcion.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shui520it.access.funcion.BuildConfig;
import com.shui520it.access.funcion.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String[] BASE_PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int BASE_PERMISSION_ONE_REQUEST = 0x1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为全屏模式
        fullScreen(this);
        setContentView(R.layout.activity_main);
        checkHasPermission();
        TextView versionCodeText = findViewById(R.id.versionCodeTxt);
        versionCodeText.setText(getString(R.string.version,BuildConfig.VERSION_NAME));
    }

    // 跳转到安卓的辅助功能界面
    public void openAccessibilitySettings(View view) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            //跳转系统自带界面 辅助功能界面
            Toast.makeText(this, "请打开辅助功能!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    public void checkHasPermission() {
        ArrayList<String> deniedList = new ArrayList<>();
        for (int i = 0; i < BASE_PERMISSIONS.length; i++) {
            if (ContextCompat.checkSelfPermission(this, BASE_PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(BASE_PERMISSIONS[i]);
            }
        }

        if (!deniedList.isEmpty()) {
            String[] tempDenied = deniedList.toArray(new String[deniedList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, tempDenied, BASE_PERMISSION_ONE_REQUEST);
        }
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }
    }
}
