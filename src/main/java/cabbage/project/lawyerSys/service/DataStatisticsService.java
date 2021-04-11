package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.vo.CompanyMathVo;
import cabbage.project.lawyerSys.vo.LawyerMathVo;
import cabbage.project.lawyerSys.vo.ServiceMathVo;

import java.util.List;

public interface DataStatisticsService {
  ServiceMathVo serviceMath(String startDate, String endDate);

  List<CompanyMathVo> companyMath(String startDate, String endDate);

  List<LawyerMathVo> lawyerMath(String startDate, String endDate);
}
