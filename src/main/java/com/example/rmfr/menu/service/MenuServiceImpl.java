package com.example.rmfr.menu.service;

import com.example.rmfr.menu.dto.MenusDto;
import com.example.rmfr.menu.entity.Menus;
import com.example.rmfr.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public List<MenusDto> getMenus(Principal principal) {
        List<MenusDto> dtos = new ArrayList<>();
        try {
            int status = principal != null ? 1 : 0;
            List<Menus> entities = menuRepository.findByMenuStatusLessThanEqualOrderByMenuSectionAscMenuSeqAsc(status);

            for ( Menus menu : entities ) {
                dtos.add(new MenusDto().of(menu));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return dtos;
    }
}
