package com.example.aaaaa.Controller;

import com.example.aaaaa.Dao.LocationAreaDao;
import com.example.aaaaa.Entity.Pokemon;
import com.example.aaaaa.Entity.User;
import com.example.aaaaa.Repository.PokemonRepository;
import com.example.aaaaa.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<HashMap<String, Object>> listarLugaresEncuentroPokemon(@RequestParam(value = "nombre",required = false) String nombre) {
        HashMap<String, Object> response = new HashMap<>();
        if(nombre==null||nombre.isEmpty()){
            response.put("status","error");
            response.put("error","Ingrese el nombre del pokemon");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ArrayList<HashMap<String,Object>>listaLugares=new ArrayList<>();
        List<String>listaObtenida=locationAreaDao.listarLugaresDeEncuentroPokemon(nombre);
        if(listaObtenida==null){
            response.put("status","error");
            response.put("error","El pokemon no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        for(String lugar:listaObtenida){
            HashMap<String,Object>informacionLugar=new HashMap<>();
            informacionLugar.put("nombre",lugar);
            informacionLugar.put("metodoMasEfectivo",locationAreaDao.obtenerMetodoEncuentroMayorPosibilidadCaptura(lugar));
            listaLugares.add(informacionLugar);
        }
        response.put("status","success");
        response.put("content", listaLugares);
        response.put("pokemon",nombre);
        return ResponseEntity.ok(response);
    }

    @PostMapping("capturarPokemon")
    public ResponseEntity<HashMap<String,Object>> capturarPokemon(@RequestParam(value = "nombrePokemon",required = false)String nombre,
                                                                  @RequestParam(value = "nombreUsuario",required = false)String user){
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> errors = new HashMap<>();
        Boolean validacion=true;
        if(nombre==null||nombre.isEmpty()){
            response.put("status","error");
            errors.put("nombrePokemon","Ingrese el nombre del pokemon");
            validacion=false;
        }
        if(user==null|| user.isEmpty()){
            response.put("status","error");
            errors.put("nombreUsuario","Ingrese el nombre del usuario");
            validacion=false;
        }
        if(validacion){
            List<String>listaObtenida=locationAreaDao.listarLugaresDeEncuentroPokemon(nombre);
            if(listaObtenida==null){
                response.put("status","error");
                errors.put("pokemon","El pokemon ingresado no existe");
                response.put("errors",errors);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
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
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }else {
            response.put("errors",errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("capturarPokemon2")
    public ResponseEntity<HashMap<String,Object>> capturarPokemon2(@RequestParam(value = "nombrePokemon",required = false)String nombre,
                                                                  @RequestParam(value = "nombreUsuario",required = false)String user){
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> errors = new HashMap<>();
        Boolean validacion=true;
        if(nombre==null||nombre.isEmpty()){
            response.put("status","error");
            errors.put("nombrePokemon","Ingrese el nombre del pokemon");
            validacion=false;
        }
        if(user==null|| user.isEmpty()){
            response.put("status","error");
            errors.put("nombreUsuario","Ingrese el nombre del usuario");
            validacion=false;
        }
        if(validacion){
            List<String>listaObtenida=locationAreaDao.listarLugaresDeEncuentroPokemon(nombre);
            if(listaObtenida==null){
                response.put("status","error");
                errors.put("pokemon","El pokemon ingresado no existe");
                response.put("errors",errors);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
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
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }else {
            response.put("errors",errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("listarLugaresEncuentroPokemon2")
    public ResponseEntity<HashMap<String, Object>> listarLugaresEncuentroPokemon2(@RequestParam(value = "nombre",required = false) String nombre) {
        HashMap<String, Object> response = new HashMap<>();
        if(nombre==null||nombre.isEmpty()){
            response.put("status","error");
            response.put("error","Ingrese el nombre del pokemon");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ArrayList<HashMap<String,Object>>listaLugares=new ArrayList<>();
        List<String>listaObtenida=locationAreaDao.listarLugaresDeEncuentroPokemon2(nombre);
        if(listaObtenida==null){
            response.put("status","error");
            response.put("error","El pokemon no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        for(String lugar:listaObtenida){
            HashMap<String,Object>informacionLugar=new HashMap<>();
            informacionLugar.put("nombre",lugar);
            informacionLugar.put("metodoMasEfectivo",locationAreaDao.obtenerMetodoEncuentroMayorPosibilidadCaptura2(lugar));
            listaLugares.add(informacionLugar);
        }
        response.put("status","success");
        response.put("content", listaLugares);
        response.put("pokemon",nombre);
        return ResponseEntity.ok(response);
    }
}
