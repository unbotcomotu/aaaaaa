package com.example.aaaaa.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class encounter_method_rates {
    encounter_method encounter_method;
    List<version_details> version_details;
}
