package com.apace.udp.thread;

import com.apace.udp.entity.TargetInfo;
import com.apace.udp.entity.UdpMsg;
import com.apace.udp.utils.CharsetUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/7/31.
 */

public class SendThread extends Thread {


    private DatagramSocket datagramSocket = null;

    public SendThread(DatagramSocket socket) {
        this.datagramSocket = socket;
    }

    private LinkedBlockingQueue<UdpMsg> msgQueue;
    private UdpMsg sendingMsg;

    protected LinkedBlockingQueue<UdpMsg> getMsgQueue() {
        if (msgQueue == null) {
            msgQueue = new LinkedBlockingQueue<>();
        }
        return msgQueue;
    }

    protected SendThread setSendingMsg(UdpMsg sendingMsg) {
        this.sendingMsg = sendingMsg;
        return this;
    }

    public UdpMsg getSendingMsg() {
        return this.sendingMsg;
    }

    public boolean enqueueUdpMsg(final UdpMsg tcpMsg) {
        if (tcpMsg == null || getSendingMsg() == tcpMsg || getMsgQueue().contains(tcpMsg)) {
            return false;
        }
        try {
            getMsgQueue().put(tcpMsg);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        UdpMsg msg;
        if (datagramSocket == null) {
            return;
        }
        try {
            while (!Thread.interrupted() && (msg = getMsgQueue().take()) != null) {
                setSendingMsg(msg);//设置正在发送的
                byte[] dataBytes = msg.getSourceDataBytes();
                if (dataBytes == null) {//根据编码转换消息
                    dataBytes = CharsetUtil.stringToData(msg.getSourceDataString(), CharsetUtil.UTF_8);
                }
                if (dataBytes != null && dataBytes.length > 0) {
                    try {
                        TargetInfo mTargetInfo = msg.getTarget();
                        DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length, new InetSocketAddress(mTargetInfo.getIp(), mTargetInfo.getPort()));
                        try {
                            msg.setTime();
                            datagramSocket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
