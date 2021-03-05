package cabbage.project.lawyerSys.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

  private Set<Integer> set = new HashSet<>();

  @Override
  public void initialize(ListValue constraintAnnotation) {
    set = Arrays.stream(constraintAnnotation.vals()).boxed().collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
    return set.contains(integer);
  }
}
