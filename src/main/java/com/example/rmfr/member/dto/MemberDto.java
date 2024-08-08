package com.example.rmfr.member.dto;

import com.example.rmfr.member.entity.Members;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MemberDto {
    private String memUuid;
    private String memId;
    private String memPw;
    private String memEmail;
    private Integer memLevel;
    private LocalDateTime memPwUpdateDate;
    private String memPhone;
    private String zipcode;
    private String memAddr1;
    private String memAddr2;

    public void of(Members member) {
        this.memUuid = member.getMemUuid();
        this.memId = member.getMemId();
        this.memEmail = member.getMemEmail();
        this.memLevel = member.getMemLevel();
        this.memPwUpdateDate = member.getMemPwUpdateDate();
        this.memPhone = member.getMemPhone();
        this.zipcode = member.getZipcode();
        this.memAddr1 = member.getMemAddr1();
        this.memAddr2 = member.getMemAddr2();
    }
}
