package org.example.userserv.mapper;

import org.example.userserv.dto.CardRequest;
import org.example.userserv.dto.CardResponse;
import org.example.userserv.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    Card toEntity(CardRequest cardRequest);
    CardResponse toResponse(Card card);
}
