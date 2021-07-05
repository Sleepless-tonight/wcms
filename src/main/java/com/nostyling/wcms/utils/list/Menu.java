package com.nostyling.wcms.utils.list;

import lombok.Data;

import java.util.List;
@Data
public class Menu {
    private String id;
    private String pid;
    private String name;
    private List<Menu> menuList;


}
