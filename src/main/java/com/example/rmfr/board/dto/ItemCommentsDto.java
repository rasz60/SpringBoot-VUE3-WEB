package com.example.rmfr.board.dto;

import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.member.entity.Members;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
public class ItemCommentsDto {

    private String itemUuid;
    private String commentUuid;
    private ItemComments commentParentUuid;
    private String commentContents;
    private int commentStatus;
    private Members commentRegUuid;
    private LocalDateTime commentRegDate;
    private Members commentUpdaterUuid;
    private LocalDateTime commentUpdateDate;
    private int commentLikeCnt;
    public ItemCommentsDto() {}

    public void of(ItemComments itemComments) {
        this.itemUuid = itemComments.getItemUuid();
        this.commentUuid = itemComments.getCommentUuid();
        this.commentParentUuid = itemComments.getCommentParentUuid();
        this.commentContents = itemComments.getCommentContents();
        this.commentStatus = itemComments.getCommentStatus();
        this.commentRegUuid = itemComments.getCommentRegUuid();
        this.commentRegDate = itemComments.getCommentRegDate();
        this.commentUpdaterUuid = itemComments.getCommentUpdaterUuid();
        this.commentUpdateDate = itemComments.getCommentUpdateDate();
        this.commentLikeCnt = itemComments.getCommentLikes().size();
    }
}
