package com.xiaodao.office.work.fake;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Creator {

    public static void main(String[] args) {
//        读入所有人员数据并生成输入数据
        List<PersonDTO> personDTOList = new ArrayList<>();
        EasyExcel.read("E:\\tmp\\人事干部-人事管理干部.xlsx",
                PersonDTO.class, new PersonDTOListener(personDTOList)).doReadAll();

        create(personDTOList);

    }

    public static void create(List<PersonDTO> personDTOList) {

        LocalDate start = LocalDate.of(2023, 1, 1);


        String fileName = System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        for (int j = 1; j <= 8; j++) {
            LocalDate monthEnd = start.plusMonths(1);
            List<OutputDTO> monthOutput = new ArrayList<>();
            int i = 0;
            while (start.isBefore(monthEnd)) {
                LocalDate finalStart = start;
                PersonDTO personDTO = personDTOList.get(RandomUtil.randomInt(0, personDTOList.size() - 1));
                OutputDTO outputDTO = new OutputDTO();
                outputDTO.setIndex(String.valueOf(++i));
                outputDTO.setName(personDTO.getName());
                outputDTO.setUnit(personDTO.getUnit());
                outputDTO.setPhone(personDTO.getPhone());
                outputDTO.setTime(finalStart.toString());
                monthOutput.add(outputDTO);
                start = start.plusDays(1);
            }
            String sheetName = j + "月份";
            excelWriter.write(monthOutput, EasyExcel.writerSheet(j,sheetName).build());
        }
        excelWriter.finish();


    }
}
