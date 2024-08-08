package com.example.rmfr.board.controller;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardRestController {

    private final BoardItemsService boardItemsService;
    private final MemberService memberService;
    @GetMapping("/rest/itemHeaders")
    public List<ItemHeaders> itemHeaders(Principal principal) {
        int auth = 1;
        if ( principal != null )
            auth = memberService.getUserInfo(principal.getName()).getMemLevel();

        return boardItemsService.getItemHeaders(auth);
    }

    @PostMapping("/rest/board/reg")
    @ResponseBody
    public Map<String, String> regItem(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        Map<String, String> rst = new HashMap<>();
        try {
            if ( principal != null ) {
                MemberDto member = memberService.getUserInfo(principal.getName());
                rst = boardItemsService.regItem(boardItemsDto, member);
            }
            else {
                rst.put("resultCode", "400");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @GetMapping("/rest/boardList/{itemStatus}/{page}/{limit}")
    public Page<BoardItemsDto> getBoardList(@PathVariable(value = "itemStatus") int itemStatus,
                                            @PathVariable(value = "page") int page,
                                            @PathVariable("limit") int limit,
                                            Principal principal) {
        Page<BoardItemsDto> dtos = null;

        try {
            dtos = boardItemsService.getBoardList(itemStatus, page, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dtos;
    }

    @GetMapping("/rest/item/{seq}")
    public BoardItemsDto getBoardItem(@PathVariable(value = "seq") int seq) {
        BoardItemsDto dto = new BoardItemsDto();
        try {
            dto = boardItemsService.getBoardItem(seq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dto;
    }
}
