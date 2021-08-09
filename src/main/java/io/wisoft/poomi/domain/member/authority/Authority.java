package io.wisoft.poomi.domain.member.authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authority")
public class Authority {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "authority")
    private String authority;
}