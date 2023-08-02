package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static ru.practicum.shareit.TestUtils.item;
import static ru.practicum.shareit.TestUtils.owner;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationItemTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    public void getItemByIdTest() {
        UserDto savedUser = userService.createUser(owner);
        ItemDto savedItem = itemService.createItem(item, savedUser.getId());
        ItemDtoResponse gotItem = itemService.getItemById(savedUser.getId(), savedItem.getId());
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(savedItem.getName()));
        assertThat(gotItem.getAvailable(), equalTo(savedItem.getAvailable()));
        userService.deleteUserById(savedUser.getId());
    }
}