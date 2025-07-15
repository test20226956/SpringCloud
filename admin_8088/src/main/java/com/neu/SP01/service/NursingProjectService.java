package com.neu.SP01.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.NursingLevelProjectDao;
import com.neu.SP01.dao.NursingProjectDao;
import com.neu.SP01.po.CheckInRecord;
import com.neu.SP01.po.NursingProject;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

@Service
@Transactional
public class NursingProjectService {
	
	@Autowired
    private NursingLevelProjectDao nlpd;
	@Autowired
    private NursingProjectDao npd;
	@Autowired
    private CheckInRecordDao cird;
	
	//显示搜索
	public PageResponseBean<List<NursingProject>> searchProjects(
            String name, 
            Integer state, 
            Long pageNum, 
            Long pageSize) {
        
        try {
            // 1. 参数处理
            pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
            pageSize = (pageSize == null || pageSize < 1 || pageSize > 100) ? 10 : pageSize;
            state = (state == null) ? 1 : state; // 默认状态为1
            
            // 2. 计算偏移量
            Long offset = (pageNum - 1) * pageSize;
            
            // 3. 执行查询
            List<NursingProject> projects = npd.searchProjects(
                name, state, offset, pageSize);
            
            // 4. 处理空结果
            if (projects.isEmpty()) {
                return new PageResponseBean<>(500, "无符合条件的数据", null);
            }
            
            // 5. 获取总数
            Long total = npd.countSearchProjects(name, state);
            
            // 6. 构建响应
            PageResponseBean<List<NursingProject>> response = 
                new PageResponseBean<>(200, "查询成功", projects);
            response.setTotal(total);
            return response;
            
        } catch (Exception e) {
            return new PageResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
    
	//添加
	public ResponseBean<Integer> addNursingProject(NursingProject nursingProject) {
        try {
            // 简单非空校验
            if (nursingProject.getState() == null ||
                nursingProject.getTime() == null ||
                nursingProject.getName() == null || nursingProject.getName().isEmpty() ||
                nursingProject.getPrice() == null || nursingProject.getPrice().isEmpty() ||
                nursingProject.getPeriod() == null || nursingProject.getPeriod().isEmpty() ||
                nursingProject.getDescription() == null || nursingProject.getDescription().isEmpty()) {
                return new ResponseBean<>(500, "所有字段都不能为空", null);
            }
            
            // 执行插入
            int result = npd.insert(nursingProject);
            
            if (result > 0) {
                return new ResponseBean<>(200, "添加成功", nursingProject.getNursingProjectId());
            } else {
                return new ResponseBean<>(500, "添加失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
	//编辑
	public ResponseBean<String> updateNursingProject(NursingProject updateProject) {
        try {
            // 1. 校验ID必填
            if (updateProject.getNursingProjectId() == null) {
                return new ResponseBean<>(500, "项目ID不能为空");
            }

            // 2. 检查项目是否存在
            NursingProject existing = npd.selectById(updateProject.getNursingProjectId());
            if (existing == null) {
                return new ResponseBean<>(500, "未找到指定护理项目");
            }

            // 3. 合并数据（空字段保留原值）
            NursingProject merged = mergeData(existing, updateProject);

            // 4. 执行更新
            int affected = npd.updateSelective(merged);

            // 5. 状态改为0时级联禁用关联
            if (updateProject.getState() != null && updateProject.getState() == 0) {
                nlpd.disableRelationsByProjectId(updateProject.getNursingProjectId());
            }

            return affected > 0 
                ? new ResponseBean<>(200, "更新成功", null) 
                : new ResponseBean<>(500, "更新失败");

        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }

    private NursingProject mergeData(NursingProject existing, NursingProject update) {
        NursingProject merged = new NursingProject();
        merged.setNursingProjectId(existing.getNursingProjectId());
        merged.setState(update.getState() != null ? update.getState() : existing.getState());
        merged.setTime(update.getTime() != null ? update.getTime() : existing.getTime());
        merged.setName(StringUtils.hasText(update.getName()) ? update.getName() : existing.getName());
        merged.setPrice(StringUtils.hasText(update.getPrice()) ? update.getPrice() : existing.getPrice());
        merged.setPeriod(StringUtils.hasText(update.getPeriod()) ? update.getPeriod() : existing.getPeriod());
        merged.setDescription(StringUtils.hasText(update.getDescription()) ? update.getDescription() : existing.getDescription());
        return merged;
    }
    //删除
    public ResponseBean<Integer> deleteNursingPro(Integer nursingProjectId) {
        try {
            // 1. 参数校验
            if (nursingProjectId == null) {
                return new ResponseBean<>(500, "项目ID不能为空");
            }

            // 2. 级联禁用所有关联关系（先操作从表）
            int disabledRelations = nlpd.disableRelationsByProjectId(nursingProjectId);

            // 3. 逻辑删除主项目（后操作主表）
            int deletedProject = npd.deleteNursingProject(nursingProjectId);

            if (deletedProject == 0) {
                return new ResponseBean<>(500, "未找到指定护理项目");
            }

            return new ResponseBean<>(200, "删除成功", null);

        } catch (Exception e) {
            // @Transactional会自动回滚所有操作
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
    
    //显示客户未有的
    public ResponseBean<List<NursingProject>> getUnpurchasedProjects(Integer customerId) {
        try {
            // 1. 验证参数
            if (customerId == null || customerId <= 0) {
                return new ResponseBean<>(500, "客户ID不能为空");
            }
            
            // 2. 获取客户当前有效的入住记录
            CheckInRecord activeRecord = cird.findActiveCheckInRecord(customerId);
            if (activeRecord == null) {
                return new ResponseBean<>(500, "客户没有有效的入住记录");
            }
            
            // 3. 查询未购买项目
            List<NursingProject> result = npd.findUnpurchasedProjects(
                customerId, activeRecord.getNursingLevelId());
            
            if (result.isEmpty()) {
                return new ResponseBean<>(500, "查询失败");
            }
            
            return new ResponseBean<>(200, "查询成功", result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
    
    //在未有的下面查询
    public ResponseBean<List<NursingProject>> searchUnpurchasedProjects(
            Integer customerId, 
            String name) {
        try {
            // 1. 参数校验
            if (customerId == null || customerId <= 0) {
                return new ResponseBean<>(500, "客户ID不能为空");
            }
            
            // 2. 获取客户当前有效的入住记录
            CheckInRecord activeRecord = cird.findActiveCheckInRecord(customerId);
            if (activeRecord == null) {
                return new ResponseBean<>(500, "客户没有有效的入住记录");
            }
            
            // 3. 查询未购买项目（支持模糊搜索）
            List<NursingProject> result = npd.searchUnpurchasedProjects(
                customerId, 
                activeRecord.getNursingLevelId(),
                name); // 传入项目名称参数
            if(result==null) {
            	return new ResponseBean<>(500, "查询失败");
            }
            
            return new ResponseBean<>(200, "查询成功", result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
}