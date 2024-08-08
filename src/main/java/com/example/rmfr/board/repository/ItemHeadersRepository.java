package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.item.ItemHeaders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemHeadersRepository extends JpaRepository<ItemHeaders, String> {

    List<ItemHeaders> findByItemHeaderAuth(int auth);
}
