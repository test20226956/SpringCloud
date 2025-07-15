package com.neu.SP01.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AIDTO {
	@Data
	@AllArgsConstructor
	public class MealRecommendation {
	    private String mealName;
	    private String reason;
	}

	@Data
	public class DietCycleDetail {
	    private Integer dietCycleId;
	    private String date;
	    private Integer mealId;
	    private Integer type;
	    private Integer state;
	    private String mealName;
	    private String mealImg;
	    private Integer mealType;
	    private Integer mealState;
	}

}
