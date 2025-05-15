package com.hy.smartwork.ast;

import com.hy.smartwork.ast.dto.UrlTokenDto;
import com.hy.smartwork.ast.util.ScanControllerUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 动态解析当前工程的Controller类
 * 备注：只能放在原工程里使用，不能跨工程，因为是测试代码，原生产工程不方便提交，特此在这备份
 *
 * @author huangying
 */
public class StaticUrlScanner {

    private static final LoginCheckAnalyzer loginAnalyzer = new LoginCheckAnalyzer();

    /**
     * 程序推断的token权限记录
     */
    private static List<UrlTokenDto> urlTokenDtos = new ArrayList<>();

    /**
     * 人工核实确认URL的token权限记录
     * 允许有些内容跟程序推断是一致的
     */
    private static Map<String, Integer> manualUrls = new HashMap<>();

    static {
        /*
         * 0 必需token
         * 1 可选token
         * 2 不需要token
         */
        // 场景1：程序推断为必需token，之前人工推断为可选，二次确认为可选token（程序推断错误）
        manualUrls.put("/events/addClicksCount/v1", 1);
        manualUrls.put("/fifa/getTC/v1", 1);
        manualUrls.put("/promoLibrary/eligible/v1", 1);
        manualUrls.put("/signalProviderPromo/getLeaderboard/v1", 1);
        manualUrls.put("/auOlympic/getDetail/v1", 1);
        manualUrls.put("/olympic/getDetails/v1", 1);
        manualUrls.put("/rugby/getUserChampionDetails/v1", 1);
        manualUrls.put("/promoLibrary/getLeaderBoard/v1", 1);
        manualUrls.put("/euroCup/getMatchList/v1", 1);
        manualUrls.put("/demoTradingCompetition/getDetails/v1", 1);
        manualUrls.put("/euroCup/getUserRanking/v1", 1);
        manualUrls.put("/fifa/getMatchList/v1", 1);
        manualUrls.put("/signalProviderPromo/getDetails/v1", 1);
        manualUrls.put("/promoLibrary/getCountryLeaderBoard/v1", 1);
        manualUrls.put("/euroCup/showChampion/v1", 1);
        manualUrls.put("/productGuessingRF/home/v1", 1);
        manualUrls.put("/fourthMay/banner/v1", 1);

        // 场景2：程序推断为可选token，之前人工判断为必需token，二次确认为可选token（人工推断错误）
        manualUrls.put("/roulette/getDetail/v1", 1);
        manualUrls.put("/usercoupon/bit/checkExchangeCouponBlacklist/v1", 2);
        manualUrls.put("/usercoupon/qUseLossCoupTrades/v1", 2);
        manualUrls.put("/events/stApp/getEventsBlackWhiteList/v1", 2);
        manualUrls.put("/usercoupon/bit/sendCouponMessage/v1", 2);

        // 场景3：程序推断为必需token，但之前人工判断为可选token，二次确认为必需token（人工推断错误）==》调整原有的白名单列表
        manualUrls.put("/productGuessingRF/vote/v1", 0);
        manualUrls.put("/nonAgricultural/predict/v1", 0);

        // 场景4：程序推断为可选token，之前人工判断为必需token，二次确认为必需token（程序推断错误）
        manualUrls.put("/fifa/redeem/v1", 0);
        manualUrls.put("/easter/competeLotteryDraw/v1", 0);
        manualUrls.put("/stockActivity/randomStockList/v1", 0);
        manualUrls.put("/fifa/guess/v1", 0);
        manualUrls.put("/fifa/collectReward/v1", 0);
        manualUrls.put("/fifa/getRanking/v1", 0);
        manualUrls.put("/fifa/getUserInfo/v1", 0);
        manualUrls.put("/fifa/getChampionRanking/v1", 0);
        manualUrls.put("/fifa/getUserAccount/v1", 0);
        manualUrls.put("/fifa/popup/v1", 0);
        manualUrls.put("/fifa/guessRatio/v1", 0);
        manualUrls.put("/valentine/init/v1", 0);
        manualUrls.put("/newCredit/isJoinCreditActivity/v1", 0);
        manualUrls.put("/stockActivity/stockList/v1", 0);
        manualUrls.put("/PUEaster/sendCoupon/v1", 0);
        manualUrls.put("/stockActivity/stockListDetail/v1", 0);
        manualUrls.put("/stockActivity/stockListDetail/v2", 0);
        manualUrls.put("/newCredit/redeemCredit/v1", 0);


        // 场景4：已经下线的活动，默认不加入白名单，按必需token处理
        manualUrls.put("/tradeLossActivity/getRedeemedList/v1", 0);

        // 场景5：邀请活动相关的，全部为必需token
        manualUrls.put("/invite/detail/v1", 0);
        manualUrls.put("/invite/coupon/v1", 0);
        manualUrls.put("/invite/coupon/agree/v1", 0);
        manualUrls.put("/invite/agree/v1", 0);
        manualUrls.put("/invite/channel/v1", 0);
        manualUrls.put("/invite/onOff/v1", 0);
        manualUrls.put("/invite/test/v1", 0);
        manualUrls.put("/invite/delete/v1", 0);
        manualUrls.put("/invite/data/v1", 0);
        manualUrls.put("/invite/sendEmail/v1", 0);
        manualUrls.put("/invite/jumpData/v1", 0);
    }

