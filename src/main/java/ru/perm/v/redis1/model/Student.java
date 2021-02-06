package ru.perm.v.redis1.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@RedisHash("Student")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    @Min(value = 1, message = "Id should not be less than 1")
    private Integer id;
    @Size(min = 3, max = 200, message
            = "Name must be between 3 and 200 characters")
    private String name;

}
