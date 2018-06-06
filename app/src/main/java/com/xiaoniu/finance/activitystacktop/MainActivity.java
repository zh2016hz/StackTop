package com.xiaoniu.finance.activitystacktop;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  检查覆盖层权限
        checkOverlayPermission();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AccessibilityUtil.checkAccessibility(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    intent.putExtra(TrackerService.COMMAND, TrackerService.COMMAND_OPEN);
                    startService(intent);
                    finish();
                }
            }
        });
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                startActivityForResult(
                        new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        REQUEST_CODE
                );
                Toast.makeText(this, "请先授予 \"栈顶通\" 悬浮窗权限", Toast.LENGTH_LONG).show();
            }
        }
    }
}
