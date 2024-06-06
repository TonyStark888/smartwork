package com.hy.smartwork.poker;

/**
 * 扑克牌 对象
 * @author huangying1
 */
public class PokerInfo {

    /**
     * 扑克牌花色类型：黑桃（Spade）、红桃（Heart）、方块（Diamond）、梅花（Club）
     */
    private String suitTypes;

    /**
     * 点数，从A到K，共13种
     */
    private String point;

    /**
     * 持牌归属人的uid，未发放时为null
     */
    private Long owner;

    /**
     * 单张扑克牌的状态：0 未发牌， 1 已发牌， 2 已打出
     */
    private Integer status;

    /**
     * 是否为底牌
     */
    private Boolean baseCard;
}
