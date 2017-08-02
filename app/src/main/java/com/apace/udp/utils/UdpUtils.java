package com.apace.udp.utils;

import com.apace.udp.constants.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public class UdpUtils {

    //命令编号
    private static final int TERMINAL_POST = 100;//提交命令
    private static final int TERMINAL_GET = 101;//请求命令
    private static final int TERMINAL_DELETE = 102;//删除命令
    private static final int TERMINAL_UPDATE = 104;//更新命令

    //数据类型
    private static final int DATA_CONTENT = 200;//文本类型
    private static final int DATA_IMAGE = 201;//图片类型
    private static final int DATA_VIDEO = 202;//图像类型
    private static final int DATA_AUDIO = 203;//语音类型
    private static final int DATA_FILE = 204;//文件类型


    /**
     * 聊天客户端协议
     */
    public static class Chat {

        /**
         * 纯文本
         *
         * @param terminalType 命令编号
         * @param userId       原用户编号
         * @param toUserId     目标用户编号
         * @param content      文本内容
         * @return
         */
        public static List<byte[]> contentToByte(int terminalType, int userId, int toUserId, String content) {

            List<byte[]> list = new ArrayList<>();
            //内容字节数组
            byte[] bytes = ByteUtil.getBytes(content);
            //内容字节长度
            int length = ByteUtil.getBytes(content).length;
            //需要分包的数目
            int size = length / Constants.UDP_LENGTH;
            int total = size;
            if (length % Constants.UDP_LENGTH > 0) {
                total = size + 1;
            }

            //命令编号
            byte[] terminalByte = ByteUtil.getBytes_1(terminalType);
            //后台用户标志
            byte[] userIdByte = ByteUtil.getBytes_1(userId);
            //数据类型
            byte[] contentTypeByte = ByteUtil.getBytes_1(DATA_CONTENT);
            //验证标志
            byte[] authByte = ByteUtil.getBytes_2(10001);
            //包内容长度
            byte[] contentLengthByte = ByteUtil.getBytes_2(length);
            //流水号
            byte[] serialByte = ByteUtil.getBytes_2(1236);
            //原用户编号
            byte[] userIdToByte = ByteUtil.getBytes_4(userId);
            //目标用户编号
            byte[] toUserIdByte = ByteUtil.getBytes_4(toUserId);
            //包总数
            byte[] totalByte = ByteUtil.getBytes_2(total);

            byte[] tmp_1 = ByteUtil.byteMerger(terminalByte, userIdByte);
            byte[] tmp_2 = ByteUtil.byteMerger(tmp_1, contentTypeByte);
            byte[] tmp_3 = ByteUtil.byteMerger(tmp_2, authByte);
            byte[] tmp_4 = ByteUtil.byteMerger(tmp_3, contentLengthByte);
            byte[] tmp_5 = ByteUtil.byteMerger(tmp_4, serialByte);
            byte[] tmp_6 = ByteUtil.byteMerger(tmp_5, userIdToByte);
            byte[] tmp_7 = ByteUtil.byteMerger(tmp_6, toUserIdByte);
            //协议头部
            byte[] tmpHead = ByteUtil.byteMerger(tmp_7, totalByte);

            //如果内容长度超过UDP一个数据包的最大长度，进行分包处理
            if (length - Constants.UDP_LENGTH > 0) {//内容长度大于UDP最大长度，拆包
                //拆包临时字节数组
                byte[] tmp;
                for (int i = 0; i < size; i++) {//循环拆包
                    //当前包号
                    byte[] curPacketNum = ByteUtil.getBytes_2(i + 1);
                    byte[] tmp_head = ByteUtil.byteMerger(tmpHead, curPacketNum);
                    //临时数组的长度
                    tmp = new byte[(Constants.UDP_LENGTH)];
                    System.arraycopy(bytes, i * (Constants.UDP_LENGTH), tmp, 0, (Constants.UDP_LENGTH));
                    list.add(ByteUtil.byteMerger(tmp_head, tmp));
                }
                if (length % (Constants.UDP_LENGTH) > 0) {
                    //当前包号
                    byte[] curPacketNum = ByteUtil.getBytes_2(size + 1);
                    byte[] tmp_head = ByteUtil.byteMerger(tmpHead, curPacketNum);
                    tmp = new byte[length % (Constants.UDP_LENGTH)];
                    System.arraycopy(bytes, size * (Constants.UDP_LENGTH), tmp, 0, length % (Constants.UDP_LENGTH));
                    list.add(ByteUtil.byteMerger(tmp_head, tmp));
                }
            } else {//内容长度小于UDP内容长度，不拆包
                //当前包号
                byte[] curPacketNum = ByteUtil.getBytes_2(1);
                byte[] tmp_head = ByteUtil.byteMerger(tmpHead, curPacketNum);
                list.add(ByteUtil.byteMerger(tmp_head, bytes));
            }
            return list;

        }
    }

}
