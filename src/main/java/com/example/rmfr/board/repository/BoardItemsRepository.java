package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.BoardItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BoardItemsRepository extends JpaRepository<BoardItems, String>, JpaSpecificationExecutor<BoardItems> {

    Page<BoardItems> findByItemStatus(int itemStatus, PageRequest pageRequest);
    BoardItems findByItemUuid(String itemUuid);
    Optional<BoardItems> findByItemSeq(int seq);

}
