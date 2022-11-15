package com.benym.benchmark.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.cglib.BeanCopierCache;
import com.alibaba.fastjson.JSON;
import com.benym.benchmark.test.interfaces.MapStructMapperComplex;
import com.benym.benchmark.test.model.complex.DbDO;
import com.benym.benchmark.test.model.complex.DbVO;
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
 * 复杂对象拷贝基准测试
 *
 * @author: benym
 */
@Fork(1) // Fork 1个线程进行测试
@BenchmarkMode(Mode.Throughput) // 吞吐量
@Warmup(iterations = 3) // JIT预热
@Measurement(iterations = 10, time = 5) // 迭代10次,每次5s
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@Threads(10) // 线程10个
public class BenchmarkTestComplex {
    /**
     * 作用域为本次JMH测试，线程共享
     * <p>
     * 初始化source数据集
     */
    @State(Scope.Benchmark)
    public static class GenerateModel {
        DbDO dbDo;

        // 初始化
        @Setup(Level.Trial)
        public void prepare() {
            dbDo = new ModelService().getComplex();
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
            copier = RpasBeanUtils.getBeanCopierWithNoConverter(DbDO.class, DbVO.class);
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
            copier = BeanCopier.create(DbDO.class, DbVO.class, false);
        }
    }

    /**
     * 初始化MapStruct
     */
    @State(Scope.Benchmark)
    public static class MapStructInit {
        MapStructMapperComplex mapStructMapper;

