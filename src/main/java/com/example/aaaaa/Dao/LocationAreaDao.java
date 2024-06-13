package com.example.aaaaa.Dao;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class LocationAreaDao {
    public List<String> listarLugaresDeEncuentroPokemon(String nombre){
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Object> response=restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/"+nombre+"/encounters", Object.class);
        ArrayList<HashMap<String,Object>> content=(ArrayList<HashMap<String, Object>>) response.getBody();
        List<String>listaLugares=new ArrayList<>();
        for(HashMap<String,Object> map:content){
            listaLugares.add((String) ((HashMap<String,Object>) map.get("location_area")).get("name"));
        }
        return listaLugares;
    }

    public HashMap<String,Object> obtenerMetodoEncuentroMayorPosibilidadCaptura(String nombreLugar){
        HashMap<String,Object>methodResponse= new HashMap<>();
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Object> response=restTemplate.getForEntity("https://pokeapi.co/api/v2/location-area/"+nombreLugar+"/", Object.class);
        ArrayList<HashMap<String,Object>> content=(ArrayList<HashMap<String, Object>>) ((HashMap<String,Object>) response.getBody()).get("encounter_method_rates");
        List<String>listaLugares=new ArrayList<>();
        Integer maxRatio=0;
        String nombreMetodo=null;
        for(HashMap<String,Object> map:content){
            Integer ratio=Integer.parseInt((String) ((HashMap<String,Object>) map.get("version_details")).get("rate"));
            if(ratio>maxRatio){
                maxRatio=ratio;
                nombreMetodo=(String) ((HashMap<String,Object>) ((HashMap<String,Object>) map.get("version_details")).get("version")).get("name");
            }
        }
        methodResponse.put("ratio",maxRatio);
        methodResponse.put("nombre",nombreMetodo);
        return methodResponse;
    }

}
