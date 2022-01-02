package com.study.consistence.haash.virtual;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class VirtualNodeConsistentHash {

    /**
     * 一致性Hash算法，使用【0 ~ 2^32-1】的值形成环形结构，使用2^32进行取模
     */
    private static final int MODE_NUMBER = (int)Math.pow(2.0,32)-1;

    /**
     * 一致性Hash算法按【顺时方向】遍历环，每个节点排列是有序的
     */
    private static final SortedMap<Integer, VirtualNode> virtualNodes = new TreeMap<Integer, VirtualNode>();


    /**
     * @param trueNodes         真实节点
     * @param virtualNodeNumber 一个真实对应虚拟节点数量
     */
    public VirtualNodeConsistentHash(List<TrueNode> trueNodes, int virtualNodeNumber) {
        for (TrueNode trueNode : trueNodes) {
            for (int i = 0; i < virtualNodeNumber; i++) {
                VirtualNode virtualNode = new VirtualNode(trueNode, i);
                virtualNodes.put(getHash(virtualNode.getVirtualIp()), virtualNode);
            }
        }
    }

    public String getTrueNode(String key) {
        int hashCode = getHash(key);
        // 根据hash值找到虚拟节点，再根据虚拟节点找到对应的真实节点IP
        // 如果key不存在Hash环中，则获取Hash环中的右子树，
        if (!virtualNodes.containsKey(hashCode)) {
            SortedMap<Integer, VirtualNode> selectNodes = virtualNodes.tailMap(hashCode);
            Integer hitHash = selectNodes.isEmpty() ? virtualNodes.firstKey() : selectNodes.firstKey();
            return virtualNodes.get(hitHash).getTrueIp();
        }
        return virtualNodes.get(hashCode).getTrueIp();
    }

    private int getHash(String key) {
        // 防止hashCode出现负值
        int hashCode = Math.abs(key.hashCode());
        return hashCode;
    }
}
