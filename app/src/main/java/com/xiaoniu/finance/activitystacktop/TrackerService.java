package com.xiaoniu.finance.activitystacktop;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import org.greenrobot.eventbus.EventBus;

import static android.content.ContentValues.TAG;

public class TrackerService extends AccessibilityService {
    public static final String COMMAND = "COMMAND";
    public static final String COMMAND_OPEN = "COMMAND_OPEN";
    public static final String COMMAND_CLOSE = "COMMAND_CLOSE";
    private TrackerWindowManager mTrackerWindowManager;

    public TrackerService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "event:" + event.getPackageName() + ": " + event.getClassName());
        if(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()){
//        if(AccessibilityEvent.TYPE_WINDOWS_CHANGED == event.getEventType()){  //这个坑B
            Log.d(TAG, "窗口改变发送方了一次请求" + event.getPackageName() + ": " + event.getClassName());
            EventBus.getDefault().post(new ActivityChangedEvent(event.getPackageName().toString(),event.getClassName().toString()));
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initTrackerWindowManager();

        String command = intent.getStringExtra(COMMAND);
        if(command != null) {
            if (command.equals(COMMAND_OPEN))
                mTrackerWindowManager.addView();
            else if (command.equals(COMMAND_CLOSE))
                mTrackerWindowManager.removeView();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initTrackerWindowManager(){
        if(mTrackerWindowManager == null)
            mTrackerWindowManager = new TrackerWindowManager(this);

    }
    public static class ActivityChangedEvent{
        private final String mPackageName;
        private final String mClassName;

        public ActivityChangedEvent(String packageName, String className) {
            mPackageName = packageName;
            mClassName = className;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public String getClassName() {
            return mClassName;
        }
    }
}
