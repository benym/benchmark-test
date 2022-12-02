package com.benym.benchmark.test.service;


import com.benym.benchmark.test.model.complex.DbDO;
import com.benym.benchmark.test.model.complex.MockOne;
import com.benym.benchmark.test.model.complex.MockTwo;
import com.benym.benchmark.test.model.simple.DataBaseDO;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * @author: benym
 */
public class ModelService {

    public DataBaseDO get(){
        DataBaseDO dataBaseDo = new DataBaseDO();
        dataBaseDo.setAge(1);
        dataBaseDo.setName("2131");
        dataBaseDo.setTime(new Date());
        dataBaseDo.setOtherTime(new Date());
        dataBaseDo.setYear(2L);
        return dataBaseDo;
    }
    
    public DbDO getComplex(){
        DbDO dbModel = new DbDO();
        dbModel.setAge(1);
        dbModel.setName("2131");
        dbModel.setTime(new Date());
        dbModel.setOtherTime(new Date());
        dbModel.setYear(2L);

        MockOne mockOne = new MockOne();
        mockOne.setId(UUID.randomUUID().toString().replace("-", ""));
        mockOne.setName("2311231231");
        mockOne.setParentId("1");
        mockOne.setNo("3213131231");
        mockOne.setLevel(1);
        mockOne.setAddress("132213123");
        mockOne.setOrderNo(0);
        MockOne mockModelOne2 = new MockOne();
        mockModelOne2.setId("32123123123");
        mockModelOne2.setName("42141241231");
        mockModelOne2.setParentId(mockModelOne2.getId());
        mockModelOne2.setNo("1212121");
        mockModelOne2.setLevel(1);
        mockModelOne2.setAddress("12112");
        mockModelOne2.setOrderNo(0);
        mockModelOne2.setChildren(Collections.singletonList(mockModelOne2));
        dbModel.setMockModelOne(mockOne);

        MockTwo mockTwo = new MockTwo();
        mockTwo.setId("9999");
        mockTwo.setName("11111");
        mockTwo.setParentId("22222");
        mockTwo.setNo("33333");
        mockTwo.setLevel(1);
        mockTwo.setOrderNo(0);
        dbModel.setMockModelTwo(mockTwo);
        
        dbModel.setField00("field00");
        dbModel.setField01("field01");
        dbModel.setField02("field02");
        dbModel.setField03("field03");
        dbModel.setField04("field04");
        dbModel.setField05("field05");
        dbModel.setField06("field06");
        dbModel.setField07("field07");
        dbModel.setField08("field08");
        dbModel.setField09("field09");
        dbModel.setField10("field10");
        dbModel.setField11("field11");
        dbModel.setField12("field12");
        dbModel.setField13("field13");
        dbModel.setField14("field14");
        dbModel.setField15("field15");
        dbModel.setField16("field16");
        dbModel.setField17("field17");
        dbModel.setField18("field18");
        dbModel.setField19("field19");
        dbModel.setField20("field20");
        dbModel.setField21("field21");
        dbModel.setField22("field22");
        dbModel.setField23("field23");
        dbModel.setField24("field24");
        dbModel.setField25("field25");
        dbModel.setField26("field26");
        dbModel.setField27("field27");
        dbModel.setField28("field28");
        dbModel.setField29("field29");
        dbModel.setField30("field30");
        dbModel.setField31("field31");
        dbModel.setField32("field32");
        dbModel.setField33("field33");
        dbModel.setField34("field34");
        dbModel.setField35("field35");
        dbModel.setField36("field36");
        dbModel.setField37("field37");
        dbModel.setField38("field38");
        dbModel.setField39("field39");
        dbModel.setField40("field40");
        dbModel.setField41("field41");
        dbModel.setField42("field42");
        dbModel.setField43("field43");
        dbModel.setField44("field44");
        dbModel.setField45("field45");
        dbModel.setField46("field46");
        dbModel.setField47("field47");
        dbModel.setField48("field48");
        dbModel.setField49("field49");
        dbModel.setField50("field50");
        dbModel.setField51("field51");
        dbModel.setField52("field52");
        dbModel.setField53("field53");
        dbModel.setField54("field54");
        dbModel.setField55("field55");
        dbModel.setField56("field56");
        dbModel.setField57("field57");
        dbModel.setField58("field58");
        dbModel.setField59("field59");
        dbModel.setField60("field60");
        dbModel.setField61("field61");
        dbModel.setField62("field62");
        dbModel.setField63("field63");
        dbModel.setField64("field64");
        dbModel.setField65("field65");
        dbModel.setField66("field66");
        dbModel.setField67("field67");
        dbModel.setField68("field68");
        dbModel.setField69("field69");
        dbModel.setField70("field70");
        dbModel.setField71("field71");
        dbModel.setField72("field72");
        dbModel.setField73("field73");
        dbModel.setField74("field74");
        dbModel.setField75("field75");
        dbModel.setField76("field76");
        dbModel.setField77("field77");
        dbModel.setField78("field78");
        dbModel.setField79("field79");
        dbModel.setField80("field80");
        dbModel.setField81("field81");
        dbModel.setField82("field82");
        dbModel.setField83("field83");
        dbModel.setField84("field84");
        dbModel.setField85("field85");
        dbModel.setField86("field86");
        dbModel.setField87("field87");
        dbModel.setField88("field88");
        dbModel.setField89("field89");
        dbModel.setField90("field90");
        dbModel.setField91("field91");
        dbModel.setField92("field92");
        dbModel.setField93("field93");
        dbModel.setField94("field94");
        dbModel.setField95("field95");
        dbModel.setField96("field96");
        dbModel.setField97("field97");
        dbModel.setField98("field98");
        dbModel.setField99("field99");
        dbModel.setField100("field99");
        return dbModel;
    }
}
