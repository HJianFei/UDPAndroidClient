package com.apace.udp.utils;


import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Java基本数据类型和byte数组相互转化
 *
 * @author liuyazhuang
 */
public class ByteUtil {
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes_1(int data) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (data & 0xff);
        return bytes;
    }

    public static byte[] getBytes_2(int data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes_3(int data) {
        byte[] bytes = new byte[3];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        return bytes;
    }

    public static byte[] getBytes_4(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "UTF-8");
    }

    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    public static int getInt_1(byte[] bytes) {
        return (0xff & bytes[0]);
    }

    public static int getInt_2(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8));
    }

    public static int getInt_3(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16));
    }

    public static int getInt_4(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    public static long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16))
                | (0xff000000L & ((long) bytes[3] << 24)) | (0xff00000000L & ((long) bytes[4] << 32))
                | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48))
                | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "UTF-8");
    }

    /**
     * 两个字节数组合并
     *
     * @param byte_1
     * @param byte_2
     * @return
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 字符数组截取
     *
     * @param data
     * @param startidx
     * @param length
     * @return
     */
    public static byte[] cutBytes(byte[] data, int startidx, int length) {
        byte[] ary = new byte[length];
        System.arraycopy(data, startidx, ary, 0, length);
        return ary;
    }

    public static void main(String[] args) {

        String string = "我是加大加肥觉得撒基大厦附近的萨芬加快第三辅导教师拉法基诶微积分发顺丰科技色温";
        List<byte[]> bytes = UdpUtils.Chat.contentToByte(100, 101, 102, string);
        System.out.println("分包数量：" + bytes.size());
        if (bytes.size() > 1) {
            byte[] tmp = new byte[0];
            for (int i = 0; i < bytes.size() - 1; i++) {
                if (i == 0) {
                    System.out.println(
                            "命令编号：" + getInt_1(cutBytes(bytes.get(i), 0, 1))
                                    + "  后台用户标志：" + getInt_1(cutBytes(bytes.get(i), 1, 1))
                                    + "  数据类型：" + getInt_1(cutBytes(bytes.get(i), 2, 1))
                                    + "  验证标志：" + getInt_2(cutBytes(bytes.get(i), 3, 2))
                                    + "  包内容长度：" + getInt_2(cutBytes(bytes.get(i), 5, 2))
                                    + "  流水号：" + getInt_2(cutBytes(bytes.get(i), 7, 2))
                                    + "  原用户编号：" + getInt_4(cutBytes(bytes.get(i), 9, 4))
                                    + "  目标用户编号：" + getInt_4(cutBytes(bytes.get(i), 13, 4))
                                    + "  包总数：" + getInt_2(cutBytes(bytes.get(i), 17, 2))
                                    + "  当前包数：" + getInt_2(cutBytes(bytes.get(i), 19, 2))
                                    + "  传输内容：" + Arrays.toString(cutBytes(bytes.get(i), 21, bytes.get(i).length - 21)));
                    byte[] bytes1 = cutBytes(bytes.get(i), 21, bytes.get(i).length - 21);
                    byte[] bytes2 = cutBytes(bytes.get(i + 1), 21, bytes.get(i + 1).length - 21);
                    byte[] bytes3 = byteMerger(bytes1, bytes2);
                    tmp = bytes3;
                } else {
                    System.out.println(
                            "命令编号：" + getInt_1(cutBytes(bytes.get(i), 0, 1))
                                    + "  后台用户标志：" + getInt_1(cutBytes(bytes.get(i), 1, 1))
                                    + "  数据类型：" + getInt_1(cutBytes(bytes.get(i), 2, 1))
                                    + "  验证标志：" + getInt_2(cutBytes(bytes.get(i), 3, 2))
                                    + "  包内容长度：" + getInt_2(cutBytes(bytes.get(i), 5, 2))
                                    + "  流水号：" + getInt_2(cutBytes(bytes.get(i), 7, 2))
                                    + "  原用户编号：" + getInt_4(cutBytes(bytes.get(i), 9, 4))
                                    + "  目标用户编号：" + getInt_4(cutBytes(bytes.get(i), 13, 4))
                                    + "  包总数：" + getInt_2(cutBytes(bytes.get(i), 17, 2))
                                    + "  当前包数：" + getInt_2(cutBytes(bytes.get(i), 19, 2))
                                    + "  传输内容：" + Arrays.toString(cutBytes(bytes.get(i), 21, bytes.get(i).length - 21)));
                    tmp = byteMerger(tmp, cutBytes(bytes.get(i + 1), 21, bytes.get(i + 1).length - 21));
                }
            }
            System.out.println(
                    "命令编号：" + getInt_1(cutBytes(bytes.get(bytes.size() - 1), 0, 1))
                            + "  后台用户标志：" + getInt_1(cutBytes(bytes.get(bytes.size() - 1), 1, 1))
                            + "  数据类型：" + getInt_1(cutBytes(bytes.get(bytes.size() - 1), 2, 1))
                            + "  验证标志：" + getInt_2(cutBytes(bytes.get(bytes.size() - 1), 3, 2))
                            + "  包内容长度：" + getInt_2(cutBytes(bytes.get(bytes.size() - 1), 5, 2))
                            + "  流水号：" + getInt_2(cutBytes(bytes.get(bytes.size() - 1), 7, 2))
                            + "  原用户编号：" + getInt_4(cutBytes(bytes.get(bytes.size() - 1), 9, 4))
                            + "  目标用户编号：" + getInt_4(cutBytes(bytes.get(bytes.size() - 1), 13, 4))
                            + "  包总数：" + getInt_2(cutBytes(bytes.get(bytes.size() - 1), 17, 2))
                            + "  当前包数：" + getInt_2(cutBytes(bytes.get(bytes.size() - 1), 19, 2))
                            + "  传输内容：" + Arrays.toString(cutBytes(bytes.get(bytes.size() - 1), 21, bytes.get(bytes.size() - 1).length - 21)));
            System.out.println("分包:");
            System.out.println(Arrays.toString(tmp));
            System.out.println(ByteUtil.getString(tmp));
        } else {
            System.out.println(
                    "命令编号：" + getInt_1(cutBytes(bytes.get(0), 0, 1))
                            + "  后台用户标志：" + getInt_1(cutBytes(bytes.get(0), 1, 1))
                            + "  数据类型：" + getInt_1(cutBytes(bytes.get(0), 2, 1))
                            + "  验证标志：" + getInt_2(cutBytes(bytes.get(0), 3, 2))
                            + "  包内容长度：" + getInt_2(cutBytes(bytes.get(0), 5, 2))
                            + "  流水号：" + getInt_2(cutBytes(bytes.get(0), 7, 2))
                            + "  原用户编号：" + getInt_4(cutBytes(bytes.get(0), 9, 4))
                            + "  目标用户编号：" + getInt_4(cutBytes(bytes.get(0), 13, 4))
                            + "  包总数：" + getInt_2(cutBytes(bytes.get(0), 17, 2))
                            + "  当前包数：" + getInt_2(cutBytes(bytes.get(0), 19, 2))
                            + "  传输内容：" + Arrays.toString(cutBytes(bytes.get(0), 21, bytes.get(0).length - 21)));
            System.out.println("未分包:");

            System.out.println(Arrays.toString(cutBytes(bytes.get(0), 21, bytes.get(0).length - 21)));
            System.out.println(getString(cutBytes(bytes.get(0), 21, bytes.get(0).length - 21)));

        }


    }
}