package com.example.rmfr.board.controller;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.board.service.ItemCommentsService;
import com.example.rmfr.board.service.ItemLikesService;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.service.MemberService;
import com.example.rmfr.result.RestResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardRestController {

    private final BoardItemsService boardItemsService;
    private final ItemLikesService itemLikesService;
    private final ItemCommentsService itemCommentsService;
    private final MemberService memberService;
    @GetMapping("/rest/itemHeaders")
    public RestResults itemHeaders(Principal principal) {
        RestResults rst = new RestResults();
        int auth = 1;
        try {
            if (principal != null) {
                auth = memberService.getUserInfo(principal.getName()).getMemLevel();
                rst.setResultCode(200);
                rst.setResult("itemHeaders", boardItemsService.getItemHeaders(auth));
            } else {
                rst.setResultCode(401);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    /* Items */

    @PostMapping("/rest/board/save")
    @ResponseBody
    public RestResults saveItem(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        RestResults rst = new RestResults();
        try {
            if ( principal != null ) {
                Members member = memberService.findByMemId(principal.getName());

                rst = "".equals(boardItemsDto.getItemUuid()) ?
                        boardItemsService.regItem(boardItemsDto, member) :
                        boardItemsService.updateItem(boardItemsDto, member);
            }
            else {
                rst.setResultCode(401);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    @GetMapping("/rest/boardList/{itemStatus}/{page}/{limit}")
    public RestResults getBoardList(@PathVariable(value = "itemStatus") int itemStatus,
                                    @PathVariable(value = "page") int page,
                                    @PathVariable("limit") int limit) {
        RestResults rst = new RestResults();
        try {
            rst = boardItemsService.getBoardList(itemStatus, page, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    @GetMapping("/rest/item/{seq}")
    public RestResults getBoardItem(@PathVariable(value = "seq") int seq, Principal principal) {
        RestResults rst = new RestResults();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
            }
            rst = boardItemsService.getBoardItem(seq, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    @PutMapping("/rest/delItem")
    @ResponseBody
    public RestResults delItem(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        RestResults rst = new RestResults();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
                rst = boardItemsService.delItem(boardItemsDto, member);
            } else {
                rst.setResultCode(401);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    /* Likes */

    @PostMapping("/rest/item/addLike")
    @ResponseBody
    public RestResults addLike(@RequestBody String itemUuid, Principal principal) {
        RestResults rst = new RestResults();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
                rst = itemLikesService.addLike(itemUuid, member);
            } else {
                rst.setResultCode(401);
            }
        } catch (Exception e) {
            rst.setResultCode(500);
            log.error(e.getMessage());
        }
        return rst;
    }

    @DeleteMapping("/rest/item/delLike")
    @ResponseBody
    public RestResults delLike(@RequestBody String itemUuid, Principal principal) {
        RestResults rst = new RestResults();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
                rst = itemLikesService.delLike(itemUuid, member);
            } else {
                rst.setResultCode(401);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    /* Comments */

    @GetMapping("/rest/getReplies/{itemUuid}/{commentParentUuid}")
    public RestResults getReplies(@PathVariable("itemUuid") String itemUuid,
                                  @PathVariable(value = "commentParentUuid", required = false) String commentParentUuid,
                                  Principal principal) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
            }
            rst.setResultCode(200);
            rst.setResult("comments", itemCommentsService.getReplies(itemUuid, commentParentUuid, member));
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }

    @PostMapping("/rest/addReply")
    @ResponseBody
    public RestResults addReply(@RequestBody ItemCommentsDto itemCommentsDto,Principal principal) {
        RestResults rst = new RestResults();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
                rst = itemCommentsService.addReply(itemCommentsDto, member);
            } else {
                rst.setResultCode(401);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @PutMapping("/rest/delComment")
    @ResponseBody
    public RestResults delComment(@RequestBody String commentUuid, Principal principal) {
        RestResults rst = new RestResults();
        Members member = null;
        try {
            if ( principal != null ) {
                member = memberService.findByMemId(principal.getName());
                rst = itemCommentsService.delComment(commentUuid, member);
            } else {
                rst.setResultCode(401);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }

        return rst;
    }
}
