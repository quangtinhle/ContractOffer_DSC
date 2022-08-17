package de.fraunhofer.isst.dawid.contractoffer_dsc.execption;

public class PolicyRestrictionException extends RuntimeException{

    public PolicyRestrictionException(ErrorMessage msg) {
        super(msg.toString());
    }
}
