package com.neu.SP01.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.NursingLevel;
import com.neu.SP01.po.NursingLevelProjectsDTO;
import com.neu.SP01.po.NursingProject;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.NursingLevelService;

@RestController
@RequestMapping("/NursingLevelController")
@CrossOrigin("*")
public class NursingLevelController {
	@Autowired
	private NursingLevelService nls;
	//显示及查询
	@GetMapping("/searchNursingLevel")
    public PageResponseBean<List<NursingLevel>> searchLevels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        
        return nls.searchLevels(name, state, pageNum, pageSize);
    }
	
	//添加护理级别信息
    @PostMapping("/addNursingLevel")
    public ResponseBean<Integer> addNursingLevel(@RequestBody NursingLevel nursingLevel) {
        return nls.addNursingLevel(nursingLevel);
    }
    //删除护理级别及其下的项目
    @PostMapping("/deleteNursingLevel")
    public ResponseBean<Integer> deleteNursingLevel(@RequestParam Integer nursingLevelId) {
        return nls.deleteNursingLevel(nursingLevelId);
    }
    //展示已有的项目
    @GetMapping("/showNursingPro")
    public ResponseBean<List<NursingProject>> showNursingPro(@RequestParam Integer nursingLevelId){
    	return nls.showNursingPro(nursingLevelId);
    	
    }
    //展示没有的项目
    @GetMapping("/showUnNursingPro")
    public ResponseBean<List<NursingProject>> getAvailableProjects(
            @RequestParam Integer nursingLevelId) {
        return nls.getAvailableProjects(nursingLevelId);
    }
    //编辑
    @PostMapping("/editNursingLevel")
    public ResponseBean<Integer> editNursingLevel(@RequestBody NursingLevel nursingLevel) {
    	return nls.editNursingLevel(nursingLevel);
    }
    //改变
    @PostMapping("/changeLevelProject")
    public ResponseBean<String> updateProjects(
            @RequestBody NursingLevelProjectsDTO request) {
        return nls.updateLevelProjects(
            request.getNursingLevelId(), 
            request.getProjectIds()
        );
    }
    
  //展示可用的级别项目
    @GetMapping("/showOk")
    public ResponseBean<List<NursingLevel>> showOk() {
        return nls.showOk();
    }

  //添加护理级别
    @PostMapping("/addCustNursingLevel")
    public ResponseBean<Integer> addNursingLevel(@RequestParam Integer customerId,@RequestParam Integer nursingLevelId) {
    	return nls.addNursingLevel(customerId,nursingLevelId);
    }
    
  //删除护理级别下的项目
    @PostMapping("/deleteNursingPro")
    public ResponseBean<Integer> deleteNursingPro(@RequestParam Integer nursingLevelId, @RequestParam Integer nursingProjectId) {
    	return nls.deleteNursingPro(nursingLevelId,nursingProjectId);
    }
    
    
    /*---------------------------------------------------------------*/
    //修改护理级别 —— 只修改状态
    

    

    

    //给护理级别添加项目
    @PostMapping("/addNursingPro")
    public ResponseBean<Integer> addNursingPro(Integer proId) {
        return null;
    }

    

    //搜索级别下未添加的项目
    @GetMapping("/searchUnNursingPro")
    public ResponseBean<List<NursingProject>> searchUnNursingPro(Integer levelId, String proName) {
        return null;
    }

    //搜索级别下添加的项目
    @GetMapping("/searchNursingPro")
    public ResponseBean<List<NursingProject>> searchNursingPro(Integer levelId, String proName) {
        return null;
    }

}
