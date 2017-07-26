package com.apace.udp;

import com.apace.udp.entity.BaseMsg;
import com.apace.udp.entity.TargetInfo;
import com.apace.udp.entity.UdpMsg;
import com.apace.udp.listener.UdpListener;
import com.apace.udp.manager.UdpSocketManager;
import com.apace.udp.utils.CharsetUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * UDP工具类
 */
public class UDPUtils {

    protected UdpClientConfig mUdpClientConfig;
    protected List<UdpListener> mUdpListeners;
    private DatagramSocket datagramSocket;
    private SendThread sendThread;
    private ReceiveThread receiverThread;

    private UDPUtils() {
        super();
    }

    public static UDPUtils getUdpClient() {
        UDPUtils client = new UDPUtils();
        client.init();
        return client;
    }

    private void init() {
        mUdpListeners = new ArrayList<>();
        mUdpClientConfig = new UdpClientConfig.Builder().create();
    }

    public void closeSocket() {
        if (datagramSocket != null && datagramSocket.isConnected()) {
            datagramSocket.disconnect();
            datagramSocket = null;
        }
    }

    public void startUdpServer() {
        if (!getReceiveThread().isAlive()) {
            getReceiveThread().start();
        }
    }

    public void stopUdpServer() {
        getReceiveThread().interrupt();
    }

    public boolean isUdpServerRuning() {
        return getReceiveThread().isAlive();
    }

    public void sendMsg(UdpMsg msg) {
        if (!getSendThread().isAlive()) {//开启发送线程
            getSendThread().start();
        }
        getSendThread().enqueueUdpMsg(msg);
        startUdpServer();

    }

    private SendThread getSendThread() {
        if (sendThread == null || !sendThread.isAlive()) {
            sendThread = new SendThread();
        }
        return sendThread;
    }

    private ReceiveThread getReceiveThread() {
        if (receiverThread == null || !receiverThread.isAlive()) {
            receiverThread = new ReceiveThread();
        }
        return receiverThread;
    }

    private DatagramSocket getDatagramSocket() {
        if (datagramSocket != null) {
            return datagramSocket;
        }
        synchronized (new Object()) {
            if (datagramSocket != null) {
                return datagramSocket;
            }
            int localPort = mUdpClientConfig.getLocalPort();
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
                datagramSocket.setSoTimeout((int) mUdpClientConfig.getReceiveTimeout());
            } catch (SocketException e) {
                e.printStackTrace();
                datagramSocket = null;
            }
            return datagramSocket;
        }
    }

    private class SendThread extends Thread {
        private LinkedBlockingQueue<UdpMsg> msgQueue;
        private UdpMsg sendingMsg;

        protected LinkedBlockingQueue<UdpMsg> getMsgQueue() {
            if (msgQueue == null) {
                msgQueue = new LinkedBlockingQueue<UdpMsg>();
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
            if (getDatagramSocket() == null) {
                return;
            }
            try {
                while (!Thread.interrupted() && (msg = getMsgQueue().take()) != null) {
                    setSendingMsg(msg);//设置正在发送的
                    byte[] newary = msg.getSourceDataBytes();
                    if (newary == null) {//根据编码转换消息
                        newary = CharsetUtil.stringToData(msg.getSourceDataString(), mUdpClientConfig.getCharsetName());
                    }
                    if (newary != null && newary.length > 0) {
                        try {
                            TargetInfo mTargetInfo = msg.getTarget();
                            DatagramPacket packet = new DatagramPacket(newary, newary.length, new InetSocketAddress(mTargetInfo.getIp(), mTargetInfo.getPort()));
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

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            if (getDatagramSocket() == null) {
                return;
            }
            byte[] buff = new byte[1024];
            DatagramPacket pack = new DatagramPacket(buff, buff.length);
            while (!Thread.interrupted()) {
                try {
                    getDatagramSocket().receive(pack);
                    byte[] res = Arrays.copyOf(buff, pack.getLength());
                    UdpMsg udpMsg = new UdpMsg(res, new TargetInfo(pack.getAddress().getHostAddress(), pack.getPort()),
                            BaseMsg.MsgType.Receive);
                    udpMsg.setTime();
                    String msgstr = CharsetUtil.dataToString(res, mUdpClientConfig.getCharsetName());
                    udpMsg.setSourceDataString(msgstr);
                    notifyReceiveListener(udpMsg);
                } catch (IOException e) {
                    if (!(e instanceof SocketTimeoutException)) {//不是超时报错
                    }
                }
            }
        }
    }

    private void notifyReceiveListener(final UdpMsg msg) {
        for (UdpListener l : mUdpListeners) {
            UdpListener listener = l;
            if (listener != null) {
                listener.onReceive(UDPUtils.this, msg);

            }

        }
    }

    public void config(UdpClientConfig udpClientConfig) {
        mUdpClientConfig = udpClientConfig;
    }

    public void addUdpClientListener(UdpListener listener) {
        if (mUdpListeners.contains(listener)) {
            return;
        }
        this.mUdpListeners.add(listener);
    }

    public void removeUdpClientListener(UdpListener listener) {
        this.mUdpListeners.remove(listener);
    }

    @Override
    public String toString() {
        return "UDPUtils{" +
                "datagramSocket=" + datagramSocket +
                '}';
    }
}
