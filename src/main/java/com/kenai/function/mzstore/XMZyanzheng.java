package com.kenai.function.mzstore;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.format.Time;

import com.kenai.function.messages.XLog;
import com.kenai.function.messages.XToast;
import com.kenai.function.settings.XSetting;
import com.meizu.mstore.license.ILicensingService;
import com.meizu.mstore.license.LicenseCheckHelper;
import com.meizu.mstore.license.LicenseResult;

import java.util.Calendar;

public class XMZyanzheng {
    /**
     * 指定是否为测试版
     */
    public static boolean False;
    private final static String ISLAWFUL_STRING = "isLawful_String";
    private final static String ISLAWFUL = "isLawful";
    private final static String ISLAWFUL_ZHENGSHIBAN = "isLawful_String_zhengshiban";

    private final static String KenaiCheckDays = "kenai_check_days";
    private final static String KenaiCheckDaysFuzhu = "kenai_check_days_fuzhu";
    private final static String KenaiCheckAdd = "kenai_check_add_fuzhu";

    private final static String APKPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbUhRfkd8BHKTDnPWIONrdQTnYlM8kY+Oq+OrU/gvHaa21m24BcO+7wEuIL3oQeR2Y3KH0PekjJbhmOkoDKHzt+SF+Nsuy9SkEuEluPbwbONDBccLkPUfCwcF5mTuzfPC7xbjxpH0KJGkR1NQPjxMFLUUYAgiN+1VB9mfjdOPhawIDAQAB";

