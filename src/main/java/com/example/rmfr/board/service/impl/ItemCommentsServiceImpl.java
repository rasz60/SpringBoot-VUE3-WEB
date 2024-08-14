package com.example.rmfr.board.service.impl;

import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.board.repository.ItemCommentsRepository;
import com.example.rmfr.board.repository.ItemLikesRepository;
import com.example.rmfr.board.service.ItemCommentsService;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.result.RestResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemCommentsServiceImpl implements ItemCommentsService {

    private final ItemCommentsRepository itemCommentsRepository;
    private final ItemLikesRepository itemLikesRepository;

    @Override
    public List<ItemCommentsDto> getReplies(String itemUuid, String commentParentUuid, Members member) {
        List<ItemCommentsDto> cmmDtos = new ArrayList<>();
        try {
            List<ItemComments> cmms = new ArrayList<>();

            if (! "0".equals(commentParentUuid) ) {
                Optional<ItemComments> pCmm = itemCommentsRepository.findById(commentParentUuid);
                if(pCmm.isPresent()) {
                    ItemComments parentComment = pCmm.get();
                    int depth = parentComment.getCommentDepth() + 1;
                    cmms = itemCommentsRepository.findByItemUuidAndCommentParentUuidAndCommentDepth(itemUuid, parentComment, depth);
                }
            }

            else cmms = itemCommentsRepository.findByItemUuidAndCommentDepth(itemUuid, 0);


            for (ItemComments cmm : cmms) {
                ItemCommentsDto dto = ItemCommentsDto.of(cmm);
                if ( member != null ) {
                    // 로그인한 아이디로 좋아요를 누른 댓글 인지
                    Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(cmm.getCommentUuid(), member);
                    boolean likeCmm = likes > 0;
                    dto.setLikeComment(likeCmm);

                    // 로그인한 아이디로 작성된 댓글 인지? 수정/삭제 권한
                    boolean regChk = authChk(member, dto.getCommentRegUuid().getMemUuid());
                    dto.setEAuth(regChk);
                    dto.setDAuth(regChk);
                }
                cmmDtos.add(dto);
            }


        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return cmmDtos;
    }

    @Override
    public RestResults addReply(ItemCommentsDto itemCommentsDto, Members member) {
        RestResults rst = new RestResults();

        try {
            itemCommentsDto.setCommentStatus(0);
            itemCommentsDto.setCommentRegUuid(member);
            itemCommentsDto.setCommentUpdaterUuid(member);

            ItemComments cmm = ItemComments.of(itemCommentsDto);

            // parentComment 가 있을 때, set 해주기
            if ( !"".equals(itemCommentsDto.getCommentParentUuid()) ) {
                String pUuid = itemCommentsDto.getCommentParentUuid();
                Optional<ItemComments> pCmm = itemCommentsRepository.findById(pUuid);
                pCmm.ifPresent(cmm::setCommentParentUuid);
            }

            itemCommentsRepository.save(cmm);

            rst.setResultCode(200);
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    @Override
    public RestResults delComment(String commentUuid, Members member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            Optional<ItemComments> comm = itemCommentsRepository.findById(commentUuid);
            if ( comm.isPresent() ) {
                ItemComments comment = comm.get();
                if ( authChk(member, comment.getCommentRegUuid().getMemUuid()) ) {
                    comment.setCommentStatus(1);
                    comment.setCommentUpdaterUuid(member);
                    comment.setCommentUpdateDate(LocalDateTime.now());
                    itemCommentsRepository.save(comment);
                    rst.setResultCode(200);
                } else {
                    rst.setResultCode(400);
                    rst.setResultMessage("댓글 삭제 권한", 0);
                }
            } else {
                rst.setResultCode(400);
                rst.setResultMessage("삭제할 댓글", 0);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    public boolean authChk(Members member, String memUuid) {
        return member.getMemUuid().equals(memUuid) || member.getMemLevel() > 1;
    }
}
