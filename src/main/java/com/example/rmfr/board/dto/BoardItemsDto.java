package com.example.rmfr.board.dto;

import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.member.entity.Members;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardItemsDto {

    private String itemUuid;
    private int itemSeq;
    private String itemHeaderId;
    private ItemHeaders itemHeader;
    private String itemHeaderName;
    private String itemTitle;
    private String itemContents;
    private String itemKeywords;
    private int itemStatus;
    private Members itemRegUuid;
    private LocalDateTime itemRegDate;
    private Members itemUpdaterUuid;
    private LocalDateTime itemUpdateDate;
    private int itemHitsCnt;
    private int itemLikesCnt;
    private int itemCommentsCnt;

    private boolean eAuth;
    private boolean dAuth;
    private boolean cAuth;

    private boolean likeItem;

    public BoardItemsDto() {}

    public BoardItemsDto(BoardItems boardItems) {
        this.itemUuid = boardItems.getItemUuid();
        this.itemSeq = boardItems.getItemSeq();
        this.itemHeader = boardItems.getItemHeader();
        if ( this.itemHeader != null ) {
            this.itemHeaderName = boardItems.getItemHeader().getItemHeaderName();
        }
        this.itemTitle = boardItems.getItemTitle();
        this.itemContents = boardItems.getItemContents();
        this.itemKeywords = boardItems.getItemKeywords();
        this.itemStatus = boardItems.getItemStatus();
        this.itemRegUuid = boardItems.getItemRegUuid();
        this.itemRegDate = boardItems.getItemRegDate();
        this.itemUpdaterUuid = boardItems.getItemUpdaterUuid();
        this.itemUpdateDate = boardItems.getItemUpdateDate();
        this.itemHitsCnt = boardItems.getItemHits().size();
        this.itemLikesCnt = boardItems.getItemLikes().size();
        this.itemCommentsCnt = boardItems.getItemComments().size();
    }

}
