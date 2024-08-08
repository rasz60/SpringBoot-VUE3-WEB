package com.example.rmfr.board.spec;

import com.example.rmfr.board.entity.BoardItems;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
@Component
public class BoardItemsSpecifications {
    public static Specification<BoardItems> withItemStatus(int itemStatus) {
        return (Specification<BoardItems>) ((root, query, builder) ->
                builder.equal(root.get("itemStatus"), itemStatus)
        );
    }
}
