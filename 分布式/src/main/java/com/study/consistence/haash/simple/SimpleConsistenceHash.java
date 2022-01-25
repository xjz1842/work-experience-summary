package com.study.consistence.haash.simple;

import com.study.consistence.haash.simple.Node;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class SimpleConsistenceHash {

    /**
     * 一致性Hash算法按【顺时方向】遍历环，每个节点排列是有序的
     */
    private static SortedMap<Integer, String> hashNodes = new TreeMap<Integer, String>();


    /**
     * 一致性Hash算法，使用【0 ~ 2^32-1】的值形成环形结构，使用2^32进行取模
     */
    private static final int MODE_NUMBER = (1 << 32)-1;;

    public static void main(String[] args) {
        System.out.println(MODE_NUMBER);
    }
    /**
     * 构造函数，计算每个节点的Hash值
     *
     * @param serverNodes 服务节点
     */
    public SimpleConsistenceHash(List<Node> serverNodes) {
        for (Node node : serverNodes) {
            hashNodes.put(getHash(node.getIp()), node.getIp());
        }
        System.out.println(hashNodes);
    }

    public String getNode(String key) {
        // 得到节点的Hash函数
        int hashCode = getHash(key);
        System.out.println(hashCode);
        // 根据HashCode找到大于该Hash的所有Map
        SortedMap<Integer, String> selectedNodes = hashNodes.tailMap(hashCode);
        if (selectedNodes.isEmpty()) {
            // 如果没有，则从所有节点中，取出第一个节点的IP
            int firstKey = hashNodes.firstKey();
            return hashNodes.get(firstKey);
        }
        // 否则就取大于该hash的第一个节点的IP
        return hashNodes.get(selectedNodes.firstKey());
    }

    private int getHash(String key) {
        // 防止hashCode出现负值
        int hashCode = Math.abs(key.hashCode());
        return hashCode;
    }
}
