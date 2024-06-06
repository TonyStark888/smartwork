package com.hy.smartwork.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 将ErrorCode的错误码定义，提取错误码和注释，用于补充message.properties配置文件
 * 注意：错误码要使用标准的注释格式才行
 *
 * @author huangying1
 */
public class ExtractErrorMsg {

    public static void main(String[] args) {
        String str = "/**\n" +
                "\t * pin码锁定中不允许做与需要pin相关的操作\n" +
                "\t */\n" +
                "\tString PIN_HOLDING_ERROR = \"21001\";\n" +
                "\t/**\n" +
                "\t * pin码错还剩多少次机会\n" +
                "\t */\n" +
                "\tString PIN_ERROR_LEFT = \"21002\";\n" +
                "\t/**\n" +
                "\t * 验证码错误\n" +
                "\t */\n" +
                "\tString VERIFICATION_DATA_ERROR = \"21003\";\n" +
                "\t/**\n" +
                "\t * 您的账号已经被冻结，如有疑问请联系客服\n" +
                "\t */\n" +
                "\tString BLOCK_CODE_USER_ERROR = \"21004\";\n" +
                "\t/**\n" +
                "\t * 登陆相关 手机号未注册\n" +
                "\t */\n" +
                "\tString PHONE_NUMBER_NOT_EXISTS = \"21005\";\n" +
                "\t/**\n" +
                "\t * 手势密码错误还剩多少次机会\n" +
                "\t */\n" +
                "\tString GESTURE_PWD_ERROR_LEFT = \"21006\";\n" +
                "\t/**\n" +
                "\t * 手势密码过期\n" +
                "\t */\n" +
                "\tString GESTURE_EXPIRE = \"21007\";\n" +
                "\t/**\n" +
                "\t * 手势密码连续错误被锁定，请使用账号密码登录\n" +
                "\t */\n" +
                "\tString GESTURE_PWD_HOLDING_ERROR = \"21008\";\n" +
                "\t/**\n" +
                "\t * 账户在其他设备登录\n" +
                "\t */\n" +
                "\tString GESTURE_LOGIN_ON_OTHER_DEVICE = \"21009\";\n" +
                "\t/**\n" +
                "\t * 冻结用户解冻失败\n" +
                "\t */\n" +
                "\tString LOGIN_UNFREEZE_ERROR = \"21010\";\n" +
                "\t/**\n" +
                "\t * 手机号已存在\n" +
                "\t */\n" +
                "\tString PHONE_NUMBER_EXISTS = \"21011\";\n" +
                "\t/**\n" +
                "\t * email格式错误\n" +
                "\t */\n" +
                "\tString EMAIL_FORMAT_ERROR = \"21012\";\n" +
                "\t/**\n" +
                "\t * email 已存在\n" +
                "\t */\n" +
                "\tString EMAIL_EXISTS = \"21013\";\n" +
                "\t/**\n" +
                "\t * Token 被挤掉线\n" +
                "\t */\n" +
                "\tString TOKEN_OUT_INVALIDATE = \"21014\";\n" +
                "\t/**\n" +
                "\t * 手机号码格式不正确\n" +
                "\t */\n" +
                "\tString PHONE_NUMBER_FORMAT_ERROR = \"21015\";\n" +
                "\t/**\n" +
                "\t * 类型不正确\n" +
                "\t */\n" +
                "\tString VERIFICATION_TYPE_ERROR = \"21016\";\n" +
                "\t/**\n" +
                "\t * 登录和确认密码不相等\n" +
                "\t */\n" +
                "\tString LOGIN_AND_CONFIRM_PWD_NOT_EQUALS = \"21017\";\n" +
                "\t/**\n" +
                "\t * 邀请码错误\n" +
                "\t */\n" +
                "\tString INVITE_CODE_ERROR = \"21018\";\n" +
                "\t/**\n" +
                "\t * 新登陆密码不能和上次设置的一致\n" +
                "\t */\n" +
                "\tString LOGIN_PWD_CAN_NOT_EQUALS_LAST = \"21019\";";

        StringBuffer sb = new StringBuffer();
        List<String> msg = new ArrayList<>();
        List<String> code = new ArrayList<>();
        Integer index = 0;
        try {
            BufferedReader reader = new BufferedReader(new StringReader(str));
            String line;
            while ((line = reader.readLine()) != null) {
                // 在这里处理每一行的内容，比如打印到控制台
                if (line.contains("// *******")) {
                    continue;
                }
                if (line.contains("* ")) {
                    msg.add(line.replace("* ", "").trim());
                }
                if (line.contains("=")) {
                    String[] split = line.split("=");
                    code.add(split[1].replace(";", "").replaceAll("\"", "").trim());
                    index++;
                }
            }
            for (int i = 0; i < index; i++) {
                sb.append(code.get(i));
                sb.append("=");

                // 中文转换成unicode码
//                for (int j = 0; j < msg.get(i).length(); j++) {
//                    char c = msg.get(i).charAt(j);
//                    sb.append("\\u" + Integer.toHexString(c | 0x10000).substring(1));
//                }
                // 直接添加中文文案的场景
                sb.append(msg.get(i));
                sb.append("\n");
            }
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
