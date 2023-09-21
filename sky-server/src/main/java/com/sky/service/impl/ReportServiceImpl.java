package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end) {
//        //当前集合用于存放从begin.到end范围内的每天的日期
//        List<LocalDate> dateList = new ArrayList<>();
//
//        dateList.add(begin);
//        while (!begin.equals(end)){
//            //目期计算，计算指定日期的后一天对应的日期
//            begin = begin.plusDays(1);
//            dateList.add(begin);
//        }
        List<LocalDate> dateList = getDateList(begin, end);

        List<Double> turnOverList = new ArrayList<>();
        dateList.forEach(date ->{
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Double sumOrder = orderMapper.getOrderByTime(beginTime, endTime);
            turnOverList.add(sumOrder==null ? 0 : sumOrder);
        });

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnOverList,","))
                .build();
    }

    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        dateList.forEach(date ->{
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer totalUserSize = userMapper.getUserSize(null,endTime);
            totalUserList.add(totalUserSize);
            Integer newUserSize = userMapper.getUserSize(beginTime, endTime);
            newUserList.add(newUserSize);

        });


        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        LocalDateTime beginTimes = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTimes = LocalDateTime.of(end, LocalTime.MAX);

        Integer countTotalOrderCount = orderMapper.getOrderReport(beginTimes,endTimes,null);
        Integer countValidOrderCount = orderMapper.getOrderReport(beginTimes,endTimes,5);
        Double orderCompletionRate = countTotalOrderCount==0 ? 0 : ((double)countValidOrderCount)/ ((double) countTotalOrderCount);

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        dateList.forEach(date ->{
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer countOrderCountList = orderMapper.getOrderReport(beginTime,endTime,null);
            Integer countValidOrderCountList = orderMapper.getOrderReport(beginTime,endTime,5);
            orderCountList.add(countOrderCountList);
            validOrderCountList.add(countValidOrderCountList);
        });
        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(countTotalOrderCount)
                .validOrderCount(countValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTimes = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTimes = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> top10List = orderMapper.getTop10(beginTimes,endTimes);
//        List<String> nameList = new ArrayList<>();
//        List<String> numberList = new ArrayList<>();
//        for (GoodsSalesDTO salesTop10 : top10List) {
//            nameList.add(salesTop10.getName());
//            numberList.add(String.valueOf(salesTop10.getNumber()));
//        }
        List<String> nameList = top10List.stream().map(GoodsSalesDTO::getName).toList();
        List<Integer> numberList = top10List.stream().map(GoodsSalesDTO::getNumber).toList();

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }


    public List<LocalDate> getDateList(LocalDate begin, LocalDate end){
        //当前集合用于存放从begin.到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)){
            //目期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
