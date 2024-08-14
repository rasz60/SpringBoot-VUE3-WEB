package com.example.rmfr.board.entity;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.entity.item.ItemHits;
import com.example.rmfr.board.entity.item.ItemLikes;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Entity
@Table(name = "boardItems")
@Data
@DynamicInsert
@DynamicUpdate
public class BoardItems {

    // itemUuid : 게시물 고유번호
    @Id
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @GeneratedValue(generator="uuid2")
    @Column(columnDefinition = "VARCHAR(100)")
    private String itemUuid;

    // itemSeq : 게시물 순번
    @Column(columnDefinition = "INT")
    private int itemSeq;

    // itemHeader : 게시물 말머리
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="itemHeaderId", name = "itemHeader")
    private ItemHeaders itemHeader;

    // itemTitle : 게시물 제목
    @Column(columnDefinition = "VARCHAR(200)", nullable=false)
    private String itemTitle;

    // itemContents : 게시물 내용, MySQL LONGTEXT Type
    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable=false)
    private String itemContents;

    // itemKeywords : 게시물 키워드, #을 구분자로하는 문자열
    @Column(columnDefinition = "VARCHAR(1000)")
    private String itemKeywords;

    // itemStatus : 게시물 상태 - 0: 임시저장, 1: 등록, 2: 삭제
    @Column(columnDefinition = "INT")
    private int itemStatus;

    // itemRegUuid : 게시물 등록자의 memUuid
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "itemRegUuid")
    private Members itemRegUuid;

    // itemRegDate : 게시물 등록일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime itemRegDate;

    // itemUpdaterUuid : 게시물 수정자의 memUuid
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "itemUpdaterUuid")
    private Members itemUpdaterUuid;

    // itemUpdateDate : 게시물 수정 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime itemUpdateDate;

    @OneToMany(mappedBy = "itemUuid", fetch = FetchType.EAGER)
    List<ItemHits> itemHits = new ArrayList<>();

    @OneToMany(mappedBy = "itemUuid", fetch = FetchType.EAGER)
    List<ItemLikes> itemLikes = new ArrayList<>();

    @OneToMany(mappedBy = "itemUuid", fetch = FetchType.EAGER)
    List<ItemComments> itemComments = new ArrayList<>();

    public BoardItems() {}

    public BoardItems(BoardItemsDto boardItemsDto) {
        this.itemSeq = boardItemsDto.getItemSeq();
        this.itemHeader = boardItemsDto.getItemHeader();
        this.itemTitle = boardItemsDto.getItemTitle();
        this.itemContents = boardItemsDto.getItemContents();
        this.itemKeywords = boardItemsDto.getItemKeywords();
        this.itemStatus = boardItemsDto.getItemStatus();
        this.itemRegUuid = boardItemsDto.getItemRegUuid();
        this.itemRegDate = boardItemsDto.getItemRegDate();
        this.itemUpdaterUuid = boardItemsDto.getItemUpdaterUuid();
        this.itemUpdateDate = boardItemsDto.getItemUpdateDate();
    }

    public static BoardItems of(BoardItemsDto boardItemsDto) {
        BoardItems boardItems = new BoardItems();

        boardItems.setItemUuid(boardItems.getItemUuid());
        boardItems.setItemSeq(boardItems.getItemSeq());
        boardItems.setItemHeader(boardItemsDto.getItemHeader());
        boardItems.setItemTitle(boardItemsDto.getItemTitle());
        boardItems.setItemContents(boardItemsDto.getItemContents());
        boardItems.setItemKeywords(boardItemsDto.getItemKeywords());
        boardItems.setItemStatus(boardItemsDto.getItemStatus());
        boardItems.setItemRegUuid(boardItemsDto.getItemRegUuid());
        boardItems.setItemRegDate(boardItemsDto.getItemRegDate());
        boardItems.setItemUpdaterUuid(boardItemsDto.getItemUpdaterUuid());
        boardItems.setItemUpdateDate(boardItemsDto.getItemUpdateDate());

        return boardItems;
    }

    public void updateItems(BoardItemsDto boardItemsDto) {
        this.setItemHeader(boardItemsDto.getItemHeader());
        this.setItemTitle(boardItemsDto.getItemTitle());
        this.setItemContents(boardItemsDto.getItemContents());
        this.setItemKeywords(boardItemsDto.getItemKeywords());
        this.setItemStatus(boardItemsDto.getItemStatus());
        this.setItemUpdaterUuid(boardItemsDto.getItemUpdaterUuid());
        this.setItemUpdateDate(boardItemsDto.getItemUpdateDate());
    }
}