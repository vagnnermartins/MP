package com.vagnnermartins.marcaponto.enums;

/**
* Created by vagnnermartins on 16/07/14.
*/
public enum WhichRegisterEnum {
    ENTRANCE(1),
    PAUSE(2),
    BACK(3),
    QUIT(4);

    private final int which;

    WhichRegisterEnum(int which) {
        this.which = which;
    }

    public int getWhich() {
        return which;
    }
}