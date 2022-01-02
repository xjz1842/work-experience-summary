package com.study.consistence.haash.virtual;

import com.study.consistence.haash.simple.Snowflake;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class KeyValueVirtualNodeStandardDeviation {


    /**
     * 定义10个节点，并且初始化IP
     */
    private static final List<TrueNode> NODES = new ArrayList<TrueNode>(10);
    static {
        NODES.add(new TrueNode("192.168.0.0"));
        NODES.add(new TrueNode("192.168.0.1"));
        NODES.add(new TrueNode("192.168.0.2"));
        NODES.add(new TrueNode("192.168.0.3"));
        NODES.add(new TrueNode("192.168.0.4"));
        NODES.add(new TrueNode("192.168.0.5"));
        NODES.add(new TrueNode("192.168.0.6"));
        NODES.add(new TrueNode("192.168.0.7"));
        NODES.add(new TrueNode("192.168.0.8"));
        NODES.add(new TrueNode("192.168.0.9"));
    }

    public static void main(String[] args) {
        VirtualNodeConsistentHash virtualNodeConsistentHash = new VirtualNodeConsistentHash(NODES, 10);
        Map<String, Integer> nodeCounts = new HashMap<String, Integer>();
        int keyValueCount = 1000000;
        // 需要将ID尽量散列到环上，因为对2^32取模，所以数据需要大于2^32，这样可以保证取模后数据分散
        // 因此参考Snowflake算法生产ID
        Snowflake snowflake = new Snowflake(1, 1, 0);
        for (int i = 0; i < keyValueCount; i++) {
            String nodeIp = virtualNodeConsistentHash.getTrueNode(String.valueOf(snowflake.nextId()));
            if (nodeCounts.containsKey(nodeIp)) {
                int count = nodeCounts.get(nodeIp);
                nodeCounts.put(nodeIp, count + 1);
            } else {
                nodeCounts.put(nodeIp, 1);
            }
        }
        // 一个真实节点对应10个节点，如果是100万个值，理想情况每个虚拟节点平均有10000个值
        System.out.println(compute(nodeCounts.values(), 10000, NODES.size() * 10));
    }

    public static String compute(Collection<Integer> sampleValues, Integer average, int total) {
        double sum = 0;
        for (Integer value : sampleValues) {
            sum += (value - average) * (value - average);
        }
        return BigDecimal.valueOf(Math.sqrt(sum/total)).setScale(2, RoundingMode.CEILING).toEngineeringString();
    }
}
