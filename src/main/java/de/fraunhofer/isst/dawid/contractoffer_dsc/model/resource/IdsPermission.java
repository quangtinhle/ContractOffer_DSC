package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class IdsPermission {
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("ids:constraint")
    private List<IdsConstraint> idsConstraint;
    @JsonProperty("ids:action")
    private List<IdsAction> idsAction;
    @JsonProperty("ids:target")
    private String idsTarget;
}
