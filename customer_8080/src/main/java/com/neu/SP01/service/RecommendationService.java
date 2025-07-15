package com.neu.SP01.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neu.SP01.dao.CustomerDietDao;
import com.neu.SP01.dao.DietCycleDao;
import com.neu.SP01.po.CustomerDiet;
import com.neu.SP01.po.DietCycleDetail;
import com.neu.SP01.po.RecommendationResult;
import com.neu.SP01.po.ResponseBean;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecommendationService {
    
    private final ChatClient chatClient;
    private final DietCycleDao dietCycleDao;
    private final CustomerDietDao customerDietDao;
    
    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
    
    @Autowired
    public RecommendationService(ChatClient.Builder chatClientBuilder, 
                                DietCycleDao dietCycleDao,
                                CustomerDietDao customerDietDao) {
        this.chatClient = chatClientBuilder.build();
        this.dietCycleDao = dietCycleDao;
        this.customerDietDao = customerDietDao;
    }
    
    /**
     * 根据老人ID、日期和餐型推荐菜品（仅返回菜品名称）
     * @param customerId 老人ID
     * @param date 日期(yyyy-MM-dd)
     * @param mealType 餐型(1:早餐 2:午餐 3:晚餐)
     * @return 包含推荐菜品名称列表的结果
     */
    public ResponseBean<RecommendationResult> recommendDishes(Integer customerId, String date, Integer mealType) {
        try {
            // 1. 参数校验
            if (customerId == null || date == null || mealType == null) {
                return new ResponseBean<>(500, "参数不能为空", null);
            }
            
            // 2. 获取当日可用菜品名称列表
            List<String> availableDishNames = getAvailableDishNames(date, mealType);
            if (availableDishNames.isEmpty()) {
                return new ResponseBean<>(500, "当日没有可用的菜品安排");
            }
            
            // 3. 获取老人口味偏好
            String preference = getElderlyPreference(customerId);
            
            // 4. 调用AI服务获取推荐
            String prompt = buildPromptTemplate(preference, availableDishNames, mealType);
            RecommendationResult result = callAIService(prompt, availableDishNames);
            
            // 5. 返回仅包含菜品名称的结果
            return new ResponseBean<>(200, "推荐成功", result);
            
        } catch (Exception e) {
            log.error("推荐菜品异常，customerId: {}, date: {}, mealType: {}", 
                customerId, date, mealType, e);
            return new ResponseBean<>(500, "系统异常");
        }
    }
    
    /**
     * 获取当日可用菜品名称列表
     */
    private List<String> getAvailableDishNames(String date, Integer mealType) {
        try {
            List<DietCycleDetail> dishes = dietCycleDao.selectDietCycleWithMealByType(date, mealType);
            return dishes.stream()
                .map(DietCycleDetail::getMealName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("查询可用菜品失败: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * 获取老人口味偏好（带默认值）
     */
    private String getElderlyPreference(Integer customerId) {
        try {
            CustomerDiet preferences = customerDietDao.getElderlyPreferences(customerId);
            return preferences != null && preferences.getFullPreference() != null ? 
                preferences.getFullPreference() : "口味清淡，易消化";
        } catch (Exception e) {
            log.warn("获取饮食偏好异常，使用默认偏好: {}", e.getMessage());
            return "口味清淡，易消化";
        }
    }
    
    private String buildPromptTemplate(String preferences, List<String> availableDishes, Integer mealType) {
        // 根据 mealType 确定餐型名称
        String mealName = switch (mealType) {
            case 0 -> "早餐";
            case 1 -> "午餐";
            case 2 -> "晚餐";
            default -> "餐食"; // 默认值（理论上不会发生，因为前面有校验）
        };

        return String.format("""
                你是一个专业的饮食推荐助手，请根据以下信息为老人推荐合适的%s菜品。
                
                === 重要信息 ===
                1. 老人饮食偏好: %s
                2. 当前餐型: %s
                3. 可用菜品列表:
                %s
                
                === 推荐要求 ===
                - 从【可用菜品列表】中选择 **2-3个** 最适合老人的%s菜品。
                - 推荐理由需结合【老人饮食偏好】和【当前餐型】的特点（例如：早餐需易消化，午餐需营养均衡，晚餐需清淡）。
                - 必须严格按照以下 JSON 格式返回结果（其他格式无效）:
                {
                    "recommendedDishes": ["菜品1", "菜品2"],
                    "reasoning": "推荐理由（需明确结合偏好和餐型）..."
                }
                """, 
                mealName, preferences, mealName, 
                String.join("\n", availableDishes), mealName);
    }
    
    /**
     * 调用AI服务
     */
    private RecommendationResult callAIService(String promptTemplate, List<String> availableDishes) {
        try {
            Prompt prompt = new Prompt(promptTemplate);
            
            String systemMessage = "你是一个专业的饮食推荐助手，请严格按照JSON格式输出推荐结果";
            
            String response = chatClient.prompt(prompt)
                    .system(systemMessage)
                    .user("请从以下菜品中推荐适合的菜品")
                    .call()
                    .content();
            
            // 解析AI响应
            return parseAiResponse(response, availableDishes);
            
        } catch (Exception e) {
            log.error("调用AI服务异常: {}", e.getMessage());
            return fallbackRecommendation(availableDishes, "AI服务暂时不可用: " + e.getMessage());
        }
    }
    
    /**
     * 解析AI响应
     */
    private RecommendationResult parseAiResponse(String response, List<String> availableDishes) {
        try {
            // 这里应该使用JSON解析器，以下是简化处理
            // 实际项目中应该使用Jackson或Gson
            
            if (response.contains("\"recommendedDishes\"")) {
                String[] parts = response.split("\"recommendedDishes\": \\[");
                if (parts.length > 1) {
                    String dishesPart = parts[1].split("]")[0];
                    String[] dishes = dishesPart.replace("\"", "").split(",");
                    List<String> recommended = List.of(dishes)
                            .stream()
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .filter(availableDishes::contains) // 确保推荐的菜品确实可用
                            .toList();
                    
                    // 提取推荐理由
                    String reasoning = "AI生成的推荐理由";
                    if (response.contains("\"reasoning\"")) {
                        String[] reasonParts = response.split("\"reasoning\": \"");
                        if (reasonParts.length > 1) {
                            reasoning = reasonParts[1].split("\"")[0];
                        }
                    }
                    
                    return new RecommendationResult(
                        recommended,
                        reasoning,
                        response
                    );
                }
            }
            
            // 如果解析失败，使用备用方案
            return fallbackRecommendation(availableDishes, "AI响应格式不正确");
            
        } catch (Exception e) {
            return fallbackRecommendation(availableDishes, "解析AI响应时出错: " + e.getMessage());
        }
    }
    
    /**
     * 备用推荐方案
     */
    private RecommendationResult fallbackRecommendation(List<String> availableDishes, String errorMsg) {
        // 默认推荐前2-3个菜品
        int recommendCount = Math.min(3, Math.max(2, availableDishes.size()));
        List<String> recommended = availableDishes.subList(0, recommendCount);
        
        return new RecommendationResult(
            recommended,
            errorMsg + "，默认推荐: " + String.join(", ", recommended),
            ""
        );
    }
}