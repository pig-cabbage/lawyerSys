package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ConstantTodoItemDao;
import cabbage.project.lawyerSys.entity.ConstantTodoItemEntity;
import cabbage.project.lawyerSys.service.ConstantTodoItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("constantTodoItemService")
public class ConstantTodoItemServiceImpl extends ServiceImpl<ConstantTodoItemDao, ConstantTodoItemEntity> implements ConstantTodoItemService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ConstantTodoItemEntity> page = this.page(
        new Query<ConstantTodoItemEntity>().getPage(params),
        new QueryWrapper<ConstantTodoItemEntity>()
    );

    return new PageUtils(page);
  }

}