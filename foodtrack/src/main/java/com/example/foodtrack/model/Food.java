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

    public double protein;

    public double fats;

    public Food(){
    }

    @JsonCreator
    public Food(@JsonProperty("food_name") String name,
                @JsonProperty("serving_unit") String servingUnit,
                @JsonProperty("serving_qty") double servingQty,
                @JsonProperty("nf_calories") int calories,
                @JsonProperty("nf_total_carbohydrate") double carbohydrates,
                @JsonProperty("nf_protein") double protein,
                @JsonProperty("nf_total_fat") double fats) {
        name = WordUtils.capitalize(name);
        this.name = name;
        this.servingUnit = servingUnit;
        this.servingQty = servingQty;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fats = fats;
    }
}
