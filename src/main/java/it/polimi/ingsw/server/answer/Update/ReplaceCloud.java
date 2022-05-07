package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.server.virtualview.VirtualCloud;

public class ReplaceCloud implements Update{
    private final VirtualCloud cloud;
    private final int index;

    public ReplaceCloud(VirtualCloud cloud, int index) {
        this.cloud = cloud;
        this.index = index;
    }

    public VirtualCloud getCloud() {
        return cloud;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(CLI c){
        c.visit(this);
    }
}
