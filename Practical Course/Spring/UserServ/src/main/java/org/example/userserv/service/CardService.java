package org.example.userserv.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.userserv.dto.CardRequest;
import org.example.userserv.dto.CardResponse;
import org.example.userserv.entity.Card;
import org.example.userserv.entity.User;
import org.example.userserv.exception.CardNotFoundException;
import org.example.userserv.exception.UserNotFoundException;
import org.example.userserv.exception.ValueAlreadyExistsException;
import org.example.userserv.mapper.CardMapper;
import org.example.userserv.repository.CardRepository;
import org.example.userserv.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;

    public CardResponse create(CardRequest cardRequest) {
        if (cardRepository.existsByNumber(cardRequest.getNumber()))
            throw new ValueAlreadyExistsException("number", cardRequest.getNumber());

        User user = userRepository.findById(cardRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Card card = cardMapper.toEntity(cardRequest);
        card.setUser(user);

        return cardMapper.toResponse(cardRepository.save(card));
    }

    public CardResponse findById(Long id) {
        return cardRepository.findById(id).map(cardMapper::toResponse)
                .orElseThrow(CardNotFoundException::new);
    }

    public List<CardResponse> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        return cardRepository.findByIds(ids).stream().map(cardMapper::toResponse).toList();
    }

    @Transactional
    public CardResponse update(Long id, CardRequest cardRequest) {
        Card card = cardRepository.findById(id).orElseThrow(CardNotFoundException::new);

        if (cardRequest.getHolder() != null) card.setHolder(cardRequest.getHolder());
        if (cardRequest.getExpirationDate() != null) card.setExpirationDate(cardRequest.getExpirationDate());
        if (cardRequest.getNumber() != null) {
            if (cardRepository.existsByNumber(cardRequest.getNumber())
                    && !cardRequest.getNumber().equals(card.getNumber()))
                throw new ValueAlreadyExistsException("number", cardRequest.getNumber());
            card.setNumber(cardRequest.getNumber());
        }

        return cardMapper.toResponse(cardRepository.save(card));
    }

    @Transactional
    public void delete(Long id) {
        if (!cardRepository.existsById(id)) throw new CardNotFoundException();

        cardRepository.deleteById(id);
    }
}
