package com.neu.SP01.controller;

import com.neu.SP01.po.CustomerNursingDTO;
import com.neu.SP01.po.ResponseBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 这个类是客户护理设置
@RestController
@RequestMapping("/CustNursingController")
@CrossOrigin("*")
public class CustNursingController {

    //显示所有客户和其护理信息
    @GetMapping("/showCustomerNursing")
    public ResponseBean<List<CustomerNursingDTO>> showCustomerNursing() {
        return null;
    }

    //根据客户姓名查找 - 真的不会取名了救命
    @GetMapping("/search")
    public ResponseBean<List<CustomerNursingDTO>> search(String name) {
        return null;
    }

    //设置护理级别
    @PostMapping("/addNursingLevel")
    public ResponseBean<Integer> addNursingLevel(Integer custId, Integer levelId) {
        return null;
    }

    //删除护理级别 - 应该去入住那里改级别 级别下的项目也要删除
    @PostMapping("/deleteNursingLevel")
    public ResponseBean<Integer> deleteNursingLevel(Integer custId, Integer levelId) {
        return null;
    }


}
