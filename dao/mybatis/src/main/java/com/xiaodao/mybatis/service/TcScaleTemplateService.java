package com.xiaodao.mybatis.service;

import com.xiaodao.mybatis.entity.TcScaleTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (TcScaleTemplate)表服务接口
 *
 * @author jianghaitao
 * @since 2024-04-08 11:13:05
 */
public interface TcScaleTemplateService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TcScaleTemplate queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tcScaleTemplate 筛选条件
     * @param pageRequest     分页对象
     * @return 查询结果
     */
    Page<TcScaleTemplate> queryByPage(TcScaleTemplate tcScaleTemplate, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tcScaleTemplate 实例对象
     * @return 实例对象
     */
    TcScaleTemplate insert(TcScaleTemplate tcScaleTemplate);

    /**
     * 修改数据
     *
     * @param tcScaleTemplate 实例对象
     * @return 实例对象
     */
    TcScaleTemplate update(TcScaleTemplate tcScaleTemplate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
