package com.xiaoniu.finance.activitystacktop;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * 文件描述：  显示窗口管理类
 * Created by  xn069392
 * 创建时间    2018/6/6
 */

public class TrackerWindowManager {
    private static final WindowManager.LayoutParams LAYOUT_PARAMS ;
    private final Context mContext;
    private final WindowManager mWindowManager;
    private FloatingView mFloatingView;

    public TrackerWindowManager(Context context){
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

   static {
       WindowManager.LayoutParams params = new WindowManager.LayoutParams();
       params.x = 0;
       params.y = 0;
       params.width = WindowManager.LayoutParams.WRAP_CONTENT;
       params.height = WindowManager.LayoutParams.WRAP_CONTENT;
       params.gravity = Gravity.LEFT | Gravity.TOP;
       params.type = WindowManager.LayoutParams.TYPE_PHONE;
       params.format = PixelFormat.RGBA_8888;
       params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
               | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

       LAYOUT_PARAMS = params;
   }

    public void addView() {
        if(mFloatingView == null){
            mFloatingView = new FloatingView(mContext);
            mFloatingView.setLayoutParams(LAYOUT_PARAMS);

            mWindowManager.addView(mFloatingView, LAYOUT_PARAMS);
        }
    }

    public void removeView(){
        if(mFloatingView != null){
            mWindowManager.removeView(mFloatingView);
            mFloatingView = null;
        }
    }
}
