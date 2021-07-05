package com.nostyling.wcms.utils.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListToTreeUtil {
    public static void main(String[] args) {
        String json = "[\n" +
                "    {\n" +
                "        \"name\":\"甘肃省\",\n" +
                "        \"pid\":0,\n" +
                "        \"id\":1\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"天水市\",\n" +
                "        \"pid\":1,\n" +
                "        \"id\":2\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"秦州区\",\n" +
                "        \"pid\":2,\n" +
                "        \"id\":3\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"北京市\",\n" +
                "        \"pid\":0,\n" +
                "        \"id\":4\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"昌平区\",\n" +
                "        \"pid\":4,\n" +
                "        \"id\":5\n" +
                "    }\n" +
                "]";
        List<Menu> menus = JSONArray.parseArray(json, Menu.class);
        System.out.println(menus);
        List<Menu> menus1 = toTree(menus,"0");
        System.out.println(JSON.toJSONString(menus1));


    }

    /**
     * 转化树形结构
     * 1、首先把list转成map  pid作为key，List<Menu>作为value
     * 2、递归转化Tree结构
     * @param list
     * @return
     */
    public static List<Menu> toTree(List<Menu> list,String pid) {
        Map<String, List<Menu>> map = list.stream().
                filter(
                        a -> {
                            return null != a.getPid();
                        }
                )
                .collect(
                        Collectors.groupingBy(
                                Menu::getPid
                        )
                );
        List<Menu> menus = map.get(pid);
        recursionToTree(menus, map);
        return menus;
    }

    /**
     * 递归转化Tree结构
     * @param list，map
     * @param map
     */
    public static void recursionToTree(List<Menu> list, Map<String, List<Menu>> map){
        for(Menu tn : list){
            String key = tn.getId();
            if(map.containsKey(key)) {
                List<Menu> children = map.get(key);
                tn.setMenuList(children);
                recursionToTree(children, map);
            }
        }
    }

}
