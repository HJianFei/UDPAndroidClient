package com.apace.udp.utils;


public class Utils {


    public static void main(String[] args) {


        byte[] mydate = packBytes(System.currentTimeMillis(), 1, "send", "revc", "文本", ByteUtil.getBytes("我的信息内容这样的"), 0, 18);
 
        /*-------------------------------------------------------------------*/

        System.out.println("=========================================");

        byte[][] bytes = parseBytes(mydate);

        long myno = ByteUtil.getLong(bytes[0]);
        System.out.println("no " + myno);

        int type_ = ByteUtil.getInt(bytes[1]);
        System.out.println("type " + type_);

        int send_len = ByteUtil.getInt(bytes[2]);
        System.out.println("sendlen_ary " + send_len);

        String send_str = ByteUtil.getString(bytes[3]);
        System.out.println("send_ary " + send_str);

        int revc_len = ByteUtil.getInt(bytes[4]);
        System.out.println("revc_len " + revc_len);

        String revc_str = ByteUtil.getString(bytes[5]);
        System.out.println("revc_str " + revc_str);

        int pack_total = ByteUtil.getInt(bytes[6]);
        System.out.println("pack_total " + pack_total);


        int msgnamelen_ = ByteUtil.getInt(bytes[7]);
        System.out.println("msgnamelen_  " + msgnamelen_);

        String msgname_ = ByteUtil.getString(bytes[8]);
        System.out.println("msgname_ " + msgname_);

        int msgtextlen_ = ByteUtil.getInt(bytes[9]);
        System.out.println("msgtextlen_ " + msgtextlen_);

        int start_ = ByteUtil.getInt(bytes[10]);
        System.out.println("start_ " + start_);

        int end_ = ByteUtil.getInt(bytes[11]);
        System.out.println("end_  " + end_);

        String magtext_ = ByteUtil.getString(bytes[12]);
        System.out.println("magtext_ " + magtext_);


    }

    public static byte[] packBytes(long ProtocolNum, int type_, String sender, String revceiver, String msg_Name, byte[] msg, int startIdx, int totalSize) {

        byte[] msgname = ByteUtil.getBytes(msg_Name);
        int msg_Namelen = msgname.length;
        int textlen = msg.length;
        byte[] send = ByteUtil.getBytes(sender);
        int sendlen = send.length;
        byte[] revc = ByteUtil.getBytes(revceiver);
        int revclen = revc.length;
        int end = startIdx + msg.length;//信息结束索引

        byte[] noary = ByteUtil.getBytes(ProtocolNum);
        byte[] typeary = ByteUtil.getBytes(type_);
        byte[] msgnameary = msgname;
        byte[] msgnamelenary = ByteUtil.getBytes(msg_Namelen);
        byte[] curtextlenary = ByteUtil.getBytes(textlen);
        byte[] startary = ByteUtil.getBytes(startIdx);
        byte[] endary = ByteUtil.getBytes(end);
        byte[] sendlenary = ByteUtil.getBytes(sendlen);
        byte[] revclenary = ByteUtil.getBytes(revclen);
        byte[] totalary = ByteUtil.getBytes(totalSize);

        int totallen = noary.length + typeary.length + msgnameary.length + msgnamelenary.length
                + msg.length + curtextlenary.length + startary.length + endary.length + send.length
                + revc.length + sendlenary.length + revclenary.length + totalary.length;

        byte[] data = new byte[totallen];
        int curlen = 0;
        System.arraycopy(noary, 0, data, curlen, noary.length);
        curlen = curlen + noary.length;
        System.arraycopy(typeary, 0, data, curlen, typeary.length);
        curlen = curlen + typeary.length;
        System.arraycopy(sendlenary, 0, data, curlen, sendlenary.length);
        curlen = curlen + sendlenary.length;
        System.arraycopy(send, 0, data, curlen, send.length);
        curlen = curlen + send.length;
        System.arraycopy(revclenary, 0, data, curlen, revclenary.length);
        curlen = curlen + revclenary.length;
        System.arraycopy(revc, 0, data, curlen, revc.length);
        curlen = curlen + revc.length;
        System.arraycopy(totalary, 0, data, curlen, totalary.length);
        curlen = curlen + totalary.length;
        System.arraycopy(msgnamelenary, 0, data, curlen, msgnamelenary.length);
        curlen = curlen + msgnamelenary.length;
        System.arraycopy(msgnameary, 0, data, curlen, msgnameary.length);
        curlen = curlen + msgnameary.length;
        System.arraycopy(curtextlenary, 0, data, curlen, curtextlenary.length);
        curlen = curlen + curtextlenary.length;
        System.arraycopy(startary, 0, data, curlen, startary.length);
        curlen = curlen + startary.length;
        System.arraycopy(endary, 0, data, curlen, endary.length);
        curlen = curlen + endary.length;
        System.arraycopy(msg, 0, data, curlen, msg.length);
        return data;
    }


