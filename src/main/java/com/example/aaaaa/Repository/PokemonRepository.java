package com.example.aaaaa.Repository;

import com.example.aaaaa.Entity.Pokemon;
import org.hibernate.annotations.TypeRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {
}