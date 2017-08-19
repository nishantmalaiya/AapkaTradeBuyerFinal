package com.aapkatrade.buyer.home.buyerregistration.entity;

/**
 * Created by PPC09 on 19-Jan-17.
 */

public class State {
    public String stateId;
    public String stateName;
    public String countprod;

    public State(String stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public State(String stateId, String stateName, String countprod) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.countprod = countprod;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateId='" + stateId + '\'' +
                ", stateName='" + stateName + '\'' +
                ", countprod='" + countprod + '\'' +
                '}';
    }
}
