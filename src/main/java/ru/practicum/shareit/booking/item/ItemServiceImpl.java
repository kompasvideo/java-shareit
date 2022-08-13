package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.ItemFoundDto;
import ru.practicum.shareit.booking.item.model.Comment;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        validateWhenSaveItem(itemDto, userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        itemRepository.save(item);
        log.info("Вещь id = {} успешно сохранена у пользователя id = {}", item.getId(), userId);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        validateWhenUpdateItem(userId, itemId);
        Item item = itemRepository.findById(itemId).get();
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        itemRepository.save(item);
        log.info("Вещь id = {} успешно обновлена у пользователя id = {}", itemId, userId);
        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemFoundDto getItem(long userId, long itemId) {
        checkUserById(userId);
        checkItemById(itemId);
        Item foundItem = itemRepository.findById(itemId).get();
        log.info("Вещь id = {} успешно найдена", itemId);
        List<CommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        //Перебор всех комментариев по предмету и преобразование их в DTO класс
        comments.forEach(comment -> commentsDto.add(CommentMapper.toCommentDto(
            comment,
            userRepository.findById(comment.getUserId()).get().getName())
        ));
        // Если пользователь является владельцем предмета
        if (foundItem.getOwnerId().equals(userId)) {
            return getItemFoundDto(foundItem, userId, commentsDto);
        }
        return ItemMapper.toItemFoundDto(foundItem,
            null,
            null,
            commentsDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemFoundDto> getAllItem(long userId) {
        checkUserById(userId);
        log.info("Все вещи успешно найдены у пользователя id = {}", userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        List<ItemFoundDto> itemsDto = new ArrayList<>();

        items.forEach(item -> itemsDto.add(getItemFoundDto(item, userId)));

        return itemsDto;
    }

    @Transactional()
    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        validateWhenAddComment(userId, itemId, comment);
        comment.setItemId(itemId);
        comment.setUserId(userId);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment, userRepository.findById(userId).get().getName());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItem(long userId, String text) {
        checkUserById(userId);
        if (text.isBlank())
            return new ArrayList<>();
        log.info("Все вещи успешно найдены по text = {} для пользователя id = {}", text, userId);
        return ItemMapper.toItemsDto(itemRepository.findItemsByText(text));
    }

    private ItemFoundDto getItemFoundDto(Item foundItem, Long userId) {
        List<Booking> bookings = bookingRepository.findTwoBookingByOwnerIdOrderByEndAsc(userId, foundItem.getId());
        if (bookings.size() >= 2) {
            return ItemMapper.toItemFoundDto(foundItem,
                new ItemFoundDto.LastBooking(bookings.get(0)),
                new ItemFoundDto.NextBooking(bookings.get(1)));
        } else if (bookings.size() == 1) {
            if (bookings.get(0).getStart().isBefore(LocalDateTime.now())) {
                return ItemMapper.toItemFoundDto(foundItem,
                    new ItemFoundDto.LastBooking(bookings.get(0)),
                    null);
            } else {
                return ItemMapper.toItemFoundDto(foundItem,
                    null,
                    new ItemFoundDto.NextBooking(bookings.get(0)));
            }
        } else {
            return ItemMapper.toItemFoundDto(foundItem,
                null,
                null);
        }
    }

    private ItemFoundDto getItemFoundDto(Item foundItem, Long userId, List<CommentDto> commentsDto) {
        List<Booking> bookings = bookingRepository.findTwoBookingByOwnerIdOrderByEndAsc(userId, foundItem.getId());
        if (bookings.size() == 2) {
            return ItemMapper.toItemFoundDto(foundItem,
                new ItemFoundDto.LastBooking(bookings.get(0)),
                new ItemFoundDto.NextBooking(bookings.get(1)),
                commentsDto);
        } else if (bookings.size() == 1) {
            if (bookings.get(0).getStart().isBefore(LocalDateTime.now())) {
                return ItemMapper.toItemFoundDto(foundItem,
                    new ItemFoundDto.LastBooking(bookings.get(0)),
                    null,
                    commentsDto);
                // если аренда является следующей
            } else {
                return ItemMapper.toItemFoundDto(foundItem,
                    null,
                    new ItemFoundDto.NextBooking(bookings.get(0)),
                    commentsDto);
            }
        } else {
            return ItemMapper.toItemFoundDto(foundItem,
                null,
                null,
                commentsDto);
        }
    }

    private void validateWhenSaveItem(ItemDto itemDto, Long userId) {
        checkUserById(userId);
        if (itemDto.getAvailable() == null) {
            log.warn("У вещи нету статуса аренды");
            throw new BadRequestException("У вещи нету статуса аренды");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.warn("У вещи нету названия");
            throw new BadRequestException("У вещи нету названия");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.warn("У вещи нету описания");
            throw new BadRequestException("У вещи нету описания");
        }
    }

    // Метод для проверок при создании комментария
    private void validateWhenAddComment(Long userId, Long itemId, Comment comment) {
        checkUserById(userId);
        checkItemById(itemId);
        if (comment.getText().isBlank()) {
            log.warn("Комментарий не может быть пустым");
            throw new BadRequestException("Комментарий не может быть пустым");
        }
        if (bookingRepository.findByBookerIdAndItemId(userId, itemId) == null) {
            log.warn("Пользователь id = {} не арендовывал предмет id = {}", userId, itemId);
            throw new BadRequestException("Пользователь id = " + userId + " не арендовывал предмет id = " + itemId);
        }
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemId(userId, itemId);
        //Аренда по которой можно будет сделать отзыв
        Booking checkBooking = null;
        for (Booking booking : bookings) {
            if (booking.getStatus().equals(Status.APPROVED) && booking.getEnd().isBefore(LocalDateTime.now())) {
                checkBooking = booking;
                break;
            }
        }
        if (checkBooking == null) {
            log.warn("Бронирование не подтверждено или не закончился срок аренды ");
            throw new BadRequestException("Бронирование не подтверждено или не закончился срок аренды ");
        }
    }

    private void validateWhenUpdateItem(Long userId, Long itemId) {
        checkUserById(userId);
        if (itemRepository.findItemsByOwnerId(userId) == null) {
            log.warn("У пользователя id = {} нету вещей для аренды", userId);
            throw new NotFoundException("У пользователя id = " + userId + " нету вещей для аренды");
        }
        if (itemRepository.findItemsByOwnerId(userId).stream().noneMatch(item -> item.getId().equals(itemId))) {
            log.warn("У пользователя id = {} нету прав на вещь id = {}", userId, itemId);
            throw new ForbiddenException("У пользователя id = " + userId + " нету прав на вещь id = " + itemId);
        }
    }

    private void checkUserById(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private void checkItemById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            log.warn("Вещь id = {} не найдена", itemId);
            throw new NotFoundException("Вещь id = " + itemId + " не найдена");
        }
    }
}
