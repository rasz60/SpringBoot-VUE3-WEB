package com.example.rmfr.board.entity.item;

import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.member.entity.Members;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "itemHits")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class ItemHits {

    // itemUuid : 조회한 게시물의 고유번호
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="itemUuid", name = "itemUuid")
    private BoardItems itemUuid;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemHitId;

    // itemHitMemUuid : 조회한 회원의 UUID
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "itemHitMemUuid")
    private Members itemHitMemUuid;

    // itemHitDate : 조회한 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime itemHitDate;

    public ItemHits() {};
}
