package com.yonhoo.ddd.domain.model;

public enum ConditionType {

    INVITES_MEMBER("InvitesMember", ",", false),
    ISSUING_BANK("IssuingBank", ",", true),
    CARD_TYPE("CardType", ",", true),
    COUNTRY("Country", ",", false),
    WEEKLY_CALENDAR("WeeklyCalendar", ",", false),
    CUSTOMER_CALENDAR("CustomerCalendar", ",", false),
    PERIOD("Period", ",", false),
    THEME_PARK_CODE("ThemeParkCode", ",", false),
    ACCESS_CODE("OfferCode", ",", false),

    //hotel
    MEMBER("Member", ",", false);

    private final String code;
    private final String splitter;
    private final boolean storedAsIdentifier;

    ConditionType(String code, String splitter, boolean storedAsIdentifier) {
        this.code = code;
        this.splitter = splitter;
        this.storedAsIdentifier = storedAsIdentifier;
    }

    public static ConditionType fromCode(String code) {
        for (ConditionType ruleType : values()) {
            if (ruleType.getCode().equals(code)) {
                return ruleType;
            }
        }
        throw new ClassCastException("failed to cast enum type " + code);
    }

    public String getCode() {
        return code;
    }
}
