package com.ouitrips.app.entities.circuits;

import com.ouitrips.app.entities.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Circuit_groups",schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CircuitGroup {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_generator")
    @SequenceGenerator(name = "group_id_generator", sequenceName = "groups_pkid_group_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_group")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "is_default")
    private Boolean isDefault;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "color", length = 45)
    private String color;
    @Column(name = "icon", length = 45)
    private String icon;

    @OneToMany(mappedBy = "circuitGroup")
    private Set<Circuit> circuit;



    @ManyToOne
    @JoinColumn(name = "fkid_user")
    private User user;
    @ManyToOne
    @JoinColumn(name = "fkid_category")
    private Category category;


}