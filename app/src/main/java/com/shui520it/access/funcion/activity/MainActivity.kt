package com.shui520it.access.funcion.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shui520it.access.funcion.BuildConfig
import com.shui520it.access.funcion.R
import com.shui520it.access.funcion.pool.ShuiRunnableImpl

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        checkHasPermission()
        setContentView(R.layout.activity_main)
        val versionCodeTxt = findViewById<TextView>(R.id.versionCodeTxt)
        versionCodeTxt.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    fun openAccessibilitySettings(view: View) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        //跳转系统自带界面 辅助功能界面
            Toast.makeText(this, "请打开辅助功能!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun checkHasPermission() {
        //对需要的权限进行判断，是否存在没有授权的权限
        BASE_PERMISSIONS
                .filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
                .takeIf { it.isNotEmpty() }?.toTypedArray()?.apply {
                    //对没有授权的权限进行请求
                    ActivityCompat.requestPermissions(this@MainActivity, this, BASE_PERMISSION_ONE_REQUEST)
                }
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.decorView.systemUiVisibility = option
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                window.attributes.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            }
        }

    }

    companion object {
        /**
         * 需要声明的权限
         */
        internal val BASE_PERMISSIONS: Array<String> = arrayOf(Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        /**
         * 请求情绪的requestCode
         */
        internal val BASE_PERMISSION_ONE_REQUEST = 0x1011
    }
}
