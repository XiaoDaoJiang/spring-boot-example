package com.xiaodao.batch.mapper;

import com.xiaodao.batch.dto.Source;
import com.xiaodao.batch.entities.Target;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SourceTargetMapperTest {

    @Test
    public void testMapping() {
        Source s = new Source();
        s.setTest("5");

        Target t = SourceTargetMapper.MAPPER.toTarget(s);
        assertEquals(5, (long) t.getTesting());
    }
}