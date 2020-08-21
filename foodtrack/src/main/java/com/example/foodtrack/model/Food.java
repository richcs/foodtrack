package com.example.foodtrack.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.text.WordUtils;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Food implements Serializable {

    public String name;

    public String servingUnit;

    public double servingQty;

    public int calories;

    public double carbohydrates;

    public double fats;

    public double protein;

    @JsonCreator
    public Food(@JsonProperty("food_name") String name, @JsonProperty("serving_unit") String servingUnit, @JsonProperty("serving_qty") double servingQty) {
        name = WordUtils.capitalize(name);
        this.name = name;
        this.servingUnit = servingUnit;
        this.servingQty = servingQty;
    }
}
