package com.example.pef.prathamopenschool;

// add created by id

public class Group {
    public String GroupID;
    public String GroupName;
    public String UnitNumber;
    public String DeviceID;
    public String Responsible;
    public String ResponsibleMobile;
    public int VillageID;
    public String GroupCode;
    public int ProgramID;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public int getVillageID() {
        return VillageID;
    }

    public void setVillageID(int villageID) {
        VillageID = villageID;
    }

    public int getProgramID() {
        return ProgramID;
    }

    public void setProgramID(int programID) {
        ProgramID = programID;
    }
}