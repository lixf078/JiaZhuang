package com.aiyiqi.lib.utils;

import android.content.Context;

import com.aiyiqi.decoration.lib.constants.Constants;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by hubing on 16/3/17.
 */
public class DeviceUtil {
    public static final String getAndroidId(final Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String fetchIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()&& isIPv4Address(inetAddress
                                .getHostAddress())) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if(!ipaddress.contains("::")){//ipV6的地址
                                return ipaddress;
                            }
                        }
                    }
                }else {
                    continue;
                }
            }
        } catch (SocketException ex) {
            Logger.e(Constants.TAG, "fetch ip error.",ex);
        }
        return "192.168.1.100";
    }

    /**
     * Ipv4地址检查
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    /**
     * 检查是否是有效的IPV4地址
     * @param input the address string to check for validity
     * @return true if the input parameter is a valid IPv4 address
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /*===========以下是IPv6的检查，暂时用不到==========*/

    /*//未压缩过的IPv6地址检查
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");

    //检查参数是否有效的标准(未压缩的)IPv6地址
    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }


    //压缩过的IPv6地址检查
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile(
            "^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)" + // 0-6 hex fields
            "::" +
            "(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$"); // 0-6 hex fields
    //检查参数是否有效压缩IPv6地址
    public static boolean isIPv6HexCompressedAddress(final String input) {
        int colonCount = 0;
        for(int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ':') {
                colonCount++;
            }
        }
        return  colonCount <= 7 && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    //检查是否是压缩或者未压缩过的IPV6地址
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }*/
}
