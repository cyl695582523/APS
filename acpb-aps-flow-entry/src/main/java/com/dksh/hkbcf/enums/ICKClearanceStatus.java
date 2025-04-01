package com.dksh.hkbcf.enums;

import java.util.Arrays;

public enum ICKClearanceStatus {
    UNCHECKED(1),
    CHECKED(2);
    private final int value;

    private ICKClearanceStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ICKClearanceStatus valueOf(int value){
        return Arrays.stream(ICKClearanceStatus.values())
                .filter(ickClearanceStatus -> ickClearanceStatus.value()==value).findFirst().orElse(null);
    }
}
