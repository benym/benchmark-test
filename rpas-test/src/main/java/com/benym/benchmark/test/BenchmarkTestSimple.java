package com.benym.benchmark.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.cglib.BeanCopierCache;
import com.alibaba.fastjson.JSON;
import com.benym.benchmark.test.interfaces.MapStructMapper;
import com.benym.benchmark.test.model.complex.DbDO;
import com.benym.benchmark.test.model.complex.DbVO;
import com.benym.benchmark.test.model.simple.DataBaseDO;
import com.benym.benchmark.test.model.simple.DataBaseVO;
import com.benym.benchmark.test.service.ModelService;
import com.benym.benchmark.test.utils.RpasBeanUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.sf.cglib.beans.BeanCopier;
import org.dozer.DozerBeanMapper;
import org.mapstruct.factory.Mappers;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.TimeUnit;

/**
 * 简单对象拷贝基准测试
 *
 * @author: benym
 */
@Fork(1) // Fork 1个线程进行测试
@BenchmarkMode(Mode.Throughput) // 吞吐量
@Warmup(iterations = 3) // JIT预热
@Measurement(iterations = 10, time = 5) // 迭代10次,每次5s
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@Threads(10) // 线程10个
public class BenchmarkTestSimple {

    /**
     * 作用域为本次JMH测试，线程共享
     * <p>
     * 初始化source数据集
     */
    @State(Scope.Benchmark)
    public static class GenerateModel {
        DataBaseDO dataBaseModel;

        // 初始化
        @Setup(Level.Trial)
        public void prepare() {
            dataBaseModel = new ModelService().get();
        }
    }

    /**
     * 初始化Orika
     */
    @State(Scope.Benchmark)
    public static class OrikaMapper {
        MapperFactory mapperFactory;

        @Setup(Level.Trial)
        public void prepare() {
            mapperFactory = new DefaultMapperFactory.Builder().build();
        }
    }

    /**
     * 初始化Dozer
     */
    @State(Scope.Benchmark)
    public static class DozerMapper {
        DozerBeanMapper mapper;

        @Setup(Level.Trial)
        public void prepare() {
            mapper = new DozerBeanMapper();
        }
    }


    @State(Scope.Benchmark)
    public static class RpasBeanUtilsInit {
        BeanCopier copier;

        @Setup(Level.Trial)
        public void prepare() {
            copier = RpasBeanUtils.getBeanCopierWithNoConverter(DataBaseDO.class, DataBaseVO.class);
        }
    }

    /**
     * 初始化BeanCopier
     */
    @State(Scope.Benchmark)
    public static class BeanCopierInit {
        BeanCopier copier;

        @Setup(Level.Trial)
        public void prepare() {
            copier = BeanCopier.create(DataBaseDO.class, DataBaseVO.class, false);
        }
    }

    /**
     * 初始化MapStruct
     */
    @State(Scope.Benchmark)
    public static class MapStructInit {
        MapStructMapper mapStructMapper;

        @Setup(Level.Trial)
        public void prepare() {
            mapStructMapper = Mappers.getMapper(MapStructMapper.class);
        }
    }

    /**
     * 初始化Objectmapper
     */
    @State(Scope.Benchmark)
    public static class ObjectMapperInit {
        ObjectMapper objectMapper;

        @Setup(Level.Trial)
        public void prepare() {
            objectMapper = new ObjectMapper();
        }
    }

    /**
     * 初始化hutool cglibUtil
     */
    @State(Scope.Benchmark)
    public static class HutoolCglibInit {
        BeanCopier copier;

        @Setup(Level.Trial)
        public void prepare(){
            copier = BeanCopierCache.INSTANCE.get(DataBaseDO.class, DataBaseVO.class, null);
        }
    }

