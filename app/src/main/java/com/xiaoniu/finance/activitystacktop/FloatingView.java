package com.xiaoniu.finance.activitystacktop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.ContentValues.TAG;

/**
 * 文件描述：
 * Created by  xn069392
 * 创建时间    2018/6/6
 */

public class FloatingView extends LinearLayout {
    private final Context mContext;
    private final WindowManager mWindowManager;
    private TextView mTvPackageName;
    private TextView mTvClassName;
    private ImageView mIvClose;

    public FloatingView(Context context) {
        this(context,null);
    }

    public FloatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.floating_layout,this);
        initView();
    }

    private void initView() {
        mTvPackageName = (TextView) findViewById(R.id.tv_package_name);
        mTvClassName = (TextView) findViewById(R.id.tv_class_name);
        mIvClose = (ImageView) findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "关闭悬浮框", Toast.LENGTH_SHORT).show();
                mContext.startService(
                        new Intent(mContext, TrackerService.class)
                                .putExtra(TrackerService.COMMAND, TrackerService.COMMAND_CLOSE)
                );
            }
        });
    }

    Point preP, curP;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                preP = new Point((int)event.getRawX(), (int)event.getRawY());
                break;

            case MotionEvent.ACTION_MOVE:
                curP = new Point((int)event.getRawX(), (int)event.getRawY());
                int dx = curP.x - preP.x,
                        dy = curP.y - preP.y;

                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.getLayoutParams();
                layoutParams.x += dx;
                layoutParams.y += dy;
                mWindowManager.updateViewLayout(this, layoutParams);

                preP = curP;
                break;
        }


        return false;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TrackerService.ActivityChangedEvent event){
        Log.d(TAG, "event:" + event.getPackageName() + ": " + event.getClassName());
        String packageName = event.getPackageName(),
                className = event.getClassName();

        mTvPackageName.setText(packageName);
        mTvClassName.setText(
                className.startsWith(packageName)?
                        className.substring(packageName.length()):
                        className
        );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
        Log.d(TAG, "event:      服务关闭" );
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "event:      服务关闭" );
        EventBus.getDefault().unregister(this);
    }
}
