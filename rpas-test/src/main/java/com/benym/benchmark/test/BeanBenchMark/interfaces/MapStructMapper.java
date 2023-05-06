package com.benym.benchmark.test.BeanBenchMark.interfaces;

import com.benym.benchmark.test.model.simple.DataBaseDO;
import com.benym.benchmark.test.model.simple.DataBaseVO;
import org.mapstruct.Mapper;

/**
 * @author: benym
 */
@Mapper
public interface MapStructMapper {
    DataBaseVO copy(DataBaseDO dataBaseDO);
}
