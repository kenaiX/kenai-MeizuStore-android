package com.kenai.function.messages;

import android.util.Log;

public class XLog {

    public final boolean TEST = true;
    public static boolean model = false;

    public final static void xLog(String s1) {
        if (model)
            Log.v("kenai's log", s1);
    }
    
    public final static void xLog(String TAG,String s1) {
        if (model)
            Log.v("kenai's log", TAG+">>>>>"+s1);
    }

    public final static void xLog_bug(String s1) {
        if (model)
            Log.e("kenai's error", s1);
    }
    
    public final static void xLog_bug(String TAG,String s1) {
        if (model)
            Log.e("kenai's log", TAG+">>>>>"+s1);
    }
    
    
}
