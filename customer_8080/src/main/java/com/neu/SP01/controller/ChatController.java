package com.neu.SP01.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/ChatController")
@CrossOrigin("*")
public class ChatController {
	
	private final ChatModel chatModel;

	public ChatController(ChatModel chatModel) {
		super();
		this.chatModel = chatModel;
	}
	
	@GetMapping(value = "/chatStream", produces = "text/event-stream;charset=utf-8")
	public Flux<String> chatStream(@RequestParam String msg) {
	    // 1. 构造完整的 Prompt（强调健康助手角色和回答要求）
	    String prompt = """
	        你是一个智能健康助手，专门为老年人提供健康咨询服务。
	        请根据以下问题给出清晰、易懂的回答，避免使用复杂医学术语：
	        
	        问题：%s
	        
	        回答要求：
	        1. 语言通俗易懂，适合老年人理解。
	        2. 如果问题不明确，主动询问关键信息（如症状持续时间）。
	        3. 必要时给出具体建议（如饮食、运动、药物注意事项）。
	        """.formatted(msg);

	    // 2. 调用流式 AI 模型（假设 chatModel 已封装 Prompt 处理逻辑）
	    return chatModel.stream(prompt)
	        .onErrorResume(e -> Flux.just(
	            "抱歉，系统暂时无法处理您的请求，请稍后再试。",
	            "错误详情：" + e.getMessage()
	        ));
	}

}
