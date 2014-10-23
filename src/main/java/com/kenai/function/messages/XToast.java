package com.kenai.function.messages;

import android.content.Context;
import android.widget.Toast;

public class XToast {

    public final static void xToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public final static void xToast_byID(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }
}
