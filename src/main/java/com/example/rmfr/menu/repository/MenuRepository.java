package com.example.rmfr.menu.repository;

import com.example.rmfr.menu.entity.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MenuRepository extends JpaRepository<Menus, String> {

    public List<Menus> findByMenuStatusLessThanEqualOrderByMenuSectionAscMenuSeqAsc(int menuStatus);
}
