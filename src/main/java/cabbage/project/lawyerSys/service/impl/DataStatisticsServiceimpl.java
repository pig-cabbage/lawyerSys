package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.*;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class DataStatisticsServiceimpl implements DataStatisticsService {
  @Autowired
  private ServiceLevelService serviceLevelService;
  @Autowired
  private ServicePlanService servicePlanService;
  @Autowired
  private ProjectBaseService projectBaseService;
  @Autowired
  private UserCompanyService userCompanyService;
  @Autowired
  private UserLawyerService userLawyerService;
  @Autowired
  private StatisticalLawyerService statisticalLawyerService;

  @Override
  public ServiceMathVo serviceMath(String startDates, String endDates) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    try {
      Date startDate = sdf.parse(startDates);
      Date endDate = sdf.parse(endDates);
      List<ServiceLevelEntity> levelList = serviceLevelService.list();
      AtomicLong number = new AtomicLong();
      AtomicLong date = new AtomicLong(0L);
      final BigDecimal[] cost = {new BigDecimal(0)};
      AtomicReference<List<ServiceLevelMathVo>> collect1 = new AtomicReference<>();
      Assert.isNotNull(levelList, list -> collect1.set(list.stream().map(level -> {
        AtomicLong levelNumber = new AtomicLong(0L);
        AtomicLong levelDate = new AtomicLong(0L);
        final BigDecimal[] levelCost = {new BigDecimal(0)};
        List<ServicePlanEntity> planEntityListBefore = servicePlanService.list(new QueryWrapper<ServicePlanEntity>().eq("service_level", level.getId()));
        AtomicReference<List<ServicePlanMathVo>> collect = new AtomicReference<>();
        Assert.isNotNull(planEntityListBefore, planEntityList -> collect.set(planEntityList.stream().map(servicePlanEntity -> {
          List<ProjectBaseEntity> baseEntityListBefore = projectBaseService.list(new QueryWrapper<ProjectBaseEntity>().eq("plan", servicePlanEntity.getId()).between("start_time", startDate, endDate));
          AtomicLong planNumber = new AtomicLong(0L);
          AtomicLong planDate = new AtomicLong(0L);
          final BigDecimal[] planCost = {new BigDecimal(0L)};
          Assert.isNotNull(baseEntityListBefore, baseEntityList -> {
            planNumber.set(baseEntityListBefore.size());
            baseEntityList.forEach(item -> {
              long spendTime = ((Math.min(endDate.getTime(), item.getEndTime().getTime())) - item.getStartTime().getTime()) / SystemConstant.MS_OF_DAY;
              planDate.addAndGet(spendTime);
              planCost[0] = planCost[0].add(new BigDecimal(spendTime).divide(new BigDecimal(SystemConstant.MONTH_DAY), 5, RoundingMode.HALF_DOWN).multiply(level.getChargeStandard()));
            });
          });
          levelNumber.addAndGet(planNumber.get());
          levelDate.addAndGet(planDate.get());
          levelCost[0] = levelCost[0].add(planCost[0], new MathContext(5));
          return ServicePlanMathVo.builder().planId(servicePlanEntity.getId()).planName(servicePlanEntity.getName()).number(planNumber.get()).date(planDate.get()).cost(planCost[0]).build();
        }).collect(Collectors.toList())));
        number.addAndGet(levelNumber.get());
        date.addAndGet(levelDate.get());
        cost[0] = cost[0].add(levelCost[0], new MathContext(5));
        return ServiceLevelMathVo.builder().levelId(level.getId()).levelName(level.getLevel() + "级服务").number(levelNumber.get()).date(levelDate.get()).cost(levelCost[0]).list(collect.get()).build();
      }).collect(Collectors.toList())));
      return ServiceMathVo.builder().totalNumber(number.get()).totalDate(date.get()).totalCost(cost[0]).list(collect1.get()).build();
    } catch (ParseException e) {
      throw RunException.builder().code(ExceptionCode.DATE_TRANS_WRONG).build();
    }


  }

  @Override
  public List<CompanyMathVo> companyMath(String startDates, String endDates) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    try {
      Date startDate = sdf.parse(startDates);
      Date endDate = sdf.parse(endDates);
      List<UserCompanyEntity> list1 = userCompanyService.list();
      AtomicReference<List<CompanyMathVo>> result = new AtomicReference<>();
      Assert.isNotNull(list1, list -> result.set(list.stream().map(item -> {
        List<ProjectBaseEntity> baseEntityList = projectBaseService.list(new QueryWrapper<ProjectBaseEntity>().eq("company", item.getAccount()).between("start_time", startDate, endDate));
        long companyNumber = baseEntityList.size();
        final AtomicLong companyDate = new AtomicLong(0L);
        final BigDecimal[] companyCost = {new BigDecimal(0L)};
        baseEntityList.forEach(entity -> {
          if (endDate.getTime() > entity.getEndTime().getTime()) {
            companyDate.addAndGet((entity.getEndTime().getTime() - entity.getStartTime().getTime()) / SystemConstant.MS_OF_DAY);
            companyCost[0] = companyCost[0].add(entity.getCost(), new MathContext(5));
          } else {
            companyDate.addAndGet((endDate.getTime() - entity.getStartTime().getTime()) / SystemConstant.MS_OF_DAY);
            companyCost[0] = companyCost[0].add(new BigDecimal(endDate.getTime() - entity.getStartTime().getTime()).divide(new BigDecimal(entity.getEndTime().getTime() - entity.getStartTime().getTime()), 5, RoundingMode.HALF_DOWN).multiply(entity.getCost()));
          }

        });
        return CompanyMathVo.builder().accountId(item.getAccount()).name(item.getName()).number(companyNumber).date(companyDate.get()).cost(companyCost[0]).build();
      }).collect(Collectors.toList())));
      return result.get();
    } catch (ParseException e) {
      throw RunException.builder().code(ExceptionCode.DATE_TRANS_WRONG).build();
    }

  }

  @Override
  public List<LawyerMathVo> lawyerMath(String startDates, String endDates) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    try {
      Date startDate = sdf.parse(startDates);
      Date endDate = sdf.parse(endDates);
      List<UserLawyerEntity> listBefore = userLawyerService.list();
      Date date = new Date();
      AtomicReference<List<LawyerMathVo>> result = new AtomicReference<>();
      Assert.isNotNull(listBefore, list -> result.set(list.stream().map(lawyer -> {
        List<StatisticalLawyerEntity> recordsBefore = statisticalLawyerService.list(new QueryWrapper<StatisticalLawyerEntity>().eq("lawyer", lawyer.getAccount()).between("start_time", startDate, endDate));
        AtomicLong totalNumber = new AtomicLong();
        final AtomicLong totalDate = new AtomicLong(0L);
        final BigDecimal[] totalCost = {new BigDecimal(0L)};
        Assert.isNotNull(recordsBefore, records -> {
          totalNumber.addAndGet(records.size());
          records.forEach(record -> {
            long spendTime;
            if (record.getEndTime() == null) {
              if (date.getTime() > endDate.getTime()) {
                spendTime = endDate.getTime() - record.getStartTime().getTime();
              } else {
                if (date.getTime() > record.getStartTime().getTime()) {
                  spendTime = date.getTime() - record.getStartTime().getTime();
                } else {
                  spendTime = 0L;
                }
              }
              totalCost[0] = totalCost[0].add(new BigDecimal(spendTime).divide(BigDecimal.valueOf((long) SystemConstant.MS_OF_DAY * SystemConstant.MONTH_DAY), 5, RoundingMode.HALF_DOWN).multiply(serviceLevelService.getById(servicePlanService.getById(record.getPlan()).getServiceLevel()).getChargeStandard()));
            } else {
              spendTime = (Math.min(endDate.getTime(), record.getEndTime().getTime())) - record.getStartTime().getTime();
              if (endDate.getTime() > record.getEndTime().getTime()) {
                totalCost[0] = totalCost[0].add(BigDecimal.valueOf(record.getCost()));
              } else {
                totalCost[0] = totalCost[0].add(new BigDecimal(endDate.getTime() - record.getStartTime().getTime()).divide(BigDecimal.valueOf((long) SystemConstant.MS_OF_DAY * SystemConstant.MONTH_DAY), 5, RoundingMode.HALF_DOWN).multiply(serviceLevelService.getById(servicePlanService.getById(record.getPlan()).getServiceLevel()).getChargeStandard()));

              }

            }
            spendTime /= SystemConstant.MS_OF_DAY;
            totalDate.addAndGet(spendTime);
          });
        });
        return LawyerMathVo.builder().name(lawyer.getName()).account(lawyer.getAccount()).totalNumber(totalNumber.get()).totalDate(totalDate.get()).totalCost(totalCost[0]).build();
      }).collect(Collectors.toList())));
      return result.get();
    } catch (ParseException e) {
      throw RunException.builder().code(ExceptionCode.DATE_TRANS_WRONG).build();
    }

  }

  @Override
  public List<LawyerMathDetailVo> lawyerMathDetail(String account, String startDates, String endDates) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    try {
      Date startDate = sdf.parse(startDates);
      Date endDate = sdf.parse(endDates);
      Date date = new Date();
      Map<String, LawyerMathDetailVo> temp = new HashMap<>();
      List<StatisticalLawyerEntity> list = statisticalLawyerService.list(new QueryWrapper<StatisticalLawyerEntity>().eq("lawyer", account).between("start_time", startDate, endDate));
      Assert.isNotEmpty(list, list1 -> {
        list1.stream().forEach(item -> {
          long spendTime;
          BigDecimal cost = new BigDecimal(0L);
          if (item.getEndTime() == null) {
            if (date.getTime() > endDate.getTime()) {
              spendTime = endDate.getTime() - item.getStartTime().getTime();
            } else {
              if (date.getTime() > item.getStartTime().getTime()) {
                spendTime = date.getTime() - item.getStartTime().getTime();
              } else {
                spendTime = 0L;
              }
            }
            cost = (new BigDecimal(spendTime).divide(BigDecimal.valueOf((long) SystemConstant.MS_OF_DAY * SystemConstant.MONTH_DAY), 5, RoundingMode.HALF_DOWN).multiply(serviceLevelService.getById(servicePlanService.getById(item.getPlan()).getServiceLevel()).getChargeStandard()));
          } else {
            spendTime = (Math.min(endDate.getTime(), item.getEndTime().getTime())) - item.getStartTime().getTime();
            if (endDate.getTime() > item.getEndTime().getTime()) {
              cost = (BigDecimal.valueOf(item.getCost()));
            } else {
              cost = (new BigDecimal(endDate.getTime() - item.getStartTime().getTime()).divide(BigDecimal.valueOf((long) SystemConstant.MS_OF_DAY * SystemConstant.MONTH_DAY), 5, RoundingMode.HALF_DOWN).multiply(serviceLevelService.getById(servicePlanService.getById(item.getPlan()).getServiceLevel()).getChargeStandard()));
            }

          }
          spendTime /= SystemConstant.MS_OF_DAY;
          if (temp.containsKey(item.getCompany())) {
            LawyerMathDetailVo lawyerMathDetailVo = temp.get(item.getCompany());
            lawyerMathDetailVo.setTotalNumber(lawyerMathDetailVo.getTotalNumber() + 1);
            lawyerMathDetailVo.setTotalDate(lawyerMathDetailVo.getTotalDate() + spendTime);
            lawyerMathDetailVo.setTotalCost(lawyerMathDetailVo.getTotalCost().add(cost));
            temp.put(item.getCompany(), lawyerMathDetailVo);
          } else {
            LawyerMathDetailVo lawyerMathDetailVo = null;
            lawyerMathDetailVo.setName(item.getCompanyName());
            lawyerMathDetailVo.setAccount(item.getCompany());
            lawyerMathDetailVo.setTotalNumber(1L);
            lawyerMathDetailVo.setTotalDate(spendTime);
            lawyerMathDetailVo.setTotalCost(cost);
            temp.put(item.getCompany(), lawyerMathDetailVo);
          }
        });
      });
      return new ArrayList<>(temp.values());
    } catch (ParseException e) {
      throw RunException.builder().code(ExceptionCode.DATE_TRANS_WRONG).build();
    }
  }

}
