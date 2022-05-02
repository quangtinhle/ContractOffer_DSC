package de.fraunhofer.isst.dawid.contractoffer_dsc.controller;

import de.fraunhofer.iese.ids.odrl.policy.library.model.Condition;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.convert.RecieverPreferenceConvert;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.Constraint;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.ContractInformation;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.ConsumerService;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.ContractService;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.PolicyService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

@RestController
public class IDSPolicyRestController {

    private PolicyService policyService;
    private ContractService contractService;
    private List<Hashtable> policyList;
    private List<String> rulesLocationList;


    @GetMapping("/api/ids")
    public String getDefault() {
        return "Hello World";
    }

    @PostMapping("/api/ids/contract")
    public String getContractOffer(@RequestBody RecieverPreference recieverPreference) {

        Constraint constraint = recieverPreference.getConstraints();
        policyService = new PolicyService(constraint);
        //conditionList = policyService.getListPolicyPattern();
        policyList = policyService.getPolicyList();
        contractService = new ContractService(policyList, recieverPreference);
        //rulesLocationList = contractService.getLocationRule();

        String contract = contractService.getContractOfferProvider();
        return contract;
    }

    @PostMapping("/api/ids/agrrement")
    public String getContractAgreement(@RequestParam String recipient, @RequestParam String catalog) {
        ConsumerService consumerService = ConsumerService.getInstance(recipient);
        return consumerService.getContractAgreement(catalog);
    }

    @GetMapping("api/ids/resourcecatalog")
    public String getResourceCatalog(@RequestParam String recipient)
    {
        ConsumerService consumerService = ConsumerService.getInstance(recipient);
        return consumerService.getProviderResourceCatalog();
    }
}
