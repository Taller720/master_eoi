package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements Serializable {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column (name ="email",length = 50)
    private String email;
    @Column (name ="nombre_usuario",length = 30)
    private String nombreUsuario;
    @Column (name ="password",length = 250)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "empleado_id", referencedColumnName = "id")
    private Empleado empleado;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Basic(optional = false)
    private boolean active;


}
