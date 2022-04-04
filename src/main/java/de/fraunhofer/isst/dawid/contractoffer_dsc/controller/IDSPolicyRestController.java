package de.fraunhofer.isst.dawid.contractoffer_dsc.controller;

import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IDSPolicyRestController {

    @PostMapping("/contractoffer")
    public String getContractOffer(@RequestBody RecieverPreference recieverPreference) {
        //System.out.println(recieverPreference.getConstraints().get(0).getDataConsumer());
        return recieverPreference.getPreferenceUUID();
    }
}
