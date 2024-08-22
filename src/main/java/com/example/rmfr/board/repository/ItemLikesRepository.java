package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.item.ItemLikes;
import com.example.rmfr.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemLikesRepository extends JpaRepository<ItemLikes, Long> {
    Long countByItemUuid(String itemUuid);
    Long countByItemUuidAndItemLikerUuid(String itemUuid, Members member);
    Optional<ItemLikes> findByItemUuidAndItemLikerUuid(String itemUuid, Members member);
}
