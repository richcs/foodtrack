package com.example.foodtrack.controller;

import com.example.foodtrack.model.Food;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.*;
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

    List<Food> myFoods = new ArrayList<Food>();

    List<Food> searchedFoods = new ArrayList<Food>();

    ModelAndView model = new ModelAndView("index");

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView get() {
        model.addObject("searchedFoods", searchedFoods);
        model.addObject("myFoods", myFoods);
        return model;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params="action=add")
    private ModelAndView addFood(@RequestParam("foodName") String foodName, @RequestParam("action") String action){
        // If food item is already in myFoods list, increase quantity instead
        boolean foodExists = false;
        for(Food food : myFoods){
            if(food.name.equals(foodName)){
                food.incrementFoodQty();
                foodExists = true;
            }
        }

        if(!foodExists){
            Food newFood = sendFoodInfoRequest(foodName);
            myFoods.add(newFood);
        }

        model.addObject("myFoods", myFoods);
        return model;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params="action=remove")
    private ModelAndView removeFood(@RequestParam("foodName") String foodName, @RequestParam("action") String action){
        myFoods.removeIf(obj -> obj.name.equals(foodName));
        model.addObject("myFoods", myFoods);
        return model;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params="action=search")
    private ModelAndView searchFood(@RequestParam("searchName") String searchName, @RequestParam("action") String action){
        if(!searchName.isEmpty()){
            searchedFoods = sendFoodListRequest(searchName);
        }

        model.addObject("searchedFoods", searchedFoods);
        return model;
    }

    private Food sendFoodInfoRequest(String foodName){
        Food food = new Food();
        try{
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-app-id", "b9280387");
            headers.set("x-app-key", "ce85e1c8395f3c6e7817ce740f9551c1");
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject foodNameJsonObject = new JSONObject();
            foodNameJsonObject.put("query", foodName);
            HttpEntity<String> entity = new HttpEntity<>(foodNameJsonObject.toString(), headers);
            String url = "https://trackapi.nutritionix.com/v2/natural/nutrients?query=" + foodName;

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String json = response.getBody();
            json = json.substring(json.indexOf("[")+1);
            json = json.substring(0, json.length() - 2);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            food = mapper.readValue(json, Food.class);
            return food;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return food;
    }

    private List<Food> sendFoodListRequest(String foodName) {
        List<Food> searchedFoods = new ArrayList<Food>();
        try{
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-app-id", "b9280387");
            headers.set("x-app-key", "ce85e1c8395f3c6e7817ce740f9551c1");
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            String url = "https://trackapi.nutritionix.com/v2/search/instant?query=" + foodName + "&branded=false&self=false";

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
