package com.xiaoniu.finance.activitystacktop;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

/**
 * 文件描述：   障碍人士工具类
 * Created by  xn069392
 * 创建时间    2018/6/6
 */

public class AccessibilityUtil {

    public static boolean checkAccessibility(Context context) {
        // 判断辅助功能是否开启
        if (!AccessibilityUtil.isAccessibilitySettingsOn(context)) {
            // 引导至辅助功能设置页面
            context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            Toast.makeText(context, "请先开启 \"Activity 栈\" 的辅助功能", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }
}