    //解析传输协议
    public static byte[][] parseBytes(byte[] data) {

        byte[][] bytes = new byte[13][];
        int cur_len = 0;
        byte[] no_ary = cutBytes(data, cur_len, 8);
        cur_len = cur_len + no_ary.length;
        bytes[0] = no_ary;

        byte[] type_ary = cutBytes(data, cur_len, 4);
        cur_len = cur_len + type_ary.length;
        bytes[1] = type_ary;

        byte[] sendlen_ary = cutBytes(data, cur_len, 4);
        int send_len = ByteUtil.getInt(sendlen_ary);
        cur_len = cur_len + sendlen_ary.length;
        bytes[2] = sendlen_ary;

        byte[] send_ary = cutBytes(data, cur_len, send_len);
        cur_len = cur_len + send_ary.length;
        bytes[3] = send_ary;

        byte[] revclen_ary = cutBytes(data, cur_len, 4);
        int revc_len = ByteUtil.getInt(revclen_ary);
        cur_len = cur_len + revclen_ary.length;
        bytes[4] = revclen_ary;

        byte[] revc_ary = cutBytes(data, cur_len, revc_len);
        cur_len = cur_len + revc_ary.length;
        bytes[5] = revc_ary;

        byte[] total_ary = cutBytes(data, cur_len, 4);
        cur_len = cur_len + total_ary.length;
        bytes[6] = total_ary;

        byte[] msgnamelen_ary = cutBytes(data, cur_len, 4);
        int msgname_len = ByteUtil.getInt(msgnamelen_ary);
        cur_len = cur_len + msgnamelen_ary.length;
        bytes[7] = msgnamelen_ary;

        byte[] msgname_ary = cutBytes(data, cur_len, msgname_len);
        cur_len = cur_len + msgname_ary.length;
        bytes[8] = msgname_ary;

        byte[] curmsgtextlen_ary = cutBytes(data, cur_len, 4);
        int tlen = ByteUtil.getInt(curmsgtextlen_ary);
        cur_len = cur_len + curmsgtextlen_ary.length;
        bytes[9] = curmsgtextlen_ary;

        byte[] start_ary = cutBytes(data, cur_len, 4);
        cur_len = cur_len + start_ary.length;
        bytes[10] = start_ary;

        byte[] end_ary = cutBytes(data, cur_len, 4);
        cur_len = cur_len + end_ary.length;
        bytes[11] = end_ary;

        byte[] magtext_ary = cutBytes(data, cur_len, tlen);
        bytes[12] = magtext_ary;

        return bytes;

    }


    public static byte[] cutBytes(byte[] data, int startidx, int length) {
        byte[] ary = new byte[length];
        System.arraycopy(data, startidx, ary, 0, length);
        return ary;
    }

	/*

	  
		int cur_len=0;
		byte[] no_ary=cutBytes(data, cur_len, 8);
		System.out.println("cur_len "+cur_len);
		long myno=ByteUtil.getLong(no_ary);
		System.out.println("no "+myno);
		cur_len=cur_len+no_ary.length;
		
		byte[] type_ary=cutBytes(data, cur_len, 4);
		System.out.println("type "+ByteUtil.getInt(type_ary));
		cur_len=cur_len+type_ary.length;
		
		byte[] sendlen_ary=cutBytes(data, cur_len, 4);
		int send_len=ByteUtil.getInt(sendlen_ary);
		System.out.println("sendlen_ary "+send_len);
		cur_len=cur_len+sendlen_ary.length;
		
		byte[] send_ary=cutBytes(data, cur_len, send_len);
		String send_str=ByteUtil.getString(send_ary);
		System.out.println("send_ary "+send_str);
		cur_len=cur_len+send_ary.length;
		
		byte[] revclen_ary=cutBytes(data, cur_len, 4);
		int revc_len=ByteUtil.getInt(revclen_ary);
		System.out.println("revc_len "+revc_len);
		cur_len=cur_len+revclen_ary.length;
		
		byte[] revc_ary=cutBytes(data, cur_len, revc_len);
		String revc_str=ByteUtil.getString(revc_ary);
		System.out.println("revc_str "+revc_str);
		cur_len=cur_len+revc_ary.length;
		
		byte[] msgnamelen_ary=cutBytes(data, cur_len, 4);
		int msgname_len=ByteUtil.getInt(msgnamelen_ary);
		System.out.println("msgname_len  "+msgname_len);
		cur_len=cur_len+msgnamelen_ary.length;
		
		byte[] msgname_ary=cutBytes(data, cur_len,msgname_len);
		System.out.println("msgname_ary "+ByteUtil.getString(msgname_ary));
		cur_len=cur_len+msgname_ary.length;
		
		byte[] msgtextlen_ary=cutBytes(data, cur_len,4);
		int tlen=ByteUtil.getInt(msgtextlen_ary);
		System.out.println("msgtextlen_ary "+tlen);
		cur_len=cur_len+msgtextlen_ary.length;
		
		byte[] start_ary=cutBytes(data, cur_len,4);
		System.out.println("start_ary "+ByteUtil.getInt(start_ary));
		cur_len=cur_len+start_ary.length;
		
		byte[] end_ary=cutBytes(data, cur_len,4);
		System.out.println("end_ary  "+ByteUtil.getInt(end_ary));
		cur_len=cur_len+end_ary.length;
		
		byte[] magtext_ary=cutBytes(data,cur_len,tlen);
		System.out.println("magtext_ary "+ByteUtil.getString(magtext_ary));
	 
	 */
}
