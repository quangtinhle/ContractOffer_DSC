package de.fraunhofer.isst.dawid.contractoffer_dsc.service;

import de.fraunhofer.iese.ids.odrl.policy.library.model.*;
import de.fraunhofer.iese.ids.odrl.policy.library.model.enums.*;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.Constraint;

import util.OdrlCreator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;


public class JsonIDSConverter {

    String baseUid = "http://w3id.org/idsa/autogen/contract/";

    private List<Hashtable> policylist = new ArrayList<>();
    private Constraint constraintInput;
    private List<Rule> rules = new ArrayList<>();
    private List<Condition> constraints = new ArrayList<>();
    private List<Rule> preDuties = new ArrayList<>();
    private List<Rule> postDuties = new ArrayList<>();

    public JsonIDSConverter(Constraint constraintInput) {
        this.constraintInput = constraintInput;
        Action useAction = new Action(ActionType.USE);
        Rule rule = new Rule(RuleType.PERMISSION, useAction);
        rules.add(rule);
    }

    public List<Condition> getConstraintList() {

        addConsumerCondition();
        addDataHistory();
        addLocationCondition();
        addPreDuties();
        addPurposeCondition();
        addMinCompensationCondition();
        addUsagePeriod();
        addCounterCondition();

        return constraints;
    }

    public List<Hashtable> getPolicylist() {

        addDataHistory();
        convertHashtable();
        addConsumerCondition();
        convertHashtable();
        addLocationCondition();
        convertHashtable();
        addMinCompensationCondition();
        convertHashtable();
        addIdentifiabilityCondition();
        convertHashtable();
        addUsagePeriod();
        convertHashtable();
        addPurposeCondition();
        convertHashtable();
        addWaitingTime();
        convertHashtable();
        //addCounterCondition();
        //convertHashtable();
        return policylist;
    }
    public boolean addLocationCondition() {

        if (!constraintInput.getLocation().equals("")) {
            RightOperandType rightOperandType = RightOperandType.ANYURI;
            ConditionType conditionType = ConditionType.CONSTRAINT;
            LeftOperand leftOperand = LeftOperand.ABSOLUTE_SPATIAL_POSITION;
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            RightOperand rightOperand = new RightOperand("https://ontologi.es/place/" + constraintInput.getLocation(), rightOperandType);
            rightOperands.add(rightOperand);
            Condition constraint = new Condition(conditionType, leftOperand, Operator.SAME_AS, rightOperands, null);
            constraints.add(constraint);
            return true;
        }
        return false;
    }

    public boolean addConsumerCondition() {
        if (!constraintInput.getDataConsumer().equals("")) {
            RightOperandType rightOperandType = RightOperandType.ANYURI;
            ConditionType conditionType = ConditionType.CONSTRAINT;
            LeftOperand leftOperand = LeftOperand.SYSTEM;
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            RightOperand rightOperand = new RightOperand(constraintInput.getDataConsumer(), rightOperandType);
            rightOperands.add(rightOperand);
            Condition constraint = new Condition(conditionType, leftOperand, Operator.SAME_AS, rightOperands, null);
            constraints.add(constraint);
            return true;
        }
        return false;
    }

    public boolean addPurposeCondition() {

        if (!constraintInput.getPurpose().equals("")) {
            RightOperandType rightOperandType = RightOperandType.ANYURI;
            ConditionType conditionType = ConditionType.CONSTRAINT;
            LeftOperand leftOperand = LeftOperand.PURPOSE;
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            RightOperand rightOperand = new RightOperand(constraintInput.getPurpose(), rightOperandType);
            rightOperands.add(rightOperand);
            Condition constraint = new Condition(conditionType, leftOperand, Operator.SAME_AS, rightOperands, null);
            constraints.add(constraint);
            return true;

        }
        return false;
    }

    public boolean addIdentifiabilityCondition() {

        if (!constraintInput.getIdentifiability().equals("")) {
            RightOperandType rightOperandType = RightOperandType.STRING;
            ConditionType conditionType = ConditionType.CONSTRAINT;
            LeftOperand leftOperand = LeftOperand.STATE;
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            RightOperand rightOperand = new RightOperand(constraintInput.getIdentifiability(), rightOperandType);
            rightOperands.add(rightOperand);
            Condition constraint = new Condition(conditionType, leftOperand, Operator.HAS_STATE, rightOperands, null);
            constraints.add(constraint);
            return true;

        }
        return false;
    }


    public void addPreDuties() {

        String preduties = constraintInput.getIdentifiability();
        if (preduties != "") {

            if (preduties.equals("anonymized")) {
                anonymizeInRest();
            } else {
                if (preduties.equals("pseudonymize")) {
                    pseudonymizeInRest();
                } else {
                    if (preduties.equals("allowed")) {
                        allowedInRest();
                    } else {
                        if (preduties.equals("*")) {
                            allInRest();
                        }
                    }
                }
            }
        }
    }

