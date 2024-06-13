package com.example.aaaaa.Controller;

import com.example.aaaaa.Dao.LocationAreaDao;
import com.example.aaaaa.Entity.Pokemon;
import com.example.aaaaa.Entity.User;
import com.example.aaaaa.Repository.PokemonRepository;
import com.example.aaaaa.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.logging.Handler;

@RestController
public class KiwiController {

    private final LocationAreaDao locationAreaDao;
    private final PokemonRepository pokemonRepository;
    private final UserRepository userRepository;

    public KiwiController(LocationAreaDao locationAreaDao,
                          PokemonRepository pokemonRepository,
                          UserRepository userRepository) {
        this.locationAreaDao = locationAreaDao;
        this.pokemonRepository = pokemonRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("listarLugaresEncuentroPokemon")
    public ResponseEntity<HashMap<String, Object>> listarLugaresEncuentroPokemon(@RequestParam("nombre") String nombre) {
        HashMap<String, Object> response = new HashMap<>();
        ArrayList<HashMap<String,Object>>listaLugares=new ArrayList<>();
        List<String>listaObtenida=locationAreaDao.listarLugaresDeEncuentroPokemon(nombre);
        for(String lugar:listaObtenida){
            HashMap<String,Object>informacionLugar=new HashMap<>();
            informacionLugar.put("nombre",lugar);
            informacionLugar.put("metodoMasEfectivo",locationAreaDao.obtenerMetodoEncuentroMayorPosibilidadCaptura(lugar));
            listaLugares.add(informacionLugar);
        }
        response.put("content", listaLugares);
        response.put("pokemon",nombre);
        return ResponseEntity.ok(response);
    }

    @GetMapping("capturarPokemon")
    public ResponseEntity<HashMap<String,Object>> capturarPokemon(@RequestParam("nombrePokemon")String nombre,@RequestParam("nombreUsuario")String user){
        HashMap<String, Object> response = new HashMap<>();
        List<String>listaObtenida=locationAreaDao.listarLugaresDeEncuentroPokemon(nombre);
        String lugarElegido=listaObtenida.get((int) ((Math.round((listaObtenida.size()-1)*new Random().nextDouble()))));
        HashMap<String,Object>metodoMasEfectivo=locationAreaDao.obtenerMetodoEncuentroMayorPosibilidadCaptura(lugarElegido);
        Pokemon pokemon=new Pokemon();

        Optional<User>optUser=userRepository.findByNombre(user);
        if(optUser.isEmpty()){
            User newUser= new User();
            newUser.setNombre(user);
            userRepository.save(newUser);
        }
        User userFound=userRepository.findByNombre(user).get();
        pokemon.setUser(userFound);
        pokemon.setNombre(nombre);
        pokemon.setLugarEncuentro(lugarElegido);
        pokemon.setPosibilidadCaptura(Double.valueOf((Integer) metodoMasEfectivo.get("ratio")));
        pokemonRepository.save(pokemon);
        response.put("pokemonEncontrado", pokemon);
        response.put("status","success");
        return ResponseEntity.ok(response);
    }
}
