package de.fraunhofer.isst.dawid.contractoffer_dsc.execption;

public enum ErrorMessage {


    POLICY_RESTRICTION("Policy restriction detected.");

    private final String value;
    ErrorMessage(final String message) {
        this.value = message;
    }

    @Override
    public String toString() {
        return value;
    }
}
