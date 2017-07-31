package com.apace.udp.listener;

import com.apace.udp.entity.UdpMsg;
import com.apace.udp.socket.SocketUtil;

/**
 * UDP连接监听器
 */
public interface UdpListener {

    void onStarted(SocketUtil udpUtils);

    void onStoped(SocketUtil udpUtils);

    void onSended(SocketUtil udpUtils, UdpMsg udpMsg);

    void onReceive(SocketUtil client, UdpMsg udpMsg);

    void onError(SocketUtil client, String msg, Exception e);

}
