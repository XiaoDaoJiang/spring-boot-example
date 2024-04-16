package com.xiaodao.mybatis.service.impl;

import com.xiaodao.mybatis.entity.TcScaleTemplate;
import com.xiaodao.mybatis.dao.TcScaleTemplateDao;
import com.xiaodao.mybatis.service.TcScaleTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (TcScaleTemplate)表服务实现类
 *
 * @author jianghaitao
 * @since 2024-04-08 11:13:07
 */
@Service("tcScaleTemplateService")
public class TcScaleTemplateServiceImpl implements TcScaleTemplateService {
    @Resource
    private TcScaleTemplateDao tcScaleTemplateDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TcScaleTemplate queryById(Integer id) {
        return this.tcScaleTemplateDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tcScaleTemplate 筛选条件
     * @param pageRequest     分页对象
     * @return 查询结果
     */
    @Override
    public Page<TcScaleTemplate> queryByPage(TcScaleTemplate tcScaleTemplate, PageRequest pageRequest) {
        long total = this.tcScaleTemplateDao.count(tcScaleTemplate);
        return new PageImpl<>(this.tcScaleTemplateDao.queryAllByLimit(tcScaleTemplate, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tcScaleTemplate 实例对象
     * @return 实例对象
     */
    @Override
    public TcScaleTemplate insert(TcScaleTemplate tcScaleTemplate) {
        this.tcScaleTemplateDao.insert(tcScaleTemplate);
        return tcScaleTemplate;
    }

    /**
     * 修改数据
     *
     * @param tcScaleTemplate 实例对象
     * @return 实例对象
     */
    @Override
    public TcScaleTemplate update(TcScaleTemplate tcScaleTemplate) {
        this.tcScaleTemplateDao.update(tcScaleTemplate);
        return this.queryById(tcScaleTemplate.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tcScaleTemplateDao.deleteById(id) > 0;
    }
}