    // 定义接口变量
    public static ILicensingService mLicensingService = null;
    // 定义绑定服务时使用的Connection
    private static ServiceConnection mLicenseConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            mLicensingService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mLicensingService = ILicensingService.Stub.asInterface(service);
        }
    };

    /**
     * 同时必须用UBIND退出
     */
    public static void xBind(Activity activity) {
        // if (!get_isLawful_zhengshiban()) {
        if (mLicensingService == null) {
            Intent intent = new Intent();
            String s = ILicensingService.class.getName();
            XLog.xLog(s);
            intent.setAction(s);
            try {
                activity.bindService(intent, mLicenseConnection,
                        Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
            }
            XLog.xLog("bind mzstore");
        }
        // }
    }

    /**
     * 获取是否已经绑定
     */
    public static boolean xisHasBind() {
        if (mLicensingService != null)
            return true;
        else
            return false;
    }

    /**
     * 已经设置验证保护，针对非魅族手机不会执行
     */
    public static void xUnBind(Activity activity) {

        try {
            if (mLicensingService != null) {
                activity.unbindService(mLicenseConnection);
                XLog.xLog("unbind mzstore");
                mLicensingService = null;
            }
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    /**
     * 已经设置验证保护，针对非魅族手机不会执行
     */
    public static final void xcheck(Activity acticity) {
        Context context = acticity.getBaseContext();
        if (!get_isLawful_zhengshiban(context)) {
            if (XMZyanzheng.False) {
                XSetting.xset_string_int(context, ISLAWFUL_STRING,
                        "special version by kenai");
                XSetting.xset_boolean(context, ISLAWFUL, true);
                XToast.xToast(context, "正在使用特殊版本！");
                return;
            }

            if (xisHasBind()) {
                XLog.xLog("check mzstore");
                final String packageName = acticity.getApplication()
                        .getPackageName();
                // 调用服务检查License，并返回检查结果。。这里的mLicensingService是上一节点绑定的验证服务
                LicenseResult result;
                try {
                    result = mLicensingService.checkLicense(packageName);
                } catch (RemoteException e) {
                    XToast.xToast(context, "验证服务出现未知错误！");
                    e.printStackTrace();
                    return;
                }
                // 判断结果合法性
                if (result.getResponseCode() == LicenseResult.RESPONSE_CODE_SUCCESS) {
                    boolean bSuccess = LicenseCheckHelper.checkResult(
                            APKPublicKey, result);

                    if (bSuccess
                            && result.getPurchaseType() == LicenseResult.PURCHASE_TYPE_NORMAL) {
                        // XToast.xToast(context, "验证成功！");
                        XSetting.xset_boolean(context, ISLAWFUL, true);
                        XSetting.xset_boolean(context, ISLAWFUL_ZHENGSHIBAN,
                                true);
                        XSetting.xset_string_int(context, ISLAWFUL_STRING,
                                "正式版本");

                    } else {
                        if (bSuccess
                                && result.getPurchaseType() == LicenseResult.PURCHASE_TYPE_TRIAL) {
                            // 验证成功，是试用版本;可根据需要做功能限制或者时间限制.
                            /*
							 * 可根据自己的需要进行试用期的时间限定
							 */
                            // 该方法返回license文件的生成日期（注:该日期的值只精确到天，即时分秒为随机值）
                            Calendar beginCal = result.getStartDate();

                            XLog.xLog("试用开始的日期为: "
                                    + beginCal.get(Calendar.YEAR) + "年"
                                    + (beginCal.get(Calendar.MONTH) + 1) + "月"
                                    + beginCal.get(Calendar.DAY_OF_MONTH) + "日");

                            /**
                             * 以下是可供参考的过期判断
                             */
                            // 你自己定义的过期天数
                            final int expireDays = 3;

                            // 获取当前日期
                            Calendar nowCal = Calendar.getInstance();

                            // 求剩余的天数
                            long dif = nowCal.getTimeInMillis()
                                    - beginCal.getTimeInMillis();

                            int passDay = (int) (dif / (24 * 60 * 60 * 1000));
                            int left = expireDays - passDay;

                            // Log.e("剩余天数", "" + left);

                            if (left > 0 && left < 4) {
                                // 未过期
                                // XToast.xToast(context, "当前为试用版本！");
                                XSetting.xset_string_int(context,
                                        ISLAWFUL_STRING, "试用剩余天数：" + left + "天");
                                XSetting.xset_boolean(context, ISLAWFUL, true);
                            } else if (left < 1) {
                                // 过期
                                XLog.xLog("过期");
                                XSetting.xset_string_int(context,
                                        ISLAWFUL_STRING, "试用过期,请购买正式版");
                                XSetting.xset_boolean(context, ISLAWFUL, False);

                            } else if (left > 3) {
                                XSetting.xset_string_int(context,
                                        ISLAWFUL_STRING, "系统时间出现异常");
                                XSetting.xset_boolean(context, ISLAWFUL, False);
                            }

                        } else {
                            // XToast.xToast(context, "验证出现错误！");
                            XSetting.xset_string_int(context, ISLAWFUL_STRING,
                                    "验证出现错误");
                            XSetting.xset_boolean(context, ISLAWFUL, False);
                        }
                    }
                } else {
                    XToast.xToast(context, "验证失败！");
                    XSetting.xset_boolean(context, ISLAWFUL, False);
                    if (result.getResponseCode() == LicenseResult.RESPONSE_CODE_NO_LICENSE_FILE) {

                        XLog.xLog("无对应的证书");
                        XSetting.xset_string_int(context, ISLAWFUL_STRING,
                                "无对应的证书");
                    } else {
                        XLog.xLog("证书无效");
                        XSetting.xset_string_int(context, ISLAWFUL_STRING,
                                "证书无效");
                    }
                }
            } else {
                XToast.xToast(context, "无法绑定验证平台");
            }
            if (!get_isLawful_zhengshiban(context)) {
                if (kenai_check(context)) {
                    XSetting.xset_boolean(context, ISLAWFUL, true);
                }
            }
        }

    }

    public static boolean get_isLawful(Context context) {
        return XSetting.xget_boolean(context, ISLAWFUL);
    }

    public static boolean get_isLawful_zhengshiban(Context context) {
        return XSetting.xget_boolean(context, ISLAWFUL_ZHENGSHIBAN);
    }

    public static String get_isLawful_string(Context context) {
        return XSetting.xget_string(context, ISLAWFUL_STRING);
    }

    private static boolean kenai_check(Context context) {
        int left_day = 0;
        left_day = XSetting.xget_int(context, KenaiCheckDays);
        if (left_day != 0) {
            Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            t.setToNow();
            String time_partly;
            if (t.monthDay > 9)
                time_partly = String.valueOf(t.monthDay);
            else
                time_partly = "0" + String.valueOf(t.monthDay);

            if (XSetting.xget_int(context, KenaiCheckDaysFuzhu) != Integer
                    .parseInt(time_partly)) {
                XSetting.xset_string_int(context, KenaiCheckDaysFuzhu,
                        time_partly);
                left_day--;
                XSetting.xset_string_int(context, KenaiCheckDays, "" + left_day);
            }
            if (left_day < 0) {
                left_day = 0;
            } else {
                XSetting.xset_string_int(context, ISLAWFUL_STRING, "额外试用天数："
                        + left_day + "天");
            }
        }

        if (left_day > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void kenai_check_add(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);

            int versionCode = info.versionCode;

            if (XSetting.xget_int(context, KenaiCheckAdd) != versionCode) {
                XSetting.xset_string_int(context, KenaiCheckAdd, ""
                        + versionCode);
                XSetting.xset_string_int(context, KenaiCheckDays, "3");
            }

        } catch (NameNotFoundException e) {

        }

    }

}
