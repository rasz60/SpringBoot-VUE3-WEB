package com.example.rmfr.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Data
@DynamicInsert
@DynamicUpdate
public class Members {
    // MEMBERS Entity 테이블
    @Id
    @Column(columnDefinition = "VARCHAR(40)")
    private String mId;

    @Column(columnDefinition = "VARCHAR(100)", nullable=false)
    private String mPw;

    @Column(columnDefinition = "VARCHAR(200)", nullable=false)
    private String mEmail;

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer mLevel;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime mPwUpdateDate;

    @Column(columnDefinition = "VARCHAR(11)")
    private String mPhone;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String zipcode;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String mAddr1;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String mAddr2;
}