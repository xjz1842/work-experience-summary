package com.study.consistence.haash.virtual;

public class VirtualNode {

    private final TrueNode trueNode;

    private final int nodeNumber;


    public VirtualNode(TrueNode trueNode, int nodeNumber) {
        this.trueNode = trueNode;
        this.nodeNumber = nodeNumber;
    }

    public String getVirtualIp() {
        return this.trueNode.getIp() + nodeNumber;
    }

    public String getTrueIp() {
        return trueNode.getIp();
    }

}