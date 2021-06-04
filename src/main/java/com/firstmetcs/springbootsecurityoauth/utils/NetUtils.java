package com.firstmetcs.springbootsecurityoauth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 * @author fancunshuo
 */
public class NetUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    /**
     * 多网卡指定名字类型返回指定InetAddress
     * 输入类型错误则输出全部网卡接口信息
     *
     * @param name 名字
     * @param type 类型:  4--Inet4Address  6--Inet6Address
     * @return
     */
    public static InetAddress getInetAddress(String name, int type) {
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            InetAddress addr;
            if (type == 4) {
                while (nifs.hasMoreElements()) {
                    NetworkInterface nif = nifs.nextElement();
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        addr = addresses.nextElement();
                        if (nif.getName().equals(name) && addr instanceof Inet4Address) {
                            return addr;
                        }
                    }
                }
            } else if (type == 6) {
                while (nifs.hasMoreElements()) {
                    NetworkInterface nif = nifs.nextElement();
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        addr = addresses.nextElement();
                        if (nif.getName().equals(name) && addr instanceof Inet6Address) {
                            return addr;
                        }
                    }
                }
            } else {
                logger.debug("类型指定错误,将输出全部网卡接口信息");
                while (nifs.hasMoreElements()) {
                    NetworkInterface nif = nifs.nextElement();
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        addr = addresses.nextElement();
                        logger.debug("网卡接口名称：" + nif.getName());
                        logger.debug("网卡接口地址：" + addr.getHostAddress());
                    }
                }
                return null;
            }

        } catch (Exception e) {
            logger.error("获取Socket失败");
        }
        return null;
    }

    public String getLocalHostName() {
        String hostName;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
        } catch (Exception ex) {
            hostName = "";
        }
        return hostName;
    }

    public String[] getAllLocalHostIP() {
        String[] ret = null;
        try {
            String hostName = getLocalHostName();
            if (hostName.length() > 0) {
                InetAddress[] addrs = InetAddress.getAllByName(hostName);
                if (addrs.length > 0) {
                    ret = new String[addrs.length];
                    for (int i = 0; i < addrs.length; i++) {
                        ret[i] = addrs[i].getHostAddress();
                    }
                }
            }

        } catch (Exception ex) {
            ret = null;
        }
        return ret;
    }

    /**
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。
     * <p>
     * 过滤虚拟机、蓝牙等地址
     */
    public static String getRealIP() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces
                        .nextElement();

                // 去除回环接口，子接口，未运行和接口
                if (netInterface.isLoopback() || netInterface.isVirtual()
                        || !netInterface.isUp()) {
                    continue;
                }

                if (!netInterface.getDisplayName().contains("Intel")
                        && !netInterface.getDisplayName().contains("Realtek")) {
                    continue;
                }

                Enumeration<InetAddress> addresses = netInterface
                        .getInetAddresses();

                logger.debug(netInterface.getDisplayName());

                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();

                    if (ip != null) {
                        // ipv4
                        if (ip instanceof Inet4Address) {
                            logger.debug("ipv4 = " + ip.getHostAddress());
                            return ip.getHostAddress();
                        }
                    }
                }
                break;
            }
        } catch (SocketException e) {
            logger.error("Error when getting host ip address"
                    + e.getMessage());
        }
        return null;
    }

}
