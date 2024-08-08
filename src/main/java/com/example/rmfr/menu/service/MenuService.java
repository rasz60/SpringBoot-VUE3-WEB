package com.example.rmfr.menu.service;

import com.example.rmfr.menu.dto.MenusDto;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
@Service
public interface MenuService {

    public List<MenusDto> getMenus(Principal principal);
}
