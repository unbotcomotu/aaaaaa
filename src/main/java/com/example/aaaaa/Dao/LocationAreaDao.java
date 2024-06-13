package com.example.aaaaa.Dao;

import com.example.aaaaa.Entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class LocationAreaDao {
    public List<String> listarLugaresDeEncuentroPokemon(String nombre){
        RestTemplate restTemplate=new RestTemplate();
        try{
            ResponseEntity<Object> response=restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/"+nombre+"/encounters", Object.class);
            ArrayList<HashMap<String,Object>> content=(ArrayList<HashMap<String, Object>>) response.getBody();
            List<String>listaLugares=new ArrayList<>();
            for(HashMap<String,Object> map:content){
                listaLugares.add((String) ((HashMap<String,Object>) map.get("location_area")).get("name"));
            }
            return listaLugares;
        }catch (HttpClientErrorException.NotFound ex){
            return null;
        }

    }

    public List<String> listarLugaresDeEncuentroPokemon2(String nombre){
        RestTemplate restTemplate=new RestTemplate();
        try{
            ResponseEntity<Encounter[]> response=restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/"+nombre+"/encounters", Encounter[].class);
            List<String>listaLugares=new ArrayList<>();
            for(Encounter encounter:response.getBody()){
                listaLugares.add(encounter.getLocation_area().getName());
            }
            return listaLugares;
        }catch (HttpClientErrorException.NotFound ex){
            return null;
        }

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
            ArrayList<HashMap<String,Object>> versiones=(ArrayList<HashMap<String,Object>>)map.get("version_details");
            for(HashMap<String,Object> version:versiones){
                Integer ratio=(Integer) version.get("rate");
                if(ratio>maxRatio){
                    maxRatio=ratio;
                    nombreMetodo=(String) ((HashMap<String,Object>) version.get("version")).get("name");
                }
            }
        }
        methodResponse.put("ratio",maxRatio);
        methodResponse.put("nombre",nombreMetodo);
        return methodResponse;
    }

    public HashMap<String,Object> obtenerMetodoEncuentroMayorPosibilidadCaptura2(String nombreLugar){
        HashMap<String,Object>methodResponse= new HashMap<>();
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<encounter_aiuda>response=restTemplate.getForEntity("https://pokeapi.co/api/v2/location-area/"+nombreLugar+"/", encounter_aiuda.class);
        Integer maxRatio=0;
        String nombreMetodo="";
        for(encounter_method_rates encounter_method_rates :response.getBody().getEncounter_method_rates()){
            for(version_details version_details: encounter_method_rates.getVersion_details()){
                Integer ratio= version_details.getRate();
                if(ratio>=maxRatio){
                    maxRatio=ratio;
                    nombreMetodo= encounter_method_rates.getEncounter_method().getName();
                }
            }
        }
        if(nombreMetodo.isEmpty()){
            nombreMetodo="No existen métodos";
        }
        methodResponse.put("ratio",maxRatio);
        methodResponse.put("nombre",nombreMetodo);
        return methodResponse;
    }

}
