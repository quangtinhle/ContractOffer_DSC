package de.fraunhofer.isst.dawid.contractoffer_dsc.controller;

import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.Constraint;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.output.agrrement.Agrrement;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.ConsumerService;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.ContractService;
import de.fraunhofer.isst.dawid.contractoffer_dsc.service.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getDefault() {
        return new ResponseEntity<String>("Contract Service is running...",HttpStatus.OK);
    }

    @PostMapping(value = "/api/ids/contract", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getContractOffer(@RequestBody RecieverPreference recieverPreference) {

        Constraint constraint = recieverPreference.getConstraints();
        policyService = new PolicyService(constraint);
        //conditionList = policyService.getListPolicyPattern();
        policyList = policyService.getPolicyList();
        contractService = new ContractService(policyList, recieverPreference);
        rulesLocationList = contractService.getLocationRule();
        String contract = contractService.getContractOfferProvider();
        return new ResponseEntity<String>(contract, HttpStatus.CREATED);
    }

    @PostMapping("/api/ids/agrrement")
    public ResponseEntity<Agrrement> getContractAgreement(@RequestParam String recipient, @RequestParam String catalog) {
        consumerService = ConsumerService.getInstance(recipient);

        return new ResponseEntity<Agrrement>(consumerService.getContractAgreement(catalog),HttpStatus.CREATED);
    }

    @GetMapping(value = "api/ids/resourcecatalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResourceCatalog(@RequestParam String recipient)
    {
        consumerService = ConsumerService.getInstance(recipient);
        String response = consumerService.getProviderResourceCatalog();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
