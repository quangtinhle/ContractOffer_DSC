package util;

import de.fraunhofer.iese.ids.odrl.policy.library.model.Condition;
import de.fraunhofer.iese.ids.odrl.policy.library.model.OdrlPolicy;
import de.fraunhofer.iese.ids.odrl.policy.library.model.enums.RuleType;

public class OdrlCreator {

    public static String createODRL(OdrlPolicy policy){

        OdrlPolicy updatedPolicy = updateRightOperandValue(policy);
        return updatedPolicy.toString();
    }

    private static OdrlPolicy updateRightOperandValue(OdrlPolicy odrlPolicy)
    {
        if(odrlPolicy.getRules().get(0).getType().equals(RuleType.OBLIGATION))
        {
            if(null != odrlPolicy.getRules().get(0).getAction().getRefinements()) {
                for (Condition refinement : odrlPolicy.getRules().get(0).getAction().getRefinements()) {
                    updateToIDSValue(refinement);
                }
            }
        }

        if(odrlPolicy.getRules().get(0).getType().equals(RuleType.PERMISSION))
        {
            if(null != odrlPolicy.getRules().get(0).getConstraints()) {
                for (Condition constraint : odrlPolicy.getRules().get(0).getConstraints()) {
                    updateToIDSValue(constraint);
                }
            }
            if(null != odrlPolicy.getRules().get(0).getPostduties())
            {
                if(null != odrlPolicy.getRules().get(0).getPostduties().get(0).getAction().getRefinements())
                {
                    for (Condition refinement : odrlPolicy.getRules().get(0).getPostduties().get(0).getAction().getRefinements()) {
                        updateToIDSValue(refinement);
                    }
                }
            }
        }



        return odrlPolicy;
    }

    private static void updateToIDSValue(Condition condition) {
		/*if (condition.getLeftOperand().equals(LeftOperand.DELAY) || condition.getLeftOperand().equals(LeftOperand.ELAPSED_TIME) ) {
			if (null != condition.getRightOperand().getEntities()) {
				for (RightOperandEntity entity : condition.getRightOperand().getEntities()) {
					if (entity.getEntityType().equals(EntityType.HASDURATION)) {
						String value = entity.getValue();
						if (value != null && !value.isEmpty()) {
							String timeUnit = "";
							String xsdPrefix = "";

							switch (entity.getTimeUnit()) {
								case HOURS:
									timeUnit = TimeUnit.HOURS.getOdrlXsdDuration();
									xsdPrefix = "T";
									break;

								case DAYS:
									timeUnit = TimeUnit.DAYS.getOdrlXsdDuration();
									break;

								case MONTHS:
									timeUnit = TimeUnit.MONTHS.getOdrlXsdDuration();
									break;

								case YEARS:
									timeUnit = TimeUnit.YEARS.getOdrlXsdDuration();
									break;

							}
							entity.setValue("P" + xsdPrefix + value + timeUnit);
						}
					}
				}

			}
		}else */
/*			if (condition.getLeftOperand().equals(LeftOperand.LOG_LEVEL) || condition.getLeftOperand().equals(LeftOperand.NOTIFICATION_LEVEL))
		{
			String value = condition.getRightOperand().getValue();
			if (value != null && !value.isEmpty()) {
				condition.getRightOperand().setValue(LogLevelType.valueOf(value).getType());
			}
		}else if (condition.getLeftOperand().equals(LeftOperand.ARTIFACT_STATE))
		{
			String value = condition.getRightOperand().getValue();
			if (value != null && !value.isEmpty()) {
				condition.getRightOperand().setValue(ArtifactStateType.valueOf(value).getType());
			}
		}*/
    }
}
