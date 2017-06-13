package com.example.pef.prathamopenschool;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Card {
    public String name;
    public String numOfSongs;
    public String thumbnail;
    public String nodeType, nodeTitle, nodeImage, nodePhase, nodeAge, nodeDescription, nodeKeyword, resourceId;
    public String nodeId, resourceLevel, resourceType, resourcePath, sameCode;
    public String nodeDesc,nodeKeywords,nodeList;


    public Card() {
    }

    public Card(String name, String numOfSongs, String thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

}