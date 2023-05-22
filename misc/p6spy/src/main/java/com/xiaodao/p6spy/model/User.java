package com.xiaodao.p6spy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * @Description  
 * @author  jianghaitao
 * @Date 2023-04-02 
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user")
public class User  implements Serializable {

	private static final long serialVersionUID =  8490234506998202975L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition =  "int(11) unsigned") 
	private Integer id;

	@Column(name = "email",columnDefinition =  "varchar(255)") 
	private String email;

	@Column(name = "name",columnDefinition =  "varchar(255)") 
	private String name;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return getId() != null && Objects.equals(getId(), user.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
