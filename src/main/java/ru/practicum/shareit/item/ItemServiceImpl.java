package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final ItemRequestService itemRequestService;

    @Transactional
    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        validateWhenSaveItem(itemDto, userId);
        Item item = modelMapper.map(itemDto, Item.class);
        Optional<User> optionalUser = userRepository.findById(userId);
        item.setOwner(optionalUser.get());
        if (itemDto.getRequestId() != 0) {
            itemRequestService.responsesAddItems(item, itemDto.getRequestId());
            ItemRequest itemRequest = itemRequestService.findById(itemDto.getRequestId());
            item.setRequest(itemRequest);
        }
        Item itemS = itemRepository.save(item);
        itemDto.setId(itemS.getId());
        return itemDto;
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        validateWhenUpdateItem(userId, itemId);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.get();
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public OwnerItemDto getItem(long userId, long itemId) {
        checkUserById(userId);
        checkItemById(itemId);
        Optional<Item> optionalFoundItem = itemRepository.findById(itemId);
        Item foundItem = optionalFoundItem.get();
        List<CommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comment.getId());
            commentDto.setText(comment.getText());
            Optional<User> optionalUser = userRepository.findById(comment.getUser().getId());
            commentDto.setAuthorName(optionalUser.get().getName());
            commentDto.setCreated(comment.getCreated());
            commentsDto.add(commentDto);
        }
        if (foundItem.getOwner().getId().equals(userId)) {
            return getItemFoundDto(foundItem, userId, commentsDto);
        }
        return ItemMapper.toItemFoundDto(foundItem, null, null, commentsDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OwnerItemDto> getAllItem(long userId) {
        checkUserById(userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        List<OwnerItemDto> itemsDto = new ArrayList<>();

        items.forEach(item -> itemsDto.add(getItemFoundDto(item, userId)));

        return itemsDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItem(long userId, String text) {
        checkUserById(userId);
        if (text.isBlank())
            return new ArrayList<>();

        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemRepository.findItemsByText(text)) {
            itemsDto.add(ItemMapper.toItemDto(item));
        }
        return itemsDto;
    }

    @Transactional()
    @Override
    public CommentDto addComment(long userId, long itemId, Comment comment) {
        validateWhenAddComment(userId, itemId, comment);
        comment.setItemId(itemId);
        comment.setUser(userRepository.findById(userId).get());
        commentRepository.save(comment);
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(userRepository.findById(userId).get().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }


    private OwnerItemDto getItemFoundDto(Item foundItem, Long userId) {
        List<Booking> bookings = bookingRepository.findTwoBookingByOwnerIdOrderByEndAsc(userId, foundItem.getId());
        if (bookings.size() >= 2) {
            return ItemMapper.toItemFoundDto(foundItem,
                new OwnerItemDto.LastBooking(bookings.get(0)),
                new OwnerItemDto.NextBooking(bookings.get(1)));
        } else if (bookings.size() == 1) {
            if (bookings.get(0).getStart().isBefore(LocalDateTime.now())) {
                return ItemMapper.toItemFoundDto(foundItem,
                    new OwnerItemDto.LastBooking(bookings.get(0)),
                    null);
            } else {
                return ItemMapper.toItemFoundDto(foundItem,
                    null,
                    new OwnerItemDto.NextBooking(bookings.get(0)));
            }
        } else {
            return ItemMapper.toItemFoundDto(foundItem,
                null,
                null);
        }
    }

    private OwnerItemDto getItemFoundDto(Item foundItem, Long userId, List<CommentDto> commentsDto) {
        List<Booking> bookings = bookingRepository.findTwoBookingByOwnerIdOrderByEndAsc(userId, foundItem.getId());
        if (bookings.size() == 2) {
            return ItemMapper.toItemFoundDto(foundItem,
                new OwnerItemDto.LastBooking(bookings.get(0)),
                new OwnerItemDto.NextBooking(bookings.get(1)),
                commentsDto);
        } else if (bookings.size() == 1) {
            if (bookings.get(0).getStart().isBefore(LocalDateTime.now())) {
                return ItemMapper.toItemFoundDto(foundItem,
                    new OwnerItemDto.LastBooking(bookings.get(0)),
                    null,
                    commentsDto);
                // если аренда является следующей
            } else {
                return ItemMapper.toItemFoundDto(foundItem,
                    null,
                    new OwnerItemDto.NextBooking(bookings.get(0)),
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
            throw new BadRequestException();
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new BadRequestException();
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new BadRequestException();
        }
    }

    private void validateWhenAddComment(Long userId, Long itemId, Comment comment) {
        checkUserById(userId);
        checkItemById(itemId);
        if (comment.getText().isBlank()) {
            throw new BadRequestException();
        }
        if (bookingRepository.findByBookerIdAndItemId(userId, itemId) == null) {
            throw new BadRequestException();
        }
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemId(userId, itemId);
        Booking checkBooking = null;
        for (Booking booking : bookings) {
            if (booking.getStatus().equals(Status.APPROVED) && booking.getEnd().isBefore(LocalDateTime.now())) {
                checkBooking = booking;
                break;
            }
        }
        if (checkBooking == null) {
            throw new BadRequestException();
        }
    }

    private void validateWhenUpdateItem(Long userId, Long itemId) {
        checkUserById(userId);
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
        if (items == null) {
            throw new NotFoundException("У пользователя id = " + userId + " нету вещей для аренды");
        }
        if (items.stream().noneMatch(item -> item.getId().equals(itemId))) {
            throw new ForbiddenException("У пользователя id = " + userId + " нету прав на вещь id = " + itemId);
        }
    }

    private void checkUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private void checkItemById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("Вещь id = " + itemId + " не найдена");
        }
    }
}
