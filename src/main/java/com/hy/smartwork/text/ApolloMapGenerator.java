package com.hy.smartwork.text;

/**
 * 快速生成apollo map类的配置信息
 * apollo配置中的map结构示例：
 * system.services[0].id = 1
 * system.services[0].name = Message Center
 * system.services[0].url = neobank://v.bnc.com/3
 *
 * 生成逻辑：
 * id自增即可
 * name的配置是从其他文本拷贝过来的，一行一个
 * url先统一生成一个固定的，然后再改成想要的地址
 *
 * @author huangying1
 */
public class ApolloMapGenerator {

    public static void main(String[] args) {
        String str = "Message Center\n" +
                "Scan QR\n" +
                "Add Balance\n" +
                "History\n" +
                "Water Bill\n" +
                "Electricity Bill\n" +
                "Phone Credit\n" +
                "Transfer\n" +
                "Internet\n" +
                "TopUp\n" +
                "Mobile Data\n" +
                "Promote\n" +
                "Invited\n" +
                "Register\n" +
                "Open Account\n" +
                "Identity Verification\n" +
                "Transaction History\n" +
                "Setting\n" +
                "Feedbacks\n" +
                "FAQs\n";
        String[] split = str.split("\n");
        int index = 0;
        StringBuffer sb = new StringBuffer();
        for (String item : split) {
            sb.append("system.services[").append(index).append("].id = ").append(index + 1).append("\n");
            sb.append("system.services[").append(index).append("].name = ").append(item).append("\n");
            sb.append("system.services[").append(index).append("].url = neobank://v.bnc.com/3").append("\n\n");
            index++;
        }

        System.out.println(sb.toString());
    }
}
