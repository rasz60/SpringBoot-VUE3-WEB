package com.example.rmfr.board.service.impl;

import com.example.rmfr.board.entity.item.ItemLikes;
import com.example.rmfr.board.repository.ItemLikesRepository;
import com.example.rmfr.board.service.ItemLikesService;
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
    public int likeCount(String itemUuid) {
        return itemLikesRepository.countByItemUuid(itemUuid).intValue();
    }

    @Override
    public RestResults addLike(String itemUuid, Members member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(itemUuid, member);

            if ( likes == 0 ) {
                ItemLikes like = new ItemLikes();
                like.setItemUuid(itemUuid);
                like.setItemLikerUuid(member);
                itemLikesRepository.save(like);
                rst.setResultCode(200);
            }

            rst.setResult("itemLikes", likeCount(itemUuid));

        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    @Override
    public RestResults delLike(String itemUuid, Members member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            itemLikesRepository.findByItemUuidAndItemLikerUuid(itemUuid, member).ifPresent(itemLikesRepository::delete);
            rst.setResultCode(200);
            rst.setResult("itemLikes", likeCount(itemUuid));
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }
}