    public static void main(String[] args) {
        loginAnalyzer.analyzeControllers("com.hy.controller.activities");
        loginAnalyzer.analyzeControllers("com.hy.controller.crm.crmactivity");
        loginAnalyzer.analyzeControllers("com.hy.controller.events");
        loginAnalyzer.analyzeControllers("com.hy.controller.user");
        loginAnalyzer.analyzeControllers("com.hy.controller.invite");

        Set<Class<?>> controllers = new HashSet<>();
        // 按包扫描，包目录下全部类都需要
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.activities", true));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.crm.crmactivity", true));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.events", true));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.user", false));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.invite", true));

        // 额外处理的类
        // 仅需要指定包目录下的某个类
//        controllers.add(UserCouponController.class);
        // 需要移除的类
//        controllers.remove(TestNewcomerCouponController.class);

        parseControllers(controllers);

        for (UrlTokenDto dto : urlTokenDtos) {
            if (StringUtils.isNotEmpty(dto.output())) {
                System.out.println(dto.output());
            }
//            if (StringUtils.isNotEmpty(dto.outputForYml())) {
//                System.out.println(dto.outputForYml());
//            }
//            if (StringUtils.isNotEmpty(dto.outputForProgram())) {
//                System.out.println(dto.outputForProgram());
//            }
        }
    }


    /**
     * 解析Controller类
     *
     * @param controllers
     */
    private static void parseControllers(Set<Class<?>> controllers) {
        for (Class<?> clazz : controllers) {
            String classPath = ScanControllerUtil.getClassPath(clazz);
            UrlTokenDto dto = null;
            for (Method method : clazz.getDeclaredMethods()) {
                String[] urls = ScanControllerUtil.getMethodUrls(method, classPath);
                if (urls.length == 0) {
                    continue;
                }

                // 优先从人工核实的URL获取结果
                if (manualUrls.containsKey(urls[0])) {
                    dto = new UrlTokenDto();
                    dto.setUrl(urls[0]);
                    dto.setTokenType(manualUrls.get(urls[0]));
                    urlTokenDtos.add(dto);
                    continue;
                }

                // 从缓存中获取登录校验结果
                String className = clazz.getSimpleName();
                String key = className + "-" + method.getName();
                boolean requiresLogin = loginAnalyzer.requiresLogin(key);

                dto = new UrlTokenDto();
                if (requiresLogin) {
                    dto.setUrl(urls[0]);
                    dto.setTokenType(0);
                } else {
                    dto.setUrl(urls[0]);
                    dto.setTokenType(1);
                }
                urlTokenDtos.add(dto);
            }
        }
    }
}