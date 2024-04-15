package com.xiaodao.dao.jpa.entity;

import com.xiaodao.dao.jpa.annotation.SignField;
import com.xiaodao.dao.jpa.entity.convert.GenderConverter;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description
 * @Author
 * @Date 2023-05-22
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "t_user")
// @ExcludeSuperclassListeners
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @SignField
    @Column(name = "name")
    private String name;

    @SignField
    @Column(name = "age")
    private Integer age;

    @SignField
    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private String gender;


}