        @Setup(Level.Trial)
        public void prepare() {
            mapStructMapper = Mappers.getMapper(MapStructMapperComplex.class);
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
            copier = BeanCopierCache.INSTANCE.get(DbDO.class, DbVO.class, null);
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
    public DbVO testGetSet(GenerateModel generateModel) throws Exception {
        DbVO DbVO = new DbVO();
        DbDO dbDo = generateModel.dbDo;
        DbVO.setAge(dbDo.getAge());
        DbVO.setName(dbDo.getName());
        DbVO.setTime(dbDo.getTime());
        DbVO.setYear(dbDo.getYear());
        DbVO.setMockModelOne(dbDo.getMockModelOne());
        DbVO.setMockModelTwo(dbDo.getMockModelTwo());
        DbVO.setOtherTime(dbDo.getOtherTime());
        DbVO.setField00(dbDo.getField00());
        DbVO.setField01(dbDo.getField01());
        DbVO.setField02(dbDo.getField02());
        DbVO.setField03(dbDo.getField03());
        DbVO.setField04(dbDo.getField04());
        DbVO.setField05(dbDo.getField05());
        DbVO.setField06(dbDo.getField06());
        DbVO.setField07(dbDo.getField07());
        DbVO.setField08(dbDo.getField08());
        DbVO.setField09(dbDo.getField09());
        DbVO.setField10(dbDo.getField10());
        DbVO.setField11(dbDo.getField11());
        DbVO.setField12(dbDo.getField12());
        DbVO.setField13(dbDo.getField13());
        DbVO.setField14(dbDo.getField14());
        DbVO.setField15(dbDo.getField15());
        DbVO.setField16(dbDo.getField16());
        DbVO.setField17(dbDo.getField17());
        DbVO.setField18(dbDo.getField18());
        DbVO.setField19(dbDo.getField19());
        DbVO.setField20(dbDo.getField20());
        DbVO.setField21(dbDo.getField21());
        DbVO.setField22(dbDo.getField22());
        DbVO.setField23(dbDo.getField23());
        DbVO.setField24(dbDo.getField24());
        DbVO.setField25(dbDo.getField25());
        DbVO.setField26(dbDo.getField26());
        DbVO.setField27(dbDo.getField27());
        DbVO.setField28(dbDo.getField28());
        DbVO.setField29(dbDo.getField29());
        DbVO.setField30(dbDo.getField30());
        DbVO.setField31(dbDo.getField31());
        DbVO.setField32(dbDo.getField32());
        DbVO.setField33(dbDo.getField33());
        DbVO.setField34(dbDo.getField34());
        DbVO.setField35(dbDo.getField35());
        DbVO.setField36(dbDo.getField36());
        DbVO.setField37(dbDo.getField37());
        DbVO.setField38(dbDo.getField38());
        DbVO.setField39(dbDo.getField39());
        DbVO.setField40(dbDo.getField40());
        DbVO.setField41(dbDo.getField41());
        DbVO.setField42(dbDo.getField42());
        DbVO.setField43(dbDo.getField43());
        DbVO.setField44(dbDo.getField44());
        DbVO.setField45(dbDo.getField45());
        DbVO.setField46(dbDo.getField46());
        DbVO.setField47(dbDo.getField47());
        DbVO.setField48(dbDo.getField48());
        DbVO.setField49(dbDo.getField49());
        DbVO.setField50(dbDo.getField50());
        DbVO.setField51(dbDo.getField51());
        DbVO.setField52(dbDo.getField52());
        DbVO.setField53(dbDo.getField53());
        DbVO.setField54(dbDo.getField54());
        DbVO.setField55(dbDo.getField55());
        DbVO.setField56(dbDo.getField56());
        DbVO.setField57(dbDo.getField57());
        DbVO.setField58(dbDo.getField58());
        DbVO.setField59(dbDo.getField59());
        DbVO.setField60(dbDo.getField60());
        DbVO.setField61(dbDo.getField61());
        DbVO.setField62(dbDo.getField62());
        DbVO.setField63(dbDo.getField63());
        DbVO.setField64(dbDo.getField64());
        DbVO.setField65(dbDo.getField65());
        DbVO.setField66(dbDo.getField66());
        DbVO.setField67(dbDo.getField67());
        DbVO.setField68(dbDo.getField68());
        DbVO.setField69(dbDo.getField69());
        DbVO.setField70(dbDo.getField70());
        DbVO.setField71(dbDo.getField71());
        DbVO.setField72(dbDo.getField72());
        DbVO.setField73(dbDo.getField73());
        DbVO.setField74(dbDo.getField74());
        DbVO.setField75(dbDo.getField75());
        DbVO.setField76(dbDo.getField76());
        DbVO.setField77(dbDo.getField77());
        DbVO.setField78(dbDo.getField78());
        DbVO.setField79(dbDo.getField79());
        DbVO.setField80(dbDo.getField80());
        DbVO.setField81(dbDo.getField81());
        DbVO.setField82(dbDo.getField82());
        DbVO.setField83(dbDo.getField83());
        DbVO.setField84(dbDo.getField84());
        DbVO.setField85(dbDo.getField85());
        DbVO.setField86(dbDo.getField86());
        DbVO.setField87(dbDo.getField87());
        DbVO.setField88(dbDo.getField88());
        DbVO.setField89(dbDo.getField89());
        DbVO.setField90(dbDo.getField90());
        DbVO.setField91(dbDo.getField91());
        DbVO.setField92(dbDo.getField92());
        DbVO.setField93(dbDo.getField93());
        DbVO.setField94(dbDo.getField94());
        DbVO.setField95(dbDo.getField95());
        DbVO.setField96(dbDo.getField96());
        DbVO.setField97(dbDo.getField97());
        DbVO.setField98(dbDo.getField98());
        DbVO.setField99(dbDo.getField99());
        DbVO.setField100(dbDo.getField100());
        return DbVO;
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
    public DbVO testRpasBeanUtils(GenerateModel generateModel, RpasBeanUtilsInit init) throws Exception {
        DbVO dbVO = new DbVO();
        init.copier.copy(generateModel.dbDo, dbVO, null);
        return dbVO;
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
    public DbVO testMapStruct(GenerateModel generateModel, MapStructInit init) throws Exception {
        DbVO DbVO = init.mapStructMapper.copy(generateModel.dbDo);
        return DbVO;
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
    public DbVO testBeanCopier(GenerateModel generateModel, BeanCopierInit beanCopier) throws Exception {
        BeanCopier copier = beanCopier.copier;
        DbVO DbVO = new DbVO();
        copier.copy(generateModel.dbDo, DbVO, null);
        return DbVO;
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
    public DbVO testJackSon(GenerateModel generateModel, ObjectMapperInit init) throws Exception {
        String str = init.objectMapper.writeValueAsString(generateModel.dbDo);
        DbVO dbVO = init.objectMapper.readValue(str, DbVO.class);
        return dbVO;
    }


    /**
     * FastJson基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DbVO testFastJson(GenerateModel generateModel) throws Exception {
        String str = JSON.toJSONString(generateModel.dbDo);
        return JSON.parseObject(str, DbVO.class);
    }

    /**
     * Hutool BeanUtil基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DbVO testHutoolBeanUtil(GenerateModel generateModel) throws Exception {
        DbVO DbVO = new DbVO();
        BeanUtil.copyProperties(generateModel.dbDo, DbVO);
        return DbVO;
    }

    /**
     * Hutool CglibUtil基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DbVO testHutoolCglibUtil(GenerateModel generateModel, HutoolCglibInit init) throws Exception {
        DbVO dbVO = new DbVO();
        init.copier.copy(generateModel.dbDo, dbVO, null);
        return dbVO;
    }

    /**
     * SpringBeanUtils基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DbVO testSpringBeanUtils(GenerateModel generateModel) throws Exception {
        DbVO DbVO = new DbVO();
        BeanUtils.copyProperties(generateModel.dbDo, DbVO);
        return DbVO;
    }

    /**
     * Apache BeanUtils基准测试
     *
     * @param generateModel source
     * @return target
     * @throws Exception
     */
    @Benchmark
    public DbVO testApacheBeanUtils(GenerateModel generateModel) throws Exception {
        DbVO DbVO = new DbVO();
        org.apache.commons.beanutils.BeanUtils.copyProperties(DbVO, generateModel.dbDo);
        return DbVO;
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
    public DbVO testOrikaMapper(GenerateModel generateModel, OrikaMapper orikaMapper) throws Exception {
        // MapperFacade对象始终是同一个,不需要重复创建,减少创建开销,公平测试
        MapperFacade mapperFacade = orikaMapper.mapperFactory.getMapperFacade();
        DbVO DbVO = mapperFacade.map(generateModel.dbDo, DbVO.class);
        return DbVO;
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
    public DbVO testDozerMapping(GenerateModel generateModel, DozerMapper dozerMapper) throws Exception {
        DbVO DbVO = dozerMapper.mapper.map(generateModel.dbDo, DbVO.class);
        return DbVO;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BenchmarkTestComplex.class.getSimpleName())
                .result("result2.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }

}
