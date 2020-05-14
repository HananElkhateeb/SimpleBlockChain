import java.util.LinkedList;
import java.util.List;

public class BlockChain {
    public Block block;
    public List<BlockChain> blockChain = new LinkedList<>();

    public BlockChain(Block block){
        this.block = block;
    }

    public void addBlock (Block block){}
    public BlockChain getChainHead(){
        return null;
    }
    public int depth(){
        return 0;
    }
}
