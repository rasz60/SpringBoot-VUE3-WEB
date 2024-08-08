package com.example.rmfr.menu.dto;

import com.example.rmfr.member.entity.Members;
import com.example.rmfr.menu.entity.Menus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MenusDto {

    private String menuUuid;
    private String menuName;
    private int menuType;
    private String menuClass;
    private int menuSection;
    private int menuSeq;
    private String menuLink;
    private int menuStatus;
    private int menuAuth;
    private Members menuRegUuid;
    private LocalDateTime menuRegDate;
    private Members menuUpdaterUuid;
    private LocalDateTime menuUpdateDate;

    public MenusDto() {}

    public MenusDto of(Menus menus) {
        this.menuUuid = menus.getMenuUuid();
        this.menuName = menus.getMenuName();
        this.menuType = menus.getMenuType();
        this.menuClass = menus.getMenuType() == 0 ? "titles" : "subMenus";
        this.menuSection = menus.getMenuSection();
        this.menuSeq = menus.getMenuSeq();
        this.menuLink = menus.getMenuLink();
        this.menuStatus = menus.getMenuStatus();
        this.menuAuth = menus.getMenuAuth();
        this.menuRegUuid = menus.getMenuRegUuid();
        this.menuRegDate = menus.getMenuRegDate();
        this.menuUpdaterUuid = menus.getMenuUpdaterUuid();
        this.menuUpdateDate = menus.getMenuUpdateDate();
        return this;
    }
}
