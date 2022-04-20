package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdsReprensentation {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("ids:instance")
    private List<IdsInstance> idsInstance;
    @JsonProperty("ids:created")
    private IdsCreated idsCreated;
}
