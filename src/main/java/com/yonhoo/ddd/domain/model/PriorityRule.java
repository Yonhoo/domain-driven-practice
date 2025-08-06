package com.yonhoo.ddd.domain.model;

/**
 * 优先级规则值对象
 * 定义策略应用的优先级和顺序
 */
public class PriorityRule {
    private boolean applyUserLevel;
    private boolean applyRegion;
    private boolean applyChannel;
    private int userLevelPriority;
    private int regionPriority;
    private int channelPriority;

    public boolean shouldApplyUserLevel() {
        return applyUserLevel;
    }

    public boolean shouldApplyRegion() {
        return applyRegion;
    }

    public boolean shouldApplyChannel() {
        return applyChannel;
    }

    // Getters and setters
    public void setApplyUserLevel(boolean applyUserLevel) {
        this.applyUserLevel = applyUserLevel;
    }

    public void setApplyRegion(boolean applyRegion) {
        this.applyRegion = applyRegion;
    }

    public void setApplyChannel(boolean applyChannel) {
        this.applyChannel = applyChannel;
    }

    public int getUserLevelPriority() {
        return userLevelPriority;
    }

    public void setUserLevelPriority(int userLevelPriority) {
        this.userLevelPriority = userLevelPriority;
    }

    public int getRegionPriority() {
        return regionPriority;
    }

    public void setRegionPriority(int regionPriority) {
        this.regionPriority = regionPriority;
    }

    public int getChannelPriority() {
        return channelPriority;
    }

    public void setChannelPriority(int channelPriority) {
        this.channelPriority = channelPriority;
    }
}