package com.example.ChatBot.specification;

import com.example.ChatBot.dto.request.RoomSearchRequest;
import com.example.ChatBot.entity.Roomate;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {
    public static Specification<Roomate> filter(RoomSearchRequest homefilter) {
        Specification<Roomate> spec = Specification.allOf();

        if (homefilter.getAddress() != null && !homefilter.getAddress().isBlank()) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.like(
                                    root.get("address"),
                                    "%" + homefilter.getAddress() +"%"
                            )
            );
        }
        if (homefilter.getType() != null) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.equal(
                                    root.get("type"),
                                    homefilter.getType()
                            )
            );
        }

        if (homefilter.getMaxprice() != null && homefilter.getMinprice() != null) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.between(
                                    root.get("price"),
                                    homefilter.getMinprice(),
                                    homefilter.getMaxprice()
                            )
            );
        } else if (homefilter.getMinprice() != null) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.ge(
                                    root.get("price"),
                                    homefilter.getMinprice()
                            )
            );
        } else if (homefilter.getMaxprice() != null) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.le(
                                    root.get("price"),
                                    homefilter.getMaxprice()
                            )
            );
        }
        return spec;
    }
}
