import java.util.LinkedList;
import java.util.List;

public class BlockChain {
    public Block block;
    public List<BlockChain> blockChain = new LinkedList<>();

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
}