    private void anonymizeInRest() {
        Action anonymizeAction = new Action(ActionType.ANONYMIZE);
        Rule rule = new Rule(RuleType.OBLIGATION, anonymizeAction);
        //rule.setTarget(URI.create());
        preDuties.add(rule);
    }

    private void pseudonymizeInRest() {
        Action anonymizeAction = new Action(ActionType.PSEUDONYMIZE);
        Rule rule = new Rule(RuleType.OBLIGATION, anonymizeAction);
        //rule.setTarget(URI.create());
        preDuties.add(rule);
    }

    private void allowedInRest() {
        Action anonymizeAction = new Action(ActionType.ALLOWED);
        Rule rule = new Rule(RuleType.OBLIGATION, anonymizeAction);
        //rule.setTarget(URI.create());
        preDuties.add(rule);
    }

    private void allInRest() {
        Action anonymizeAction = new Action(ActionType.ALL);
        Rule rule = new Rule(RuleType.OBLIGATION, anonymizeAction);
        //rule.setTarget(URI.create());
        preDuties.add(rule);
    }


    /*   public boolean addPaymentCondition() {
           Compensation compensation = recieverOdrlPolicy.getCompensation();
           if (recieverOdrlPolicy.getPayment() != "") {
               String contract = "http://dbpedia.org/page/";
               RightOperand paymentRightOperand = new RightOperand(String.valueOf(recieverOdrlPolicy.getPrice()), RightOperandType.DOUBLE);
               ArrayList<RightOperand> paymentRightOperands = new ArrayList<>();
               paymentRightOperands.add(paymentRightOperand);
               Condition paymentCondition = new Condition(ConditionType.CONSTRAINT, LeftOperand.PAY_AMOUNT, Operator.EQ,
                       paymentRightOperands, null);
               paymentCondition.setUnit("http://dbpedia.org/resource/" + recieverOdrlPolicy.getUnit());
               paymentCondition.setContract(contract + recieverOdrlPolicy.getPayment());
               constraints.add(paymentCondition);
               return true;
           }
           return false;
       }*/


    public boolean addMinCompensationCondition() {
        Double minCompensation = constraintInput.getMinCompensation();
        if (minCompensation != null) {
            //String contract = "http://dbpedia.org/page/";
            RightOperand paymentRightOperand = new RightOperand(String.valueOf(minCompensation), RightOperandType.DOUBLE);
            ArrayList<RightOperand> paymentRightOperands = new ArrayList<>();
            paymentRightOperands.add(paymentRightOperand);
            Condition paymentCondition = new Condition(ConditionType.CONSTRAINT, LeftOperand.PAY_AMOUNT, Operator.EQ,
                    paymentRightOperands, null);
            //paymentCondition.setUnit("http://dbpedia.org/resource/" + compensation.getUnit());
            //paymentCondition.setContract(contract + compensation.getPayment());
            constraints.add(paymentCondition);
            return true;
        }
        return false;
    }

