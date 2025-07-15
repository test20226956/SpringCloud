package com.neu.SP01.po;

import java.util.List;

public class RecommendationResult {
    private List<String> recommendedDishes; // 推荐的菜品名列表
    private String reasoning; // AI的推荐理由
    private String promptUsed; // 实际使用的提示（调试用）
    
    // 构造方法、getter和setter
    public RecommendationResult() {}
    
    public RecommendationResult(List<String> recommendedDishes, String reasoning, String promptUsed) {
        this.recommendedDishes = recommendedDishes;
        this.reasoning = reasoning;
        this.promptUsed = promptUsed;
    }

	public List<String> getRecommendedDishes() {
		return recommendedDishes;
	}

	public void setRecommendedDishes(List<String> recommendedDishes) {
		this.recommendedDishes = recommendedDishes;
	}

	public String getReasoning() {
		return reasoning;
	}

	public void setReasoning(String reasoning) {
		this.reasoning = reasoning;
	}

	public String getPromptUsed() {
		return promptUsed;
	}

	public void setPromptUsed(String promptUsed) {
		this.promptUsed = promptUsed;
	}
    
    

    // getters and setters...
}