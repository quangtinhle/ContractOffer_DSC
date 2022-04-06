package de.fraunhofer.isst.dawid.contractoffer_dsc.controller;

import de.fraunhofer.iese.ids.odrl.policy.library.model.Condition;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.Constraint;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

@RestController
public class IDSPolicyRestController {

    private PolicyService policyService;
    private List<Hashtable> policyList;
    private List<Condition> conditionList;

    @PostMapping("/contractoffer")
    public String getContractOffer(@RequestBody RecieverPreference recieverPreference) {

        Constraint constraint = recieverPreference.getConstraints();
        policyService = new PolicyService(constraint);
        //conditionList = policyService.getListPolicyPattern();
        policyList = policyService.getPolicyList();
        for (Hashtable a: policyList
             ) {
            System.out.println(a);

        }
        return recieverPreference.getPreferenceUUID();
    }
}
