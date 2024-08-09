package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.board.entity.item.ItemHits;
import com.example.rmfr.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHitsRepository extends JpaRepository<ItemHits, Long> {

    Long countByItemUuidAndItemHitMemUuid(BoardItems boardItems, Members members);
}
