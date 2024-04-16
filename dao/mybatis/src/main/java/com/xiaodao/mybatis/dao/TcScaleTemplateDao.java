package com.xiaodao.mybatis.dao;

import com.xiaodao.mybatis.entity.TcScaleTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (TcScaleTemplate)表数据库访问层
 *
 * @author jianghaitao
 * @since 2024-04-08 11:12:58
 */
public interface TcScaleTemplateDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TcScaleTemplate queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tcScaleTemplate 查询条件
     * @param pageable        分页对象
     * @return 对象列表
     */
    List<TcScaleTemplate> queryAllByLimit(TcScaleTemplate tcScaleTemplate, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tcScaleTemplate 查询条件
     * @return 总行数
     */
    long count(TcScaleTemplate tcScaleTemplate);

    /**
     * 新增数据
     *
     * @param tcScaleTemplate 实例对象
     * @return 影响行数
     */
    int insert(TcScaleTemplate tcScaleTemplate);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TcScaleTemplate> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TcScaleTemplate> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TcScaleTemplate> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TcScaleTemplate> entities);

    /**
     * 修改数据
     *
     * @param tcScaleTemplate 实例对象
     * @return 影响行数
     */
    int update(TcScaleTemplate tcScaleTemplate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

