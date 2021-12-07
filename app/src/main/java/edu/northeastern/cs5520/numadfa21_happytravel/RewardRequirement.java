package edu.northeastern.cs5520.numadfa21_happytravel;

public class RewardRequirement {
    private String rewardImageUrl;
    private String rewardName;
    private Integer rewardRequirement;

    public String getRewardImageUrl() {
        return rewardImageUrl;
    }

    public void setRewardImageUrl(String rewardImageUrl) {
        this.rewardImageUrl = rewardImageUrl;
    }

    @Override
    public String toString() {
        return "RewardRequirement{"
                + "rewardImageUrl='"
                + rewardImageUrl
                + '\''
                + ", rewardName='"
                + rewardName
                + '\''
                + ", rewardRequirement="
                + rewardRequirement
                + '}';
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public Integer getRewardRequirement() {
        return rewardRequirement;
    }

    public void setRewardRequirement(Integer rewardRequirement) {
        this.rewardRequirement = rewardRequirement;
    }

    public RewardRequirement getCopy() {
        RewardRequirement requirement = new RewardRequirement();
        requirement.setRewardRequirement(this.getRewardRequirement());
        requirement.setRewardName(this.getRewardName());
        requirement.setRewardImageUrl(this.getRewardImageUrl());
        return requirement;
    }
}
