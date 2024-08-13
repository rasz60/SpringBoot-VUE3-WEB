package com.example.rmfr.board.service;

import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.result.RestResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemCommentsService {

    public List<ItemCommentsDto> getReplies(String itemUuid, String commentParentUuid, MemberDto member);
    public RestResults addReply(ItemCommentsDto itemCommentsDto, MemberDto member);
    public RestResults delComment(String commentUuid, MemberDto member);
}
