package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;
@Getter

public class IdsCreated {
    @JsonProperty("@value")
    public Date value;
    @JsonProperty("@type")
    public String type;
}
