package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdsPublicKey {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:keyType")
    public IdsKeyType idsKeyType;
    @JsonProperty("ids:keyValue")
    public String idsKeyValue;
}
