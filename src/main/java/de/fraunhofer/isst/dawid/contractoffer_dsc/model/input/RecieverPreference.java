package de.fraunhofer.isst.dawid.contractoffer_dsc.model.input;

import lombok.Getter;

import java.util.List;

@Getter
public class RecieverPreference {

        private String citizenUUID;
        private String preferenceUUID;
        private Boolean active;
        private String dataProvider;
        private String targetData;
        private List<Constraint> constraints;

}
