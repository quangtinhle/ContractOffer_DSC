package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdsContractOffer {
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("ids:permission")
    private List<IdsPermission> idsPermission;
    @JsonProperty("ids:provider")
    private IdsProvider idsProvider;
    @JsonProperty("ids:consumer")
    private IdsConsumer idsConsumer;
    private String preferenceUUID;
    private String citizenUUID;
    private Boolean active;
}
