package org.cloud.mclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author d05660ddw
 * @version 1.0 2017/8/27
 */

public class NetUtil {

    /**
     * 判断网络连通
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (null == context) {
            return false;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connectivity) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                return info.isAvailable();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 判断wifi联通性
     * @param context
     * @return
     */
    public static boolean isWiFi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

}
