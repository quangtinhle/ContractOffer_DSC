package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdsOperator {
    @JsonProperty("@id")
    private String id;
}
