package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdsAction {
    @JsonProperty("@id")
    public String id;
}
