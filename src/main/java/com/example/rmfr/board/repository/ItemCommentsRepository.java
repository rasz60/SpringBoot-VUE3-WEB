package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.item.ItemComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemCommentsRepository extends JpaRepository<ItemComments, String> {
    @Query("SELECT c FROM ItemComments c WHERE c.itemUuid = ?1 AND c.commentDepth = ?2 ORDER BY SIZE(c.commentLikes) DESC")
    public List<ItemComments> findByItemUuidAndCommentDepth(String itemUuid, int depth);
    @Query("SELECT c FROM ItemComments c WHERE c.itemUuid = ?1 AND c.commentParentUuid = ?2 AND c.commentDepth = ?3 ORDER BY SIZE(c.commentLikes) DESC")
    public List<ItemComments> findByItemUuidAndCommentParentUuidAndCommentDepth(String itemUuid, ItemComments commentParentUuid, int depth);
}
