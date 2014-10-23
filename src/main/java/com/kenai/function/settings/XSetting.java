package com.kenai.function.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class XSetting {
	public static SharedPreferences getSharedPreferences(Context context){
		if (setting == null)
			load(context);
		return setting;
	}
	
	private static SharedPreferences setting;

	private static void load(Context context) {

		setting = context.getApplicationContext().getSharedPreferences(context.getPackageName()
				+ "_preferences", 0);
	}

	public final static boolean xget_boolean(Context context, String string) {
		if (setting == null)
			load(context);
		return setting.getBoolean(string, false);
	}
	public final static boolean xget_boolean(Context context, int i) {
		if (setting == null)
			load(context);
		return setting.getBoolean(context.getString(i), false);
	}

	public final static int xget_int(Context context, String string) {
		if (setting == null)
			load(context);

		int i;
		try {
			i = Integer.parseInt(setting.getString(string, "0"));
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public final static int xget_int_super(Context context, String string,
			String s) {
		if (setting == null)
			load(context);
		int i;
		try {
			i = Integer.parseInt(setting.getString(string, s));
		} catch (Exception e) {
			i = Integer.parseInt(s);
		}
		return i;
	}
	public final static int xget_int(Context context, int m) {
		if (setting == null)
			load(context);

		int i;
		try {
			i = Integer.parseInt(setting.getString(context.getString(m), "0"));
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	
	public final static String xget_string(Context context, String string) {
		if (setting == null)
			load(context);
		return setting.getString(string, "");
	}
	public final static String xget_string(Context context, int i) {
		if (setting == null)
			load(context);
		return setting.getString(context.getString(i), "");
	}
	
	
	public final static float xget_float(Context context, String string) {
		if (setting == null)
			load(context);
		return Float.parseFloat(setting.getString(string, "0"));
	}
	public final static float xget_float_super(Context context, String string,String s) {
		if (setting == null)
			load(context);
		float i;
		try {
			i = Integer.parseInt(setting.getString(string, s));
		} catch (Exception e) {
			i = Integer.parseInt(s);
		}
		return i;
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public final static void xset_string_int(Context context, String key,
			String edit) {
		if (setting == null)
			load(context);
		setting.edit().putString(key, edit).commit();
	}
	public final static void xset_string_int(Context context, int i,
			String edit) {
		if (setting == null)
			load(context);
		setting.edit().putString(context.getString(i), edit).commit();

	}
	public final static void xset_boolean(Context context, String key,
			boolean edit) {
		if (setting == null)
			load(context);
		setting.edit().putBoolean(key, edit).commit();
	}
	
	public final static void xset_boolean(Context context, int i,
			boolean edit) {
		if (setting == null)
			load(context);
		setting.edit().putBoolean(context.getString(i), edit).commit();
	}
	
	
	
	
	
	
	
}
