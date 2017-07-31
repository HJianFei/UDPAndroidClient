package com.apace.udp.thread;

import android.util.Log;

import com.apace.udp.entity.BaseMsg;
import com.apace.udp.entity.TargetInfo;
import com.apace.udp.entity.UdpMsg;
import com.apace.udp.utils.CharsetUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/7/31.
 */
public class ReceiveThread extends Thread {

    private DatagramSocket datagramSocket = null;

    public ReceiveThread(DatagramSocket socket) {
        this.datagramSocket = socket;
    }

    @Override
    public void run() {
        if (datagramSocket == null) {
            return;
        }
        Log.d("onResponse", "ReceiveThread");
        byte[] buff = new byte[1024];
        DatagramPacket pack = new DatagramPacket(buff, buff.length);
        while (!Thread.interrupted()) {
            try {
                Log.d("onResponse", "receive...");
                datagramSocket.receive(pack);
                Log.d("onResponse", "receiving...");
                byte[] res = Arrays.copyOf(buff, pack.getLength());
                UdpMsg udpMsg = new UdpMsg(res, new TargetInfo(pack.getAddress().getHostAddress(), pack.getPort()),
                        BaseMsg.MsgType.Receive);
                udpMsg.setTime();
                String msgstr = CharsetUtil.dataToString(res, CharsetUtil.UTF_8);
                udpMsg.setSourceDataString(msgstr);
                //接收到消息
            } catch (IOException e) {
                if (!(e instanceof SocketTimeoutException)) {//不是超时报错

                }
            }
        }
    }
}
