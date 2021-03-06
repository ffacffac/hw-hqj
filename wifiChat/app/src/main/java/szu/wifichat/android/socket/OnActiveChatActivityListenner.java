package szu.wifichat.android.socket;

import szu.wifichat.android.entity.GroundMessage;

/** 接收消息监听的listener接口 **/
public interface OnActiveChatActivityListenner {

    /**
     * 判断收到的消息是否匹配已打开的聊天窗口
     * 
     * @param paramMsg
     *            收到的消息对象
     * @return
     */
    public boolean isThisActivityMsg(GroundMessage paramMsg);
}