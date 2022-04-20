package de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceCatalog {
    @JsonProperty("@context")
    private Context context;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("ids:offeredResource")
    private List<IdsOfferedResource> idsOfferedResources;
}
