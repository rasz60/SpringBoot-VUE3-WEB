package com.example.rmfr.board.entity.item;

import com.example.rmfr.member.entity.Members;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "itemLikes")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class ItemLikes {

    // itemUuid : 좋아요가 눌린 컨텐츠(게시물, 댓글 등)의 고유번호
    @Column(columnDefinition = "VARCHAR(100)")
    private String itemUuid;

    // itemLikeId : 좋아요의 고유번호
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemLikeId;

    // itemLikerUuid : 좋아요를 누른 회원의 UUID
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "itemLikerUuid")
    private Members itemLikerUuid;

    // itemLikeDate : 좋아요를 누른 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime itemLikeDate;

    public ItemLikes() {};
}
