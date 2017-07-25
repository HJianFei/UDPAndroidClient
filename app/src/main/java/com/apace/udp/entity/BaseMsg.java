package com.apace.udp.entity;

import java.util.Arrays;

/**
 * 接收，发送数据的实体基类
 *
 * @author Administrator
 */
public class BaseMsg {
    public enum MsgType {
        Send, Receive
    }

    private byte[] sourceDataBytes;//数据源
    private String sourceDataString;//数据源
    private TargetInfo target;
    private long time;//发送、接受消息的时间戳
    private MsgType mMsgType = MsgType.Send;

    public BaseMsg(byte[] data, TargetInfo target, MsgType type) {
        this.sourceDataBytes = data;
        this.target = target;
        this.mMsgType = type;
    }

    public BaseMsg(String data, TargetInfo target, MsgType type) {
        this.target = target;
        this.sourceDataString = data;
        this.mMsgType = type;
    }

    public void setTime() {
        time = System.currentTimeMillis();
    }


    public long getTime() {
        return time;
    }

    public MsgType getMsgType() {
        return mMsgType;
    }

    public void setMsgType(MsgType msgType) {
        mMsgType = msgType;
    }


    @Override
    public String toString() {
        return "BaseMsg{" +
                "sourceDataBytes=" + Arrays.toString(sourceDataBytes) +
                ", sourceDataString='" + sourceDataString + '\'' +
                ", target=" + target +
                ", time=" + time +
                ", msgtyoe=" + mMsgType +
                '}';
    }


    public byte[] getSourceDataBytes() {
        return sourceDataBytes;
    }

    public void setSourceDataBytes(byte[] sourceDataBytes) {
        this.sourceDataBytes = sourceDataBytes;
    }
    public String getSourceDataString() {
        return sourceDataString;
    }

    public void setSourceDataString(String sourceDataString) {
        this.sourceDataString = sourceDataString;
    }

    public TargetInfo getTarget() {
        return target;
    }

    public void setTarget(TargetInfo target) {
        this.target = target;
    }
}
