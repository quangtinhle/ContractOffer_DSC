package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectorDescription {
    @JsonProperty("@context")
    public Context context;
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:publicKey")
    public IdsPublicKey idsPublicKey;
    @JsonProperty("ids:version")
    public String idsVersion;
    @JsonProperty("ids:description")
    public ArrayList<IdsDescription> idsDescription;
    @JsonProperty("ids:title")
    public ArrayList<IdsTitle> idsTitle;
    @JsonProperty("ids:hasDefaultEndpoint")
    public IdsHasDefaultEndpoint idsHasDefaultEndpoint;
    @JsonProperty("ids:inboundModelVersion")
    public ArrayList<String> idsInboundModelVersion;
    @JsonProperty("ids:outboundModelVersion")
    public String idsOutboundModelVersion;
    @JsonProperty("ids:securityProfile")
    public IdsSecurityProfile idsSecurityProfile;
    @JsonProperty("ids:resourceCatalog")
    public ArrayList<Object> idsResourceCatalog;
    @JsonProperty("ids:maintainer")
    public IdsMaintainer idsMaintainer;
    @JsonProperty("ids:curator")
    public IdsCurator idsCurator;
}
