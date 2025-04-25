package com.xiaodao.jimmerdemo.model;

import org.babyfish.jimmer.Formula;
import org.babyfish.jimmer.sql.Embeddable;

/**
 * @author jianghaitao
 * @Classname BookPublisher
 * @Version 1.0.0
 * @Date 2025-04-18 10:36
 * @Created by jianghaitao
 */
@Embeddable
public interface BookPublisher {

    String publishYear();

    int publisherEdition();

    @Formula(dependencies = {"publishYear", "publisherEdition"})
    default String curPublisherEdition() {
        return publishYear() + "-" + publisherEdition();
    }


}
