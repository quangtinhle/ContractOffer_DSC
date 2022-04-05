package de.fraunhofer.isst.dawid.contractoffer_dsc.service;

import de.fraunhofer.iese.ids.odrl.policy.library.model.Condition;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.Constraint;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class PolicyService {

    private Constraint constraints;

    public PolicyService(Constraint constraintInput) {
        this.constraints = constraintInput;
    }

    public List<Condition> getListPolicyPattern() {

        JsonIDSConverter converter = new JsonIDSConverter(constraints);
        List<Condition> conditionList = converter.getConstraintList();
        //System.out.println(converter.createPolicy());
        return conditionList;

    }

    public List<Hashtable> getPolicyList() {
        List<Hashtable> listPolicy = new ArrayList<>();
        JsonIDSConverter converter = new JsonIDSConverter(constraints);
        listPolicy = converter.getPolicylist();
        return listPolicy;
    }

}
