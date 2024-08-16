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

    public static MemberDto of(Members member) {
        MemberDto dto = new MemberDto();

        dto.setMemUuid(member.getMemUuid());
        dto.setMemId(member.getMemId());
        dto.setMemEmail(member.getMemEmail());
        dto.setMemLevel(member.getMemLevel());
        dto.setMemPwUpdateDate(member.getMemPwUpdateDate());
        dto.setMemPhone(member.getMemPhone());
        dto.setZipcode(member.getZipcode());
        dto.setMemAddr1(member.getMemAddr1());
        dto.setMemAddr2(member.getMemAddr2());

        return dto;
    }
}
