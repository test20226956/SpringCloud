package com.neu.SP01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.RecommendationResult;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.RecommendationService;

@RestController
@RequestMapping("/RecommendController")
@CrossOrigin("*")

public class RecommendationController {
    
    private final RecommendationService recommendationService;
    
    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
    
    @GetMapping("/dishes")
    public ResponseBean<RecommendationResult> recommendDishes(
            @RequestParam Integer customerId,
            @RequestParam String date,
            @RequestParam Integer mealType) {
        
        return recommendationService.recommendDishes(customerId, date, mealType);
    }
}