package com.example.rmfr.board.entity.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long itemHeaderId;

    // itemHeaderName : 말머리 이름
    @Column(columnDefinition = "VARCHAR(20)")
    private String itemHeaderName;

}
