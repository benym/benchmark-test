package com.benym.benchmark.test.BeanBenchMark.interfaces;

import com.benym.benchmark.test.BeanBenchMark.model.complex.DbDO;
import com.benym.benchmark.test.BeanBenchMark.model.complex.DbVO;
import org.mapstruct.Mapper;

/**
 * @date: 2022/11/14 18:51
 */
@Mapper
public interface MapStructMapperComplex {
    DbVO copy(DbDO dbDO);
}
