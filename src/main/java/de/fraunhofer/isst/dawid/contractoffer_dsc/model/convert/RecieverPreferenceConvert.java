package de.fraunhofer.isst.dawid.contractoffer_dsc.model.convert;

import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.ContractInformation;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;

public class RecieverPreferenceConvert {

    private RecieverPreferenceConvert() {};

    public static ContractInformation convertToContractInformation(RecieverPreference recieverPreference) {


        return ContractInformation.builder()
                .provider(recieverPreference.getDataProvider())
                .preferenceUUID(recieverPreference.getPreferenceUUID())
                .citizenUUID(recieverPreference.getCitizenUUID())
                .active(recieverPreference.getActive())
                .build();

    }


}
