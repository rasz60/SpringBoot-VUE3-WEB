package com.example.rmfr.board.dto;

import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.member.entity.Members;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemCommentsDto {

    private String itemUuid;
    private String commentUuid;
    private int commentDepth;
    private ItemCommentsDto parentComment;
    private String commentParentUuid;
    private String commentContents;
    private int commentStatus;
    private Members commentRegUuid;
    private LocalDateTime commentRegDate;
    private Members commentUpdaterUuid;
    private LocalDateTime commentUpdateDate;
    private int commentLikeCnt;
    private int commentChildCnt;

    private boolean editYn;
    private boolean likeComment;
    private boolean childYn;
    private boolean childOpen;
    private boolean eAuth;
    private boolean dAuth;
    public ItemCommentsDto() {}

    public static ItemCommentsDto of(ItemComments itemComments) {
        ItemCommentsDto dto = new ItemCommentsDto();

        dto.setItemUuid(itemComments.getItemUuid());
        dto.setCommentUuid(itemComments.getCommentUuid());
        dto.setCommentDepth(itemComments.getCommentDepth());

        if ( itemComments.getCommentParentUuid() != null) {
            dto.setParentComment(ItemCommentsDto.of(itemComments.getCommentParentUuid()));
            dto.setCommentParentUuid(dto.getParentComment().getCommentParentUuid());
        }
        dto.setCommentStatus(itemComments.getCommentStatus());

        if ( dto.getCommentStatus() == 0 ) {
            dto.setCommentContents(itemComments.getCommentContents());
        } else {
            dto.setCommentContents("삭제된 댓글입니다.");
        }
        dto.setCommentRegUuid(itemComments.getCommentRegUuid());
        dto.setCommentRegDate(itemComments.getCommentRegDate());
        dto.setCommentUpdaterUuid(itemComments.getCommentUpdaterUuid());
        dto.setCommentUpdateDate(itemComments.getCommentUpdateDate());
        dto.setCommentLikeCnt(itemComments.getCommentLikes().size());
        dto.setCommentChildCnt(itemComments.getChildComments().size());

        dto.setEditYn(dto.getCommentUpdateDate().isAfter(dto.getCommentRegDate()));
        dto.setChildYn(dto.getCommentChildCnt() > 0);

        return dto;
    }
}
