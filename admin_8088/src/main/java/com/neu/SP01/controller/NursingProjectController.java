
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

import com.neu.SP01.po.NursingProject;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.NursingProjectService;

@RestController
@RequestMapping("/NursingProjectController")
@CrossOrigin("*")
public class NursingProjectController {
	
	@Autowired
	private NursingProjectService nps;
	//显示及查询
	@GetMapping("/searchNursingPro")
    public PageResponseBean<List<NursingProject>> searchProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        
        return nps.searchProjects(name, state, pageNum, pageSize);
    }

    // 添加护理项目
    @PostMapping("/addNursingPro")
    public ResponseBean<Integer> addNursingProject(@RequestBody NursingProject nursingProject) {
        return nps.addNursingProject(nursingProject);
    }
    //编辑
    @PostMapping("/editNursingPro")
    public ResponseBean<String> updateProject(@RequestBody NursingProject nursingProject) {
        return nps.updateNursingProject(nursingProject);
    }
    
 // 删除现有项目
    @PostMapping("/deleteNursingPro")
    public ResponseBean<Integer> deleteNursingPro(@RequestParam Integer nursingProjectId){
    	return nps.deleteNursingPro(nursingProjectId);
    }
    //显示没有的
    @GetMapping("/showUnNursingPro")
    public ResponseBean<List<NursingProject>> getUnpurchasedProjects(
            @RequestParam Integer customerId) {
        return nps.getUnpurchasedProjects(customerId);
    }
    //查询
    @GetMapping("/searchUnNursingPro")
    public ResponseBean<List<NursingProject>> getUnpurchasedProjects(
            @RequestParam Integer customerId,
            @RequestParam(required = false) String name) { // 项目名称参数改为可选
    
        return nps.searchUnpurchasedProjects(customerId, name);
    }



    
    


}
