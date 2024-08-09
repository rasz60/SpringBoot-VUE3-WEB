package com.example.rmfr.board.entity.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "ItemHeaders")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class ItemHeaders {

    // itemHeaderId : 말머리 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemHeaderId;

    // itemHeaderName : 말머리 이름
    @Column(columnDefinition = "VARCHAR(20)")
    private String itemHeaderName;

    // itemHeaderAuth : 말머리 권한, 1 - 일반, 2 - 관리자
    @Column(columnDefinition = "INT DEFAULT 1")
    private int itemHeaderAuth;

    // itemHeaderSortOrder : 정렬기준
    @Column(columnDefinition = "INT")
    private int itemHeaderSortOrder;

}
