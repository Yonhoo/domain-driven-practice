package com.yonhoo.ddd.domain.model;

/**
 * 用户上下文值对象
 * 包含用户的等级、地域、渠道等属性
 */
public class UserContext {
    private final String userId;
    private final UserLevel userLevel;
    private final Region region;
    private final Channel channel;
    private final String membershipId;

    public UserContext(String userId, UserLevel userLevel, Region region, Channel channel, String membershipId) {
        this.userId = userId;
        this.userLevel = userLevel;
        this.region = region;
        this.channel = channel;
        this.membershipId = membershipId;
    }

    public String getUserId() {
        return userId;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public Region getRegion() {
        return region;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getMembershipId() {
        return membershipId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserContext)) return false;
        UserContext that = (UserContext) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}

enum UserLevel {
    BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
}

enum Region {
    NORTH_CHINA, SOUTH_CHINA, EAST_CHINA, WEST_CHINA, INTERNATIONAL
}

enum Channel {
    OFFICIAL_WEBSITE, MOBILE_APP, THIRD_PARTY_OTA, OFFLINE_STORE, CORPORATE
} 