package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdsHasDefaultEndpoint {

    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:accessURL")
    public IdsAccessURL idsAccessURL;
}
