package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.item.ItemComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCommentsRepository extends JpaRepository<ItemComments, String> {

    public List<ItemComments> findByItemUuid(String itemUuid);

    public List<ItemComments> findByItemUuidAndCommentParentUuidAndCommentDepth(String itemUuid, ItemComments commentParentUuid, int depth);
}
