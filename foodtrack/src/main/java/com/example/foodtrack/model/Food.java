package com.example.foodtrack.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.text.WordUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Food implements Serializable {

    public String name;

    public String servingUnit;

    public BigDecimal servingQty;

    public int calories;

    public BigDecimal carbohydrates;

    public BigDecimal protein;

    public BigDecimal fats;

    public BigDecimal originalQty;

    public int originalCalories;

    public BigDecimal originalCarbohydrates;

    public BigDecimal originalProtein;

    public BigDecimal originalFats;

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
        this.name = WordUtils.capitalize(name);
        this.servingUnit = servingUnit;
        this.servingQty = BigDecimal.valueOf(servingQty);
        this.calories = calories;
        this.carbohydrates = BigDecimal.valueOf(carbohydrates);
        this.protein = BigDecimal.valueOf(protein);
        this.fats = BigDecimal.valueOf(fats);
        this.originalQty = this.servingQty;
        this.originalCalories = this.calories;
        this.originalCarbohydrates = this.carbohydrates;
        this.originalProtein = this.protein;
        this.originalFats = this.fats;

    }

    public void incrementFoodQty(){
        servingQty = servingQty.add(originalQty);
        calories = calories + originalCalories;
        carbohydrates = carbohydrates.add(originalCarbohydrates);
        protein = protein.add(originalProtein);
        fats = fats.add(originalFats);
    }
}
