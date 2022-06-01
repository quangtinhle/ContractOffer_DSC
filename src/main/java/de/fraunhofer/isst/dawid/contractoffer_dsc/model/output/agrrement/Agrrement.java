package de.fraunhofer.isst.dawid.contractoffer_dsc.model.output.agrrement;

import lombok.Setter;

@Setter
public class Agrrement {
    public String citizenUUID;
    public String preferenceUUID;
    public Boolean active;
    public String creationDate;
    public String modificationDate;
    public String remoteId;
    public boolean confirmed;
    public String value;
    public Links _links;

}
