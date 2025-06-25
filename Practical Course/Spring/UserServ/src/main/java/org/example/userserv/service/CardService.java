package org.example.userserv.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.userserv.dto.CardRequest;
import org.example.userserv.dto.CardResponse;
import org.example.userserv.entity.Card;
import org.example.userserv.entity.User;
import org.example.userserv.mapper.CardMapper;
import org.example.userserv.repository.CardRepository;
import org.example.userserv.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private static final String EXCEPTION_CARD_NOT_FOUND = "Card not found";
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;

    public CardResponse create(CardRequest cardRequest) {
        if (cardRepository.existsByNumber(cardRequest.getNumber()))
            throw new IllegalArgumentException("Card number already exists");

        User user = userRepository.findById(cardRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Card card = cardMapper.toEntity(cardRequest);
        card.setUser(user);

        return cardMapper.toResponse(cardRepository.save(card));
    }

    public CardResponse findById(Long id) {
        return cardRepository.findById(id).map(cardMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(EXCEPTION_CARD_NOT_FOUND));
    }

    public List<CardResponse> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        return cardRepository.findByIds(ids).stream().map(cardMapper::toResponse).toList();
    }

    @Transactional
    public CardResponse update(Long id, CardRequest cardRequest) {
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(EXCEPTION_CARD_NOT_FOUND));

        if (cardRequest.getHolder() != null) card.setHolder(cardRequest.getHolder());
        if (cardRequest.getExpirationDate() != null) card.setExpirationDate(cardRequest.getExpirationDate());
        if (cardRequest.getNumber() != null) {
            if (cardRepository.existsByNumber(cardRequest.getNumber())
                    && !cardRequest.getNumber().equals(card.getNumber()))
                throw new IllegalArgumentException("Card number already exists");
            card.setNumber(cardRequest.getNumber());
        }

        return cardMapper.toResponse(cardRepository.save(card));
    }

    @Transactional
    public void delete(Long id) {
        if (!cardRepository.existsById(id)) throw new EntityNotFoundException(EXCEPTION_CARD_NOT_FOUND);

        cardRepository.deleteById(id);
    }
}
