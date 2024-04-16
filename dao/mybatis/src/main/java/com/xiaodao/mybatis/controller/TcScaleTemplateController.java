package com.xiaodao.mybatis.controller;

import com.xiaodao.mybatis.entity.TcScaleTemplate;
import com.xiaodao.mybatis.service.TcScaleTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (TcScaleTemplate)表控制层
 *
 * @author jianghaitao
 * @since 2024-04-08 11:12:55
 */
@RestController
@RequestMapping("tcScaleTemplate")
public class TcScaleTemplateController {
    /**
     * 服务对象
     */
    @Resource
    private TcScaleTemplateService tcScaleTemplateService;

    /**
     * 分页查询
     *
     * @param tcScaleTemplate 筛选条件
     * @param pageRequest     分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<TcScaleTemplate>> queryByPage(TcScaleTemplate tcScaleTemplate, PageRequest pageRequest) {
        return ResponseEntity.ok(this.tcScaleTemplateService.queryByPage(tcScaleTemplate, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<TcScaleTemplate> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.tcScaleTemplateService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param tcScaleTemplate 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<TcScaleTemplate> add(TcScaleTemplate tcScaleTemplate) {
        return ResponseEntity.ok(this.tcScaleTemplateService.insert(tcScaleTemplate));
    }

    /**
     * 编辑数据
     *
     * @param tcScaleTemplate 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<TcScaleTemplate> edit(TcScaleTemplate tcScaleTemplate) {
        return ResponseEntity.ok(this.tcScaleTemplateService.update(tcScaleTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.tcScaleTemplateService.deleteById(id));
    }

}

