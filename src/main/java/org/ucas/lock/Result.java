package org.ucas.lock;

/**
 * Author: Lucio.yang
 * Date: 17-5-15
 * 消息结果
 */
public class Result {
    /**
     * 请求结果
     */
    private Boolean r;
    /**
     * 1:请求锁，2：释放锁
     */
    private int msgType;

    public Result(Boolean _r,int _msgType){
        r=_r;
        msgType=_msgType;
    }

    public Boolean getR() {
        return r;
    }

    public void setR(Boolean r) {
        this.r = r;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
