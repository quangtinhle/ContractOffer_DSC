package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdsInstance {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:fileName")
    public String idsFileName;
    @JsonProperty("ids:creationDate")
    public IdsCreationDate idsCreationDate;
    @JsonProperty("ids:byteSize")
    public int idsByteSize;
    @JsonProperty("ids:checkSum")
    public String idsCheckSum;
}