    /**
     * get/set 基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testGetSet(GenerateModel generateModel) throws Exception {
        DataBaseVO dataBaseVO = new DataBaseVO();
        DataBaseDO dataBaseModel = generateModel.dataBaseModel;
        dataBaseVO.setAge(dataBaseModel.getAge());
        dataBaseVO.setName(dataBaseModel.getName());
        dataBaseVO.setTime(dataBaseModel.getTime());
        dataBaseVO.setYear(dataBaseModel.getYear());
        dataBaseVO.setOtherTime(dataBaseModel.getOtherTime());
        return dataBaseVO;
    }

    /**
     * RpasBeanUtils基准测试
     *
     * @param generateModel source
     * @param init 初始化copier
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testRpasBeanUtils(GenerateModel generateModel, RpasBeanUtilsInit init) throws Exception {
        DataBaseVO dataBaseVO = new DataBaseVO();
        init.copier.copy(generateModel.dataBaseModel, dataBaseVO, null);
        return dataBaseVO;
    }

    /**
     * MapStruct基准测试
     *
     * @param generateModel source
     * @param init          初始化的mapper
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testMapStruct(GenerateModel generateModel, MapStructInit init) throws Exception {
        DataBaseVO dataBaseVO = init.mapStructMapper.copy(generateModel.dataBaseModel);
        return dataBaseVO;
    }

    /**
     * BeanCopier基准测试
     *
     * @param generateModel source
     * @param beanCopier    初始化的BeanCopier
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testBeanCopier(GenerateModel generateModel, BeanCopierInit beanCopier) throws Exception {
        BeanCopier copier = beanCopier.copier;
        DataBaseVO dataBaseVO = new DataBaseVO();
        copier.copy(generateModel.dataBaseModel, dataBaseVO, null);
        return dataBaseVO;
    }

    /**
     * Jackson objectMapper基准测试
     *
     * @param generateModel source
     * @param init          初始化的ObjectMapper
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testJackSon(GenerateModel generateModel, ObjectMapperInit init) throws Exception {
        String str = init.objectMapper.writeValueAsString(generateModel.dataBaseModel);
        DataBaseVO dataBaseVO = init.objectMapper.readValue(str, DataBaseVO.class);
        return dataBaseVO;
    }


    /**
     * FastJson基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testFastJson(GenerateModel generateModel) throws Exception {
        String str = JSON.toJSONString(generateModel.dataBaseModel);
        return JSON.parseObject(str, DataBaseVO.class);
    }

    /**
     * Hutool BeanUtil基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testHutoolBeanUtil(GenerateModel generateModel) throws Exception {
        DataBaseVO dataBaseVO = new DataBaseVO();
        BeanUtil.copyProperties(generateModel.dataBaseModel, dataBaseVO);
        return dataBaseVO;
    }


    /**
     * Hutool CglibUtil基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testHutoolCglibUtil(GenerateModel generateModel, HutoolCglibInit init) throws Exception {
        DataBaseVO dataBaseVO = new DataBaseVO();
        init.copier.copy(generateModel.dataBaseModel, dataBaseVO, null);
        return dataBaseVO;
    }

    /**
     * SpringBeanUtils基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testSpringBeanUtils(GenerateModel generateModel) throws Exception {
        DataBaseVO dataBaseVO = new DataBaseVO();
        BeanUtils.copyProperties(generateModel.dataBaseModel, dataBaseVO);
        return dataBaseVO;
    }

    /**
     * Apache BeanUtils基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testApacheBeanUtils(GenerateModel generateModel) throws Exception {
        DataBaseVO dataBaseVO = new DataBaseVO();
        org.apache.commons.beanutils.BeanUtils.copyProperties(dataBaseVO, generateModel.dataBaseModel);
        return dataBaseVO;
    }

    /**
     * Orika基准测试
     *
     * @param generateModel source
     * @param orikaMapper   初始化orika
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testOrikaMapper(GenerateModel generateModel, OrikaMapper orikaMapper) throws Exception {
        MapperFacade mapperFacade = orikaMapper.mapperFactory.getMapperFacade();
        DataBaseVO dataBaseVO = mapperFacade.map(generateModel.dataBaseModel, DataBaseVO.class);
        return dataBaseVO;
    }

    /**
     * Dozer基准测试
     *
     * @param generateModel source
     * @param dozerMapper   初始化dozer
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DataBaseVO testDozerMapping(GenerateModel generateModel, DozerMapper dozerMapper) throws Exception {
        DataBaseVO dataBaseVO = dozerMapper.mapper.map(generateModel.dataBaseModel, DataBaseVO.class);
        return dataBaseVO;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BenchmarkTestSimple.class.getSimpleName())
                .result("result-simple.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }

}
