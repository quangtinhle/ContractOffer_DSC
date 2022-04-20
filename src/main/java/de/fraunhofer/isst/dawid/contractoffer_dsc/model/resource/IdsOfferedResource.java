package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdsOfferedResource {
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("ids:contractOffer")
    private List<IdsContractOffer> idsContractOffer;
    @JsonProperty("ids:representation")
    private List<IdsReprensentation> idsReprensentation;
}
