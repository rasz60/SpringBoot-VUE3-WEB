package com.example.rmfr.menu.controller;

import com.example.rmfr.menu.dto.MenusDto;
import com.example.rmfr.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
public class MenusRestController {

    private final MenuService menuService;

    @GetMapping("/rest/menuList")
    public List<MenusDto> getMenus(Principal principal) {
        return menuService.getMenus(principal);
    }
}
