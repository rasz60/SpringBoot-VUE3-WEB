package com.example.rmfr.board.service;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.result.RestResults;
import org.springframework.stereotype.Service;

@Service
public interface ItemLikesService {

    public RestResults addLike(String itemUuid, MemberDto memberDto);

    public RestResults delLike(String itemUuid, MemberDto member);
}
