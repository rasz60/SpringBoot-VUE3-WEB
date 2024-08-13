package com.example.rmfr.board.service.impl;

import com.example.rmfr.board.entity.item.ItemLikes;
import com.example.rmfr.board.repository.ItemLikesRepository;
import com.example.rmfr.board.service.ItemLikesService;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.result.RestResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemLikesServiceImpl implements ItemLikesService {

    private final ItemLikesRepository itemLikesRepository;

    @Override
    public RestResults addLike(String itemUuid, MemberDto memberDto) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            Members member = new Members(memberDto);

            Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(itemUuid, member);

            if ( likes == 0 ) {
                ItemLikes like = new ItemLikes();
                like.setItemUuid(itemUuid);
                like.setItemLikerUuid(member);
                itemLikesRepository.save(like);
                rst.setResultCode("200");
            }
            msg.put("itemLikes", itemLikesRepository.countByItemUuid(itemUuid));
            rst.getResultMessage().add(msg);

        } catch(Exception e) {
            log.error(e.getMessage());

            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    @Override
    public RestResults delLike(String itemUuid, MemberDto member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            itemLikesRepository.findByItemUuidAndItemLikerUuid(itemUuid, new Members(member)).ifPresent(itemLikesRepository::delete);

            msg.put("itemLikes", itemLikesRepository.countByItemUuid(itemUuid));

            rst.setResultCode("200");
            rst.getResultMessage().add(msg);

        } catch(Exception e) {
            log.error(e.getMessage());

            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }
}