    private String checkIfEmptyValue(String value, String defaultValue) {
        if (value.length() > 0) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public boolean addUsagePeriod_Old() {
        ArrayList<RightOperandEntity> durationEntities = new ArrayList<>();
        RightOperand elapsedTimeRightOperand = new RightOperand();
        //elapsedTimeRightOperand.setType(RightOperandType.DURATIONENTITY);

        /*UsagePeriod usagePeriod = recieverOdrlPolicy.getUsagePeriod();
        String hour = "";
        String day = "";
        String month = "";
        String year = "";
        if (usagePeriod.getDurationHour()!= null && !usagePeriod.getDurationHour().isEmpty()) {
            hour = "T" + usagePeriod.getDurationHour() + TimeUnit.HOURS.getOdrlXsdDuration();
        }
        if (usagePeriod.getDurationDay() != null && !usagePeriod.getDurationDay().isEmpty()) {
            day = usagePeriod.getDurationDay() + TimeUnit.DAYS.getOdrlXsdDuration();
        }
        if (usagePeriod.getDurationMonth() != null && !usagePeriod.getDurationMonth().isEmpty()) {
            month = usagePeriod.getDurationMonth() + TimeUnit.MONTHS.getOdrlXsdDuration();
        }
        if (usagePeriod.getDurationYear() != null && !usagePeriod.getDurationYear().isEmpty()) {
            year = usagePeriod.getDurationYear() + TimeUnit.YEARS.getOdrlXsdDuration();
        }*/
        String duration = constraintInput.getUsagePeriod();

        if (!duration.equals("P")) {
            RightOperandEntity hasDurationEntity = new RightOperandEntity(EntityType.HASDURATION, duration,
                    RightOperandType.DURATION);
            // hasDurationEntity.setTimeUnit(TimeUnit.valueOf(restrictTimeDurationUnit));
            durationEntities.add(hasDurationEntity);
        }

        if (durationEntities.size() > 0) {
            elapsedTimeRightOperand.setEntities(durationEntities);
            ArrayList<RightOperand> elapsedTimeRightOperands = new ArrayList<>();
            elapsedTimeRightOperands.add(elapsedTimeRightOperand);
            Condition elapsedTimeConstraint = new Condition(ConditionType.CONSTRAINT, LeftOperand.ELAPSED_TIME,
                    Operator.SHORTER_EQ, elapsedTimeRightOperands, null);
            constraints.add(elapsedTimeConstraint);
            return true;
        }
        return false;

    }
    public boolean addUsagePeriod() {

        String duration = constraintInput.getUsagePeriod();
        if (duration != "") {
            RightOperand rightOperand = new RightOperand(duration, RightOperandType.DURATION);
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            rightOperands.add(rightOperand);
            Condition elapsedTimeConstrain = new Condition(ConditionType.CONSTRAINT, LeftOperand.ELAPSED_TIME, Operator.SHORTER_EQ, rightOperands, null);
            constraints.add(elapsedTimeConstrain);
            return true;
        }
        return false;
    }

    public boolean addWaitingTime() {

        String duration = constraintInput.getWaitingtime();
        if (duration != "") {
            RightOperand rightOperand = new RightOperand(duration, RightOperandType.DURATION);
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            rightOperands.add(rightOperand);
            Condition elapsedTimeConstrain = new Condition(ConditionType.CONSTRAINT, LeftOperand.DELAY, Operator.DURATION_EQ, rightOperands, null);
            constraints.add(elapsedTimeConstrain);
            return true;
        }
        return false;
    }




    public boolean addDataHistory() {
            String duration = constraintInput.getDataHistory();

            if (duration!="") {
                RightOperand rightOperand = new RightOperand(duration, RightOperandType.DURATION);
                ArrayList<RightOperand> rightOperands = new ArrayList<>();
                rightOperands.add(rightOperand);
                Condition dataHistoryTimeConstrain = new Condition(ConditionType.CONSTRAINT, LeftOperand.POLICY_EVALUATION_TIME, Operator.SHORTER_EQ, rightOperands, null);
                constraints.add(dataHistoryTimeConstrain);
                return true;
            }

        return false;

    }

    public boolean addCounterCondition() {
        if (constraintInput.getCounter() != "") {
            RightOperand rightOperand = new RightOperand(constraintInput.getCounter(), RightOperandType.DECIMAL);
            ArrayList<RightOperand> rightOperands = new ArrayList<>();
            rightOperands.add(rightOperand);
            Condition countCondition = new Condition(ConditionType.CONSTRAINT, LeftOperand.COUNT, Operator.LTEQ,
                    rightOperands, null);
            constraints.add(countCondition);
            return true;
        }
        return false;
    }

    public String createPolicy() {
        String jsonPolicyString = "";
        if(constraints.size()>0 || preDuties.size() >0) {
        String policyUID = baseUid + UUID.randomUUID();
        rules.get(0).setConstraints((ArrayList<Condition>) constraints);
        if (postDuties.size() > 0) {
            rules.get(0).setPostduties((ArrayList<Rule>) postDuties);
        }
        if (preDuties.size() > 0) {
            rules.get(0).setPreduties((ArrayList<Rule>) preDuties);
            preDuties.remove(0);
        }
        OdrlPolicy odrlPolicy = new OdrlPolicy();
        //odrlPolicy.setProvider(createProvider());
        odrlPolicy.setRules((ArrayList<Rule>) rules);
        odrlPolicy.setPolicyId(URI.create(policyUID));
        odrlPolicy.setType(PolicyType.getFromIdsString("ids:Permission"));
        //odrlPolicy.setType(PolicyType.getFromIdsString("ids:Contract" + "Offer"));
        jsonPolicyString = OdrlCreator.createODRL(odrlPolicy);
        if(constraints.size()>0) {
            constraints.removeAll(constraints);
        }
        }
        return jsonPolicyString;
    }

    private Party createProvider() {
        Party provider = null;
        try {
            provider = new Party(PartyType.PROVIDER, new URI(constraintInput.getDataConsumer()));
            provider.setType(PartyType.PROVIDER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return provider;
    }

    private void convertHashtable() {
        String json = createPolicy();
        if(json!="") {
            Hashtable<String, String> policy = new Hashtable<>();
            policy.put("value", json);
            policylist.add(policy);
            }
        }






}
