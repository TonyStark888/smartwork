package com.hy.smartwork.poker;

/**
 * 扑克牌玩法的上下文类
 * @author huangying1
 */
public class PokerPlayContext {

    private PokerPlayStrategy pokerPlayStrategy;

    public void setPokerPlayStrategy(PokerPlayStrategy pokerPlayStrategy) {
        this.pokerPlayStrategy = pokerPlayStrategy;
    }

    // 以下是基本的玩法行为，包括玩法选择规则，省略
}
