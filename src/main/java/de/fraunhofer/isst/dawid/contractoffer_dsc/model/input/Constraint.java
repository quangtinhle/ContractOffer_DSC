package de.fraunhofer.isst.dawid.contractoffer_dsc.model.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Constraint {

    private String dataHistory;
    private String dataConsumer;
    private String location;
    private Double minCompensation;
    private String identifiability;
    private String usagePeriod;
    private String purpose;
    private String counter;
}
