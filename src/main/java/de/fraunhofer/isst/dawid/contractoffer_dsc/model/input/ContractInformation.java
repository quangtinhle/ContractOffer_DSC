package de.fraunhofer.isst.dawid.contractoffer_dsc.model.input;

import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
public class ContractInformation {
    private String provider;
    private String citizenUUID;
    private String preferenceUUID;
    private Boolean active;
}
