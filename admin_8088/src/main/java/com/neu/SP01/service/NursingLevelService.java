package com.neu.SP01.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.NursingLevelDao;
import com.neu.SP01.dao.NursingLevelProjectDao;
import com.neu.SP01.po.NursingLevel;
import com.neu.SP01.po.NursingProject;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

@Service
@Transactional
public class NursingLevelService {
	@Autowired
    private NursingLevelDao nld;
	@Autowired
    private NursingLevelProjectDao nlpd;
	@Autowired
    private CheckInRecordDao cird;
	
	public PageResponseBean<List<NursingLevel>> searchLevels(
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
            List<NursingLevel> levels = nld.searchLevels(
                name, state, offset, pageSize);
            
            // 4. 处理空结果
            if (levels.isEmpty()) {
                return new PageResponseBean<>(500, "无符合条件的数据", null);
            }
            
            // 5. 获取总数
            Long total = nld.countSearchLevels(name, state);
            
            // 6. 构建响应
            PageResponseBean<List<NursingLevel>> response = 
                new PageResponseBean<>(200, "查询成功", levels);
            response.setTotal(total);
            return response;
            
        } catch (Exception e) {
            return new PageResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
	//添加护理级别
	public ResponseBean<Integer> addNursingLevel(NursingLevel nursingLevel) {
        try {
            // 设置默认状态为1（启用），如果未设置
            if (nursingLevel.getState() == null) {
                nursingLevel.setState(1);
            }
            
            int result = nld.insert(nursingLevel);
            if (result > 0) {
                return new ResponseBean<>(200, "添加成功", nursingLevel.getNursingLevelId());
            } else {
                return new ResponseBean<>(500, "添加失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
	
	//删除护理级别
	public ResponseBean<Integer> deleteNursingLevel(Integer nursingLevelId) {
        try {
            // 1. 检查护理级别是否存在
            NursingLevel level = nld.selectById(nursingLevelId);
            if (level == null) {
                return new ResponseBean<>(500, "未找到指定的护理级别", null);
            }
            
            // 2. 先删除关联的护理项目（设置state=0）
            //int affectedProjects = nlpd.updateStateByLevelId(nursingLevelId);
            
            // 3. 再删除护理级别本身（设置state=0）
            int affectedLevel = nld.deleteNursingLevel(nursingLevelId);
            
            // 4. 返回受影响的总记录数
            //int totalAffected = affectedLevel + affectedProjects;
            
            if (affectedLevel > 0) {
                return new ResponseBean<>(200, "删除成功", affectedLevel);
            } else {
                return new ResponseBean<>(500, "删除操作未影响任何记录", 0);
            }
        } catch (Exception e) {
            // 事务会自动回滚
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
	
	//展示该级别下的项目
	public ResponseBean<List<NursingProject>> showNursingPro(Integer nursingLevelId) {
        try {
            // 1. 查询状态为1的有效项目
            List<NursingProject> projects = nld.showNursingPro(nursingLevelId);
            
            // 2. 处理查询结果
            if (projects == null || projects.isEmpty()) {
                return new ResponseBean<>(500, "该护理级别下没有可用项目", null);
            }
            
            return new ResponseBean<>(200, "查询成功", projects);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
	//展示该级别下没有的项目
	public ResponseBean<List<NursingProject>> getAvailableProjects(Integer nursingLevelId) {
        try {
            List<NursingProject> projects = nld.findAvailableProjectsForLevel(nursingLevelId);
            
            if (projects == null || projects.isEmpty()) {
                return new ResponseBean<>(500, "没有可添加的护理项目", null);
            }
            
            return new ResponseBean<>(200, "查询成功", projects);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误: " + e.getMessage(), null);
        }
    }
	
	//更新护理级别
	public ResponseBean<Integer> editNursingLevel(NursingLevel nursingLevel) {
        try {
            // 1. 参数校验
            if (nursingLevel == null || nursingLevel.getNursingLevelId() == null) {
                return new ResponseBean<>(500, "参数不完整", null);
            }
            
            // 2. 检查记录是否存在
            NursingLevel existing = nld.selectById(nursingLevel.getNursingLevelId());
            if (existing == null) {
                return new ResponseBean<>(500, "未找到指定的护理级别", null);
            }
            
            // 3. 设置默认值（可选）
            if (nursingLevel.getState() == null) {
                nursingLevel.setState(existing.getState()); // 保持原状态
            }
            
            // 4. 执行更新
            int affectedRows = nld.updateNursingLevel(nursingLevel);
            
            if (affectedRows > 0) {
                return new ResponseBean<>(200, "更新成功", affectedRows);
            } else {
                return new ResponseBean<>(500, "更新失败", 0);
            }
        } catch (Exception e) {
            // 事务会自动回滚
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
	
	public int smartAddRelation(Integer levelId, Integer projectId) {
	    // 1. 尝试激活现有记录（包括state=0的情况）
	    int affected = nlpd.activateRelation(levelId, projectId);
	    
	    // 2. 如果没有激活任何记录，说明是全新关联
	    if (affected == 0) {
	        try {
	            return nlpd.insertRelation(levelId, projectId);
	        } catch (DuplicateKeyException e) {
	            // 3. 处理并发情况：其他线程可能已经插入
	            return nlpd.activateRelation(levelId, projectId);
	        }
	    }
	    return affected;
	}
	
	public ResponseBean<String> updateLevelProjects(Integer nursingLevelId, 
            List<Integer> newProjectIds) {
		try {
			// 获取现有关联
			List<Integer> existingIds = nlpd.findExistingProjectIds(nursingLevelId);
			Set<Integer> existingSet = new HashSet<>(existingIds);
			Set<Integer> newSet = new HashSet<>(newProjectIds);
			
			// 计算差异
			List<Integer> toAdd = newProjectIds.stream()
			.filter(id -> !existingSet.contains(id))
			.collect(Collectors.toList());
			
			List<Integer> toRemove = existingIds.stream()
			.filter(id -> !newSet.contains(id))
			.collect(Collectors.toList());
			
			// 处理新增（使用新方法）
			int addedCount = 0;
			for (Integer projectId : toAdd) {
				addedCount += smartAddRelation(nursingLevelId, projectId);
			}
			
			// 处理移除
			int removedCount = 0;
			if (!toRemove.isEmpty()) {
				removedCount = nlpd.batchDisableRelations(nursingLevelId, toRemove);
			}
			
			return new ResponseBean<>(200,String.format("更新完成，生效%d项，移除%d项", addedCount, removedCount),null);
			} catch (Exception e) {
			return new ResponseBean<>(500, "更新失败: " + e.getMessage(), null);
		}
	}
	
	
	//展示可用的级别
		public ResponseBean<List<NursingLevel>> showOk() {
	        try {
	            
	            
	            List<NursingLevel> result = nld.showOk();
	            if (result !=null) {
	                return new ResponseBean<>(200, "查询成功", result);
	            } else {
	                return new ResponseBean<>(500, "查询失败", null);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
	        }
	    }
		//添加护理级别
		
		public ResponseBean<Integer> addNursingLevel(Integer customerId, Integer nursingLevelId) {
	        try {
	            // 执行更新操作
	            int result = cird.addNursingLevel(customerId, nursingLevelId);
	            
	            if (result > 0) {
	                // 更新成功
	                return new ResponseBean<>(200,"添加成功",result);
	            } else {
	                // 更新失败，可能没有找到匹配的记录
	                return new ResponseBean<>(500, "添加失败");
	            }
	        } catch (Exception e) {
	            // 捕获并处理异常
	            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
	        }
	    }
		
		//删除护理级别下的项目
		public ResponseBean<Integer> deleteNursingPro(Integer nursingLevelId,Integer nursingProjectId) {
	        try {
	            // 1. 检查护理级别是否存在
	            NursingLevel level = nld.selectById(nursingLevelId);
	            if (level == null) {
	                return new ResponseBean<>(500, "未找到指定的护理级别", null);
	            }
	            
	            // 2. 先删除关联的护理项目（设置state=0）
	            int affectedProjects = nlpd.disableRelation(nursingLevelId,nursingProjectId);
	            
	            
	            if (affectedProjects > 0) {
	                return new ResponseBean<>(200, "删除成功", affectedProjects);
	            } else {
	                return new ResponseBean<>(500, "删除操作未影响任何记录", 0);
	            }
	        } catch (Exception e) {
	            // 事务会自动回滚
	            e.printStackTrace();
	            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
	        }
	    }
}