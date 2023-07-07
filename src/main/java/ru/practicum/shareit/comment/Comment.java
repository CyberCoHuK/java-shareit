package ru.practicum.shareit.comment;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @PositiveOrZero
    @EqualsAndHashCode.Include
    private long id;
    private String text;
    @Column(name = "item_id")
    private long item;
    @Column(name = "author_id")
    private long author;
}
