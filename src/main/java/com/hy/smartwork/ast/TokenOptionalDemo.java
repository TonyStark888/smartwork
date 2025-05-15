package com.hy.smartwork.ast;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据方法对token字段的定义，结合之前人工排查的白名单接口
 * 核算一下差异
 */
public class TokenOptionalDemo {

    /**
     * 程序计算出来的无token的URL方法
     */
    private static String[] optionalToken = new String[]{
            "/commonactivity/insertActivityUserInfo/v1",
            "/commonactivity/country/v1",
            "/fourthMay/banner/v1",
            "/fifa/getUserInfo/v1",
            "/fifa/getUserAccount/v1",
            "/fifa/getChampionRanking/v1",
            "/fifa/collectReward/v1",
            "/fifa/getRanking/v1",
            "/fifa/redeem/v1",
            "/fifa/guessRatio/v1",
            "/fifa/guess/v1",
            "/fifa/popup/v1",
            "/nonAgricultural/home/v1",
            "/nonAgricultural/getLeaderboard/v1",
            "/easter/getReward/v1",
            "/easter/competeLotteryDraw/v1",
            "/easter/home/v1",
            "/PUEaster/sendCoupon/v1",
            "/newerGiftActivity/getWBPStatus/v2",
            "/newerGiftActivity/getWBPStatus/v1",
            "/roulette/rewards/countries/v1",
            "/roulette/ranking/v1",
            "/roulette/getDetail/v1",
            "/valentine/init/v1",
            "/valentine/collectReward/v1",
            "/tradingContest/getParticipateStatus/v1",
            "/ftdActivity/randomRedeemedList/v1",
            "/newCredit/redeemCredit/v1",
            "/newCredit/isJoinCreditActivity/v1",
            "/newCredit/getNDBInfo/v1",
            "/stockActivity/randomStockList/v1",
            "/stockActivity/stockList/v1",
            "/stockActivity/stockListDetail/v1",
            "/stockActivity/stockListDetail/v2",
            "/loyalty/redeemCashByPoints/v1",
            "/loyalty/calculatePoints/v1",
            "/millionPromotion/getUserDetail/v1",
            "/millionPromotion/participate/v1",
            "/events/stApp/getEventsList/v1",
            "/events/stApp/getEventsBlackWhiteList/v1",
            "/events/getListByListStream/v1",
            "/events/getList/v2",
            "/events/getList/v1",
            "/getEventsAndRookieTicket/v1",
            "/inAppInfo/list/v1",
            "/maintenance/v2",
            "/maintenance/v1",
            "/popWindow/v2",
            "/popWindow/v1",
            "/promoTab/v1",
            "/newcomerGift/lang/v1",
            "/newcomerGift/gift/v1",
            "/img/advert-info/v1",
            "/img/query/v1",
            "/usercoupon/qUseLossCoupTrades/v1",
            "/usercoupon/bit/sendCouponMessage/v1",
            "/usercoupon/bit/checkExchangeCouponBlacklist/v1"
    };

    /**
     * 人工排查的可选token的URL方法
     */
    private static final String[] manualToken = new String[]{
            "/loyalty/redeemCashByPoints/v1",
            "/loyalty/calculatePoints/v1",
            "/promoLibrary/eligible/v1",
            "/promoLibrary/getLeaderBoard/v1",
            "/promoLibrary/getCountryLeaderBoard/v1",
            "/auOlympic/getDetail/v1",
            "/commonactivity/insertActivityUserInfo/v1",
            "/commonactivity/country/v1",
            "/demoTradingCompetition/getDetails/v1",
            "/fifa/getMatchList/v1",
            "/fifa/getTC/v1",
            "/fourthMay/banner/v1",
            "/newerGiftActivity/getWBPStatus/v2",
            "/newerGiftActivity/getWBPStatus/v1",
            "/nonAgricultural/home/v1",
            "/nonAgricultural/predict/v1",
            "/nonAgricultural/getLeaderboard/v1",
            "/productGuessingRF/home/v1",
            "/valentine/collectReward/v1",
            "/getEventsAndRookieTicket/v1",
            "/events/getList/v1",
            "/tradeLossActivity/getRedeemedList/v1",
            "/events/getList/v2",
            "/events/addClicksCount/v1",
            "/events/getListByListStream/v1",
            "/events/stApp/getEventsList/v1",
            "/productGuessingRF/vote/v1",
            "/productGuessingRF/home/v1",
            "/euroCup/showChampion/v1",
            "/euroCup/getMatchList/v1",
            "/euroCup/getUserRanking/v1",
            "/olympic/getDetails/v1",
            "/roulette/rewards/countries/v1",
            "/roulette/ranking/v1",
            "/rugby/getUserChampionDetails/v1",
            "/signalProviderPromo/getDetails/v1",
            "/signalProviderPromo/getLeaderboard/v1",
            "/img/query/v1",
            "/img/advert-info/v1",
            "/millionPromotion/getUserDetail/v1",
            "/millionPromotion/participate/v1",
            "/newcomerGift/lang/v1",
            "/newcomerGift/gift/v1",
            "/popWindow/v1",
            "/popWindow/v2",
            "/promoTab/v1",
            "/maintenance/v1",
            "/maintenance/v2",
            "/inAppInfo/list/v1",
            "/newCredit/getNDBInfo/v1",
    };

    public static void main(String[] args) {
        Set<String> optionalSet = new HashSet<>(Arrays.asList(optionalToken));
        Set<String> manualSet = new HashSet<>(Arrays.asList(manualToken));

        System.out.println("original size:" + optionalSet.size());
        System.out.println("manual size:" + manualSet.size());

        // A与B做交集，确认两边相同的URL
        optionalSet.retainAll(manualSet);
        System.out.println("两边都相同的URL，数量：" + optionalSet.size());
        System.out.println(optionalSet);

        System.out.println("\n**********");
        // A->B做差集，得到A中不在B中的元素
        Set<String> optionalSet1 = new HashSet<>(Arrays.asList(optionalToken));
        Set<String> manualSet1 = new HashSet<>(Arrays.asList(manualToken));

        optionalSet1.removeAll(manualSet1);
        System.out.println("人工排查记录里没有的记录：");
        System.out.println(optionalSet1.size());
        System.out.println(optionalSet1);

        System.out.println("\n**********");
        // B->A做差集，得到B中不在A中的元素
        Set<String> optionalSet2 = new HashSet<>(Arrays.asList(optionalToken));
        Set<String> manualSet2 = new HashSet<>(Arrays.asList(manualToken));
        manualSet2.removeAll(optionalSet2);
        System.out.println("程序探测记录里没有的记录：");
        System.out.println(manualSet2.size());
        System.out.println(manualSet2);
    }
}
