package com.hy.smartwork.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据省市区级联关联的数据，生成一份树状的结构的JSON内容
 * 原文本数据从Excel等表格拷贝而来，每行中间用逗号分隔，示例：中国,北京
 * 目前支持两级：省市
 *
 * @author huangying1
 */
public class DistrictJsonDemo {

    public static void main(String[] args) {
        String text = "Abu Dhabi,Abu Dhabi\n" +
                "Abu Dhabi,Al Ain\n" +
                "Abu Dhabi,Madinat Zayed\n" +
                "Abu Dhabi,Ruwais\n" +
                "Abu Dhabi,Ghayathi\n" +
                "Abu Dhabi,Liwa Oasis\n" +
                "Abu Dhabi,Abu al Abyad\n" +
                "Abu Dhabi,Al Ajban\n" +
                "Abu Dhabi,Al Aryam\n" +
                "Abu Dhabi,Al Faqa\n" +
                "Abu Dhabi,Al Mirfa\n" +
                "Abu Dhabi,Al Shuwaib\n" +
                "Abu Dhabi,Al Yahar\n" +
                "Abu Dhabi,Dalma\n" +
                "Abu Dhabi,Habshan\n" +
                "Abu Dhabi,Marawah\n" +
                "Abu Dhabi,Nahil\n" +
                "Abu Dhabi,Sila\n" +
                "Abu Dhabi,Sweihan\n" +
                "Dubai,Dubai\n" +
                "Dubai,Jebel Ali\n" +
                "Dubai,Hatta\n" +
                "Dubai,Al Awir\n" +
                "Dubai,Al Khawaneej\n" +
                "Dubai,Al Lisaili\n" +
                "Dubai,Al Ruwayyah\n" +
                "Dubai,Lahbab\n" +
                "Sharjah,Sharjah\n" +
                "Sharjah,Kalba\n" +
                "Sharjah,Khor Fakkan\n" +
                "Sharjah,Dhaid\n" +
                "Sharjah,Dibba Al-Hisn\n" +
                "Sharjah,Al Bataeh\n" +
                "Sharjah,Al Hamriyah\n" +
                "Sharjah,Al Madam\n" +
                "Sharjah,Mleiha\n" +
                "Sharjah,Zubarah\n" +
                "Ajman,Ajman\n" +
                "Ajman,Al Manama\n" +
                "Ajman,Masfut\n" +
                "Ras Al Khaimah,Ras Al Khaimah\n" +
                "Ras Al Khaimah,Ar-Rams\n" +
                "Ras Al Khaimah,Al Jazirah Al Hamra\n" +
                "Ras Al Khaimah,Adhen\n" +
                "Ras Al Khaimah,Al Hamraniyah\n" +
                "Ras Al Khaimah,Al Jeer\n" +
                "Ras Al Khaimah,Al Qusaidat\n" +
                "Ras Al Khaimah,Al Qor\n" +
                "Ras Al Khaimah,Asimah\n" +
                "Ras Al Khaimah,Digdaga\n" +
                "Ras Al Khaimah,Ghalilah\n" +
                "Ras Al Khaimah,Ghayl\n" +
                "Ras Al Khaimah,Huwaylat\n" +
                "Ras Al Khaimah,Khatt\n" +
                "Ras Al Khaimah,Khor Khwair\n" +
                "Ras Al Khaimah,Sha'am\n" +
                "Ras Al Khaimah,Wadi Shah\n" +
                "Fujairah,Fujairah\n" +
                "Fujairah,Dibba Al-Fujairah\n" +
                "Fujairah,Al Badiyah\n" +
                "Fujairah,Al Bithnah\n" +
                "Fujairah,Al Halah\n" +
                "Fujairah,Dadna\n" +
                "Fujairah,Ghub\n" +
                "Fujairah,Mirbah\n" +
                "Fujairah,Qidfa\n" +
                "Umm al-Quwain,Umm Al Quwain\n" +
                "Umm al-Quwain,Al Salamah\n" +
                "Umm al-Quwain,Al Rafaah\n" +
                "Umm al-Quwain,Al Rashidya\n" +
                "Umm al-Quwain,Falaj Al Mualla\n";

        String[] districtArray = text.split("\n");

        Map<String, DistrictInfo> map = new LinkedHashMap<>();
        for (String district : districtArray) {
            String[] row = district.split(",");
            String country = row[0];
            String city = row[1];
            if (map.containsKey(country)) {
                DistrictInfo countryInfo = map.get(country);
                if (null == countryInfo.getChildren()) {
                    countryInfo.setChildren(new ArrayList<>());
                }

                DistrictInfo cityInfo = new DistrictInfo();
                cityInfo.setCode(countryInfo.getCode() + String.format("%02d", countryInfo.getChildren().size() + 1));
                cityInfo.setName(city);
                countryInfo.getChildren().add(cityInfo);
            } else {
                DistrictInfo countryInfo = new DistrictInfo();
                countryInfo.setCode(String.format("%02d", map.size() + 1));
                countryInfo.setName(country);
                map.put(country, countryInfo);
            }
        }

        // 对Map的Value集合进行反序列化
        // 定义一个 ValueFilter，用于过滤空集合
        ValueFilter filter = (object, name, value) -> {
            if (value instanceof java.util.List && ((java.util.List<?>) value).isEmpty()) {
                return null; // 将空集合设置为 null
            }
            return value;
        };

        // 注册过滤器
        SerializeConfig config = new SerializeConfig();
        config.addFilter(DistrictInfo.class, filter);

        System.out.println(JSON.toJSONString(map.values()));
    }

    /**
     * 地区实体类
     * 使用一个静态内部类，更简单便捷
     */
    static class DistrictInfo implements Serializable {

        @JSONField(ordinal = 1)
        private String code;

        @JSONField(ordinal = 2)
        private String name;

        @JSONField(ordinal = 3)
        private List<DistrictInfo> children;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DistrictInfo> getChildren() {
            return children;
        }

        public void setChildren(List<DistrictInfo> children) {
            this.children = children;
        }
    }
}
