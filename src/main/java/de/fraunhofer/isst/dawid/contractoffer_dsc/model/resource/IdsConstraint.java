package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdsConstraint {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:operator")
    public IdsOperator idsOperator;
    @JsonProperty("ids:rightOperand")
    public IdsRightOperand idsRightOperand;
    @JsonProperty("ids:leftOperand")
    public IdsLeftOperand idsLeftOperand;
}
