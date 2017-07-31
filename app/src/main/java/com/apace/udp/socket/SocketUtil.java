package com.apace.udp.socket;

import com.apace.udp.UDPConfig;
import com.apace.udp.entity.UdpMsg;
import com.apace.udp.manager.UdpSocketManager;
import com.apace.udp.thread.SendThread;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by HJianFei at 2017/7/29
 * Description:
 */
public class SocketUtil {

    private DatagramSocket datagramSocket = null;
    protected static UDPConfig mUDPConfig;
    private static SocketUtil socketUtil;
    private SendThread sendThread;


    /**
     * 获取Socket实例
     *
     * @return socketUtil
     */
    public static SocketUtil getSocketUtil() {
        if (socketUtil == null) {
            socketUtil = new SocketUtil();
            socketUtil.init();
        }
        return socketUtil;
    }

    /**
     * 初始化
     */
    private void init() {

        //初始化配置信息
        mUDPConfig = new UDPConfig.Builder().create();
    }


    /**
     * 获取UDP连接
     *
     * @return
     */
    public DatagramSocket getDatagramSocket() {
        if (datagramSocket != null) {
            return datagramSocket;
        }
        synchronized (new Object()) {
            if (datagramSocket != null) {
                return datagramSocket;
            }
            int localPort = mUDPConfig.getLocalPort();
            try {
                if (localPort > 0) {
                    datagramSocket = UdpSocketManager.getUdpSocket(localPort);
                    if (datagramSocket == null) {
                        datagramSocket = new DatagramSocket(localPort);
                        UdpSocketManager.putUdpSocket(datagramSocket);
                    }
                } else {
                    datagramSocket = new DatagramSocket();
                }
                datagramSocket.setSoTimeout((int) mUDPConfig.getReceiveTimeout());
            } catch (SocketException e) {
                e.printStackTrace();
                datagramSocket = null;
            }
            return datagramSocket;
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(UdpMsg msg) {
        if (!getSendThread().isAlive()) {//开启发送线程
            getSendThread().start();
        }
        getSendThread().enqueueUdpMsg(msg);
    }

    /**
     * 获取发送消息线程
     *
     * @return
     */
    private SendThread getSendThread() {
        if (sendThread == null || !sendThread.isAlive()) {
            sendThread = new SendThread(getDatagramSocket());
        }
        return sendThread;
    }

    /**
     * 关闭UDP连接
     */
    public void closeSocket() {
        if (datagramSocket != null && datagramSocket.isConnected()) {
            datagramSocket.disconnect();
            datagramSocket = null;
        }
    }

    /**
     * UDP连接配置
     *
     * @param UDPConfig
     */
    public void config(UDPConfig UDPConfig) {
        mUDPConfig = UDPConfig;
    }
}
