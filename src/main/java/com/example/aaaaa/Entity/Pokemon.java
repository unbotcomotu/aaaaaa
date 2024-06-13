package com.example.aaaaa.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pokemon")
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pokemon", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "lugar_encuentro", nullable = false, length = 45)
    private String lugarEncuentro;

    @Column(name = "posibilidad_captura", nullable = false)
    private Double posibilidadCaptura;

}