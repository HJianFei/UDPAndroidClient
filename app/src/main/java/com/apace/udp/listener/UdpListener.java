package com.apace.udp.listener;

import com.apace.udp.UDPUtils;
import com.apace.udp.entity.UdpMsg;

/**
 * UDP连接监听器
 */
public interface UdpListener {

    void onStarted(UDPUtils udpUtils);

    void onStoped(UDPUtils udpUtils);

    void onSended(UDPUtils udpUtils, UdpMsg udpMsg);

    void onReceive(UDPUtils client, UdpMsg udpMsg);

    void onError(UDPUtils client, String msg, Exception e);

}
