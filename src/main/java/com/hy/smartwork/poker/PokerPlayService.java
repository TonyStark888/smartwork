package com.hy.smartwork.poker;

import java.util.List;

/**
 * 扑克牌玩法提供的方法
 */
public interface PokerPlayService {

    /**
     * 开局，扑克牌洗牌
     * @return
     */
    public List<PokerInfo> init();

    /**
     * 发牌逻辑，以斗地主为例，
     * 1、发牌，标记owner、status属性
     * 2、设置底牌，标记baseCard属性
     * 3、抢注，抢到地主的玩家多给3张牌
     * @param list
     */
    public void deliver(List<PokerInfo> list);

    /**
     * 出牌逻辑，以斗地主为例，
     * 1、出牌，判断出牌是否符合规则
     * 2、比较是否能够胜出上一手的出牌
     * @param hand
     */
    public boolean sendOut(List<PokerInfo> hand);

    /**
     * 判断某玩家是否胜出
     * @param owner
     */
    public boolean isWin(Long owner);
}
