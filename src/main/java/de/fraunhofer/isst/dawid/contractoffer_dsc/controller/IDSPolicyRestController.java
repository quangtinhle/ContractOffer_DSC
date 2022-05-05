package de.fraunhofer.isst.dawid.contractoffer_dsc.controller;

import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.Constraint;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.output.agrrement.Agrrement;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.ConsumerService;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.ContractService;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;

@RestController
public class IDSPolicyRestController {

    private PolicyService policyService;
    private ContractService contractService;
    private List<Hashtable> policyList;
    private List<String> rulesLocationList;
    private ConsumerService consumerService;


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
        rulesLocationList = contractService.getLocationRule();
        String contract = contractService.getContractOfferProvider();
        return contract;
    }

    @PostMapping("/api/ids/agrrement")
    public Agrrement getContractAgreement(@RequestParam String recipient, @RequestParam String catalog) {
        consumerService = ConsumerService.getInstance(recipient);

        return consumerService.getContractAgreement(catalog);
    }

    @GetMapping("api/ids/resourcecatalog")
    public String getResourceCatalog(@RequestParam String recipient)
    {
        consumerService = ConsumerService.getInstance(recipient);
        return consumerService.getProviderResourceCatalog();
    }
}
