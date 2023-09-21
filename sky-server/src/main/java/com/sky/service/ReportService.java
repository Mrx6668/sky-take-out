package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnOverStatistics(LocalDate beginDate, LocalDate endDate);

    UserReportVO getUserReport(LocalDate begin, LocalDate end);
}
