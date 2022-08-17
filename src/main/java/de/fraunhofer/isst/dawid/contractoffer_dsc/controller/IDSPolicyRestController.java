package de.fraunhofer.isst.dawid.contractoffer_dsc.controller;

import de.fraunhofer.isst.dawid.contractoffer_dsc.execption.ErrorMessage;
import de.fraunhofer.isst.dawid.contractoffer_dsc.execption.PolicyRestrictionException;
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
        return new ResponseEntity<>("Contract Service is running...", HttpStatus.OK);
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
        return new ResponseEntity<>(contract, HttpStatus.CREATED);
    }

    @PostMapping("/api/ids/agrrement")
    public ResponseEntity<Object> getContractAgreement(@RequestParam String recipient, @RequestParam String catalog) throws PolicyRestrictionException {
        consumerService = ConsumerService.getInstance(recipient);
        if (consumerService.checkConditionLocationAvailable(catalog)) {
            if(consumerService.verifyConditionLocation()) {
                Agrrement agrrement = consumerService.getContractAgreement();
                return new ResponseEntity<>(agrrement, HttpStatus.CREATED);
            } else
                return new ResponseEntity<>(new PolicyRestrictionException(ErrorMessage.POLICY_RESTRICTION).getMessage(),HttpStatus.PRECONDITION_FAILED);
        } else
             return new ResponseEntity<>(consumerService.getContractAgreement(), HttpStatus.CREATED);
    }

    @GetMapping(value = "api/ids/resourcecatalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResourceCatalog(@RequestParam String recipient) {
        consumerService = ConsumerService.getInstance(recipient);
        String response = consumerService.getProviderResourceCatalog();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
