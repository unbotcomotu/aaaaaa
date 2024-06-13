package com.example.aaaaa.Controller;

import com.example.aaaaa.Dao.LocationAreaDao;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
public class KiwiController {

    private final LocationAreaDao locationAreaDao;

    public KiwiController(LocationAreaDao locationAreaDao) {
        this.locationAreaDao = locationAreaDao;
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
        return ResponseEntity.ok(response);
    }
}
