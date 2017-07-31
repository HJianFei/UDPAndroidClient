package com.apace.udp;

import com.apace.udp.utils.CharsetUtil;

/**
 * UDP配置
 */
public class UDPConfig {
    private String charsetName = CharsetUtil.UTF_8;//默认编码
    private long receiveTimeout = 10000;//接受消息的超时时间,0为无限大
    private int localPort = -1;

    private UDPConfig() {
    }

    public String getCharsetName() {
        return charsetName;
    }


    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public int getLocalPort() {
        return localPort;
    }

    public static class Builder {
        private UDPConfig mTcpConnConfig;

        public Builder() {
            mTcpConnConfig = new UDPConfig();
        }

        public UDPConfig create() {
            return mTcpConnConfig;
        }

        public Builder setCharsetName(String charsetName) {
            mTcpConnConfig.charsetName = charsetName;
            return this;
        }

        public Builder setReceiveTimeout(long timeout) {
            mTcpConnConfig.receiveTimeout = timeout;
            return this;
        }

        public Builder setLocalPort(int localPort) {
            mTcpConnConfig.localPort = localPort;
            return this;
        }
    }
}
