package com.app.gestionturnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    private Menu parent;
    @Column(name = "AppOrder")
    private Integer order;
    private Integer active;
    private String url;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
