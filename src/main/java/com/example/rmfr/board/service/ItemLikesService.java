package com.example.rmfr.board.service;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.result.RestResults;
import org.springframework.stereotype.Service;

@Service
public interface ItemLikesService {
    public int likeCount(String itemUuid);
    public RestResults addLike(String itemUuid, Members memberDto);
    public RestResults delLike(String itemUuid, Members member);
}
