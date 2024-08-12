package com.example.rmfr.board.entity.item;

import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.member.entity.Members;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itemComments")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class ItemComments {

    // itemUuid : 댓글이 달린 게시물의 고유번호
    @Column(columnDefinition = "VARCHAR(100)")
    private String itemUuid;

    // commentUuid : 댓글 고유번호
    @Id
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @GeneratedValue(generator="uuid2")
    @Column(columnDefinition = "VARCHAR(100)")
    private String commentUuid;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int commentDepth;

    // commentParentUuid : 모댓글 고유번호
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName="commentUuid", name ="commentParentUuid")
    private ItemComments commentParentUuid;

    // commentContents : 댓글 내용
    @Column(columnDefinition = "VARCHAR(300)")
    private String commentContents;

    // commentStatus : 댓글 상태 - 0: 등록 / 1: 삭제
    @Column(columnDefinition = "INT")
    private int commentStatus;

    // commentRegUuid : 댓글 작성자의 memUuid
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "commentRegUuid")
    private Members commentRegUuid;

    // commentRegDate : 댓글 작성 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime commentRegDate;

    // commentUpdaterUuid : 댓글 수정자의 memUuid
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "commentUpdaterUuid")
    private Members commentUpdaterUuid;

    // commentUpdateDate : 댓글 수정 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime commentUpdateDate;

    @OneToMany(mappedBy = "itemUuid", fetch = FetchType.EAGER)
    List<ItemLikes> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "commentParentUuid", fetch = FetchType.EAGER)
    List<ItemComments> childComments = new ArrayList<>();


    public ItemComments() {}

    public static ItemComments of(ItemCommentsDto itemCommentsDto) {
        ItemComments cmms = new ItemComments();

        cmms.setItemUuid(itemCommentsDto.getItemUuid());
        cmms.setCommentDepth(itemCommentsDto.getCommentDepth());
        cmms.setCommentContents(itemCommentsDto.getCommentContents());
        cmms.setCommentStatus(itemCommentsDto.getCommentStatus());
        cmms.setCommentRegUuid(itemCommentsDto.getCommentRegUuid());
        cmms.setCommentUpdaterUuid(itemCommentsDto.getCommentUpdaterUuid());
        cmms.setCommentRegDate(itemCommentsDto.getCommentRegDate());
        cmms.setCommentUpdateDate(itemCommentsDto.getCommentUpdateDate());

        return cmms;
    }
}
