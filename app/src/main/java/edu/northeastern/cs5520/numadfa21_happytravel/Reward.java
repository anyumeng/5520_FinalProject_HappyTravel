package edu.northeastern.cs5520.numadfa21_happytravel;

public class Reward {

    private int rewardCount;
    private RewardRequirement rewardRequirement;

    public int getRewardCount() {
        return this.rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public RewardRequirement getRewardRequirement() {
        return rewardRequirement;
    }

    public void setRewardRequirement(RewardRequirement rewardRequirement) {
        this.rewardRequirement = rewardRequirement;
    }
}
