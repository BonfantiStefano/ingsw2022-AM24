package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithStudents;

public class ReplaceCharacterStudents implements Update{
    private final VirtualCharacterWithStudents virtualCharacterWithStudents;
    private final int index;

    public ReplaceCharacterStudents(VirtualCharacterWithStudents virtualCharacterWithStudents, int index) {
        this.virtualCharacterWithStudents = virtualCharacterWithStudents;
        this.index = index;
    }

    public VirtualCharacterWithStudents getVirtualCharacterWithStudents() {
        return virtualCharacterWithStudents;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(CLI c) {
        c.visit(this);
    }
}
