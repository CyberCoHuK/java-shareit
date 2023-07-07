package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@Entity
@Table(name = "users")
public class User {
    @PositiveOrZero(message = "ID не может быть меньше ноля")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Имя не должен быть пустым")
    private String name;
    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректная почта")
    private String email;
}
