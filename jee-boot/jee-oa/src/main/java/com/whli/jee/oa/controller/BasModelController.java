package com.whli.jee.oa.controller;

import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.oa.entity.BasModel;
import com.whli.jee.oa.service.IBasModelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>类或方法描述</p>
 *
 * @author whli
 * @date 2019/1/16 10:21
 */
@RestController
@RequestMapping(value = "/oa/basModel")
public class BasModelController {

    @Autowired
    private IBasModelService modelService;

    /**
     * 分页查询流程模型
     * @return
     * @throws Exception
     */
    @PostMapping("/findByPage")
    @ResponseBody
    public ResponseBean findByPage(@RequestBody BasModel entity) throws Exception{
        ResponseBean responseBean = new ResponseBean();
        int count = modelService.getCount(entity);
        List models = modelService.findByPage(entity);
        if (models != null && models.size() > 0){
            responseBean.setCount(count);
            responseBean.setResults(models);
        }
        return responseBean;
    }

    /**
     * 查询所有流程模型
     * @return
     * @throws Exception
     */
    @PostMapping("/findAll")
    @ResponseBody
    public ResponseBean findAll() throws Exception{
        ResponseBean responseBean = new ResponseBean();
        List models = modelService.findAll();
        if (models != null && models.size() > 0){
            responseBean.setResults(models);
        }
        return responseBean;
    }

    /**
     * 新建一个空模型
     */
    @PostMapping("/addModel")
    @ResponseBody
    public ResponseBean addModel(@RequestBody BasModel entity) throws IOException {
        ResponseBean responseBean = new ResponseBean();
        String modelId = modelService.addModel(entity);
        if (StringUtils.isNotEmpty(modelId)){
            responseBean.setResults(modelId);
        }
        return responseBean;
    }

    @PostMapping("/deleteModel")
    @ResponseBody
    public ResponseBean deleteModel(@RequestBody BasModel entity) throws IOException {
        ResponseBean responseBean = new ResponseBean();
        int modelId = modelService.deleteModel(entity);
        if (modelId > 0){
            responseBean.setResults(true);
            responseBean.setMessage("删除流程模型成功！");
        }
        return responseBean;
    }

    /**
     * 发布流程模型
     */
    @PostMapping("/deployModel")
    @ResponseBody
    public ResponseBean deployModel(@RequestBody BasModel entity) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = modelService.deployModel(entity);
        if (rows > 0){
            responseBean.setResults(true);
            responseBean.setMessage("发布流程模型成功！");
        }
        return responseBean;
    }

    /**
     *  启动流程
     */
    @PostMapping("/startProcess")
    @ResponseBody
    public ResponseBean startProcess(@RequestBody BasModel entity) {
        ResponseBean responseBean = new ResponseBean();
        int rows = modelService.startProcess(entity);
        if (rows > 0){
            responseBean.setResults(true);
            responseBean.setMessage("开始流程成功！");
        }
        return responseBean;
    }

    /**
     *  提交任务
     */
    @PostMapping("/submitTask")
    @ResponseBody
    public ResponseBean submitTask(@RequestBody BasModel entity) {
        ResponseBean responseBean = new ResponseBean();
        int rows = modelService.submitTask(entity);
        if (rows > 0){
            responseBean.setResults(true);
            responseBean.setMessage("提交任务成功！");
        }
        return responseBean;
    }
}
