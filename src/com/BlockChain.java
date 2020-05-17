package com;

import java.util.LinkedList;
import java.util.List;

public class BlockChain {
    public Block block;
    public List<BlockChain> blockChain = new LinkedList<>();
    private static Block genesisBlock;

    public BlockChain(Block block){
        this.block = block;
    }

    public boolean addBlock (Block block){
        if (this.block.getHash().equals(block.getPrevBlockHash())) {
            blockChain.add(new BlockChain(block));
            return true;
        }

        for (BlockChain child : blockChain) {
            if (child.addBlock(block))
                return true;
        }
        return false;
    }

    public BlockChain getChainHead(){
        if (blockChain.isEmpty())
            return this;

        int max = 0;
        BlockChain current = null;
        for (BlockChain child : blockChain) {
            int depth = child.depth();
            if (depth > max) {
                max = depth;
                current = child.getChainHead();
            }
        }
        return current;
    }
    public int depth(){
        if (blockChain.isEmpty())
            return 1;

        int max = 0;
        for (BlockChain child: blockChain){
            int depth = child.depth();
            if (depth > max)
                max = depth;
        }

        return max + 1;
    }

    public static Block getGenesisBlock () {
        if (genesisBlock != null)
            return genesisBlock;
        genesisBlock = new Block("0");
        genesisBlock.setHash(genesisBlock.calculateBlockHash());
        return genesisBlock;
    }

    public BlockChain traverseChain (long txid){
        if (block.containTransaction(txid))
            return this;
        BlockChain current = null;
        for (BlockChain child : blockChain) {
            current = child.traverseChain(txid);
            if (current != null)
                break;
        }
        return current;
    }
}
