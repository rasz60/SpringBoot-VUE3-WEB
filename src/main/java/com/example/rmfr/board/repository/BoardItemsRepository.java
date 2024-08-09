package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.BoardItems;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BoardItemsRepository extends JpaRepository<BoardItems, String>, JpaSpecificationExecutor<BoardItems> {

    BoardItems findByItemSeq(int seq);

}
