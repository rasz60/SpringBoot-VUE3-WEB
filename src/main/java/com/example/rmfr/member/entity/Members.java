package com.example.rmfr.member.entity;

import com.example.rmfr.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Data
@DynamicInsert
@DynamicUpdate
public class Members {
    // MEMBERS Entity 테이블
    @Id
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @GeneratedValue(generator="uuid2")
    @Column(columnDefinition = "VARCHAR(100)")
    private String memUuid;

    @Column(columnDefinition = "VARCHAR(40)")
    private String memId;

    @Column(columnDefinition = "VARCHAR(100)", nullable=false)
    private String memPw;

    @Column(columnDefinition = "VARCHAR(200)", nullable=false)
    private String memEmail;

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer memLevel;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime memPwUpdateDate;

    @Column(columnDefinition = "VARCHAR(11)")
    private String memPhone;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String zipcode;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String memAddr1;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String memAddr2;

    @Builder
    public Members(MemberDto memberDto) {
        this.memId = memberDto.getMemId();
        this.memPw = memberDto.getMemPw();
        this.memEmail = memberDto.getMemEmail();
        this.memLevel = memberDto.getMemLevel();
        this.memPwUpdateDate = memberDto.getMemPwUpdateDate();
        this.memPhone = memberDto.getMemPhone();
        this.zipcode = memberDto.getZipcode();
        this.memAddr1 = memberDto.getMemAddr1();
        this.memAddr2 = memberDto.getMemAddr2();
    }
}