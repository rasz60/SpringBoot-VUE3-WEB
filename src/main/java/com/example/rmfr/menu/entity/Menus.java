package com.example.rmfr.menu.entity;

import com.example.rmfr.member.entity.Members;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "menus")
@Data
@DynamicInsert
@DynamicUpdate
public class Menus {

    // menuUuid : 고유번호
    @Id
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @GeneratedValue(generator="uuid2")
    @Column(columnDefinition = "VARCHAR(100)")
    private String menuUuid;

    // menuName : 이름
    @Column(columnDefinition = "VARCHAR(50)")
    private String menuName;

    // menuType : 유형, 0 - title, 1 - subMenus
    @Column(columnDefinition = "INT")
    private int menuType;

    // menuSection : 화면 섹션, 1~3
    @Column(columnDefinition = "INT")
    private int menuSection;

    // menuSeq : 순서 번호
    @Column(columnDefinition = "INT")
    private int menuSeq;

    // menuLink : 링크 URL
    @Column(columnDefinition = "VARCHAR(1000)")
    private String menuLink;

    // menuStatus : 사용 여부, 0 - 사용 / 1 - 미사용
    @Column(columnDefinition = "INT DEFAULT 0")
    private int menuStatus;

    // menuAuth : 권한, 0 - 전체 공개, 1 - 회원 공개, 2 - 비공개
    @Column(columnDefinition = "INT DEFAULT 1")
    private int menuAuth;

    // menuRegUuid : 등록 회원 고유번호
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "menuRegUuid")
    private Members menuRegUuid;

    // menuRegDate : 등록 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime menuRegDate;

    // menuUpdaterUuid : 수정 회원 고유번호
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="memUuid", name = "menuUpdaterUuid")
    private Members menuUpdaterUuid;

    // menuUpdateDate : 수정 일자
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime menuUpdateDate;

    public Menus() {}
}
