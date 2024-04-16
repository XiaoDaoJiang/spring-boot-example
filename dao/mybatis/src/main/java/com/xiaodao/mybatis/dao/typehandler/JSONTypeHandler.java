package com.xiaodao.mybatis.dao.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaodao.mybatis.valueobject.RelatedCondition;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JSON类型转换
 *
 * @author jianghaitao
 * @Classname JSONTypeHandler
 * @Version 1.0.0
 * @Date 2024-04-08 16:41
 * @Created by jianghaitao
 */
public class JSONTypeHandler extends BaseTypeHandler<List<RelatedCondition>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<RelatedCondition> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, MAPPER.writeValueAsString(parameter));
    }

    @SneakyThrows
    @Override
    public List<RelatedCondition> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public List<RelatedCondition> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public List<RelatedCondition> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));

    }

    private List<RelatedCondition> parseJson(String json) {
        if (json != null) {
            try {
                return MAPPER.readValue(json, new TypeReference<List<RelatedCondition>>() {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
