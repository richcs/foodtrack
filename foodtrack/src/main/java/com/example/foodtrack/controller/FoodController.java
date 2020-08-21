package com.example.foodtrack.controller;

import com.example.foodtrack.model.Food;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class FoodController {

    ModelAndView model = new ModelAndView("index");

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getData() {
        List<Food> searchedFoods = new ArrayList<Food>();

        model.addObject("searchedFoods", searchedFoods);
        return model;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    private ModelAndView onSearch(@RequestParam("searchName") String searchName){
        List<Food> searchedFoods = new ArrayList<Food>();
        if(!searchName.isEmpty()){
            searchedFoods = searchFood(searchName);
        }

        model.addObject("searchedFoods", searchedFoods);
        return model;
    }

    private List<Food> searchFood(String foodName) {
        List<Food> searchedFoods = new ArrayList<Food>();
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-app-id", "b9280387");
            headers.set("x-app-key", "ce85e1c8395f3c6e7817ce740f9551c1");
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            String url = "https://trackapi.nutritionix.com/v2/search/instant?query=" + foodName + "&branded=false&self=false";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String json = response.getBody();
            json = json.substring(json.indexOf(":")+1);
            json = json.substring(0, json.length() - 1);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            Food[] foods = mapper.readValue(json, Food[].class);

            searchedFoods = Arrays.asList(foods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchedFoods;
    }
}
