package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.service.DataStatisticsService;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import cabbage.project.lawyerSys.vo.CompanyMathVo;
import cabbage.project.lawyerSys.vo.LawyerMathVo;
import cabbage.project.lawyerSys.vo.ServiceMathVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dataStatistics")
public class DataStatisticsConteoller {

  @Autowired
  private DataStatisticsService dataStatisticsService;


  @RequestMapping("/service")
  public R serviceMath(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
    ServiceMathVo result = dataStatisticsService.serviceMath(startDate, endDate);
    return R.ok().put("ServiceMath", result);
  }

  @RequestMapping("/company")
  public R companyMath(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
    List<CompanyMathVo> result = dataStatisticsService.companyMath(startDate, endDate);
    return R.ok().put("CompanyMath", result);
  }

  @RequestMapping("/lawyer")
  public R lawyerMath(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
    List<LawyerMathVo> result = dataStatisticsService.lawyerMath(startDate, endDate);
    return R.ok().put("LawyerMath", result);
  }
}
