package com.xiaodao.office.excel.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaodao.office.excel.dto.UploadData;
import com.xiaodao.office.excel.model.Person;
import com.xiaodao.office.excel.model.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskHandler {

    private final PersonRepository personRepository;

    private final EntityManager entityManager;

    public TaskHandler(PersonRepository personRepository, EntityManager entityManager) {
        this.personRepository = personRepository;
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = Exception.class)
    public void process(List<UploadData> dataList, Map<String, String> dic) {
        Map<String, Person> personMap = personRepository.findByIdCardInOrderByIdDesc(
                dataList.parallelStream().map(UploadData::getIdCard).filter(StrUtil::isNotBlank)
                        .collect(Collectors.toList())).stream().
                collect(Collectors.toMap(Person::getIdCard, Function.identity(), (v1, v2) -> v1));

        List<Person> savePersonList = new ArrayList<>(dataList.size());
        List<Person> updatePersonList = new ArrayList<>(personMap.size());
        List<Person> createdPersonList = null;
        int newPersonSize = dataList.size() - personMap.size();
        if (newPersonSize > 0) {
            createdPersonList = new ArrayList<>(newPersonSize);
        }else {
            createdPersonList = Collections.emptyList();
        }

        // 在这里可以循环处理分批的数据、对于错误数据返回
        for (final UploadData uploadData : dataList) {

            uploadData.setOrg(dic.getOrDefault(uploadData.getOrg(), uploadData.getOrg()));

            this.check(uploadData);

            // Person person = personMap.getOrDefault(uploadData.getIdCard(), new Person());
            Person person = personMap.get(uploadData.getIdCard());
            if (person == null){
                person =new Person();
                this.mergePerson(person, uploadData);
                createdPersonList.add(person);
            }else {
                this.mergePerson(person, uploadData);
                updatePersonList.add(person);
            }
            // savePersonList.add(person);
        }
        // personRepository.saveAll(savePersonList);
        for (final Person person : createdPersonList) {
            entityManager.persist(person);
        }
        for (final Person person : updatePersonList) {
            entityManager.merge(person);
        }
        entityManager.flush();
    }

    private void check(UploadData uploadData) {
        int age = DateUtil.ageOfNow(uploadData.getBirthday());
        // 逻辑判断
        if (age < 35) {
            uploadData.setRemark("年轻有为");
        } else if (age > 45 && age < 50) {
            uploadData.setRemark("中流砥柱");
        } else if (age > 55 && age < 60) {
            uploadData.setRemark("廉颇老矣");
        } else if (age > 60) {
            uploadData.setErrorMsg("老骥伏枥");
        } else {
            //    手动产生一个异常
            //     int i = 2/0;
        }
    }

    private void mergePerson(Person person, UploadData uploadData) {
        BeanUtil.copyProperties(uploadData, person, "remark", "errorMsg");
    }

}
