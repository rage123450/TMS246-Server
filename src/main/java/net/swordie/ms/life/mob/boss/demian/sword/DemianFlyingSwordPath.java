package net.swordie.ms.life.mob.boss.demian.sword;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.BossConstants;
import net.swordie.ms.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17-8-2019.
 *
 * @author Asura
 */
public class DemianFlyingSwordPath {

    public static final int startDelay = 500; // on 2nd last node of Path
    public static final int endDelay = 11000; // on last node of Path

    // Bouncing Paths
        // 1
    public static final List<Position> flyingSwordPathBouncing1 = new ArrayList<Position>() {{
        add(new Position(100, -100));
        add(new Position(340, 100));
        add(new Position(890, -200));
        add(new Position(1390, 50));
        add(new Position(1690, -100));
        add(new Position(1540, -300));
        add(new Position(1040, -25));
        add(new Position(-60, -25));
        add(new Position(340, -25));
        add(new Position(590, 100));
        add(new Position(1090, -200));
        add(new Position(1600, -400));
        add(new Position()); // 0, 0
    }};
        // 2
    public static final List<Position> flyingSwordPathBouncing2 = new ArrayList<Position>() {{
        add(new Position(895, -300));
        add(new Position(1530, -100));
        add(new Position(1290, 100));
        add(new Position(740, -200));
        add(new Position(240, 50));
        add(new Position(-60, -100));
        add(new Position(90, -300));
        add(new Position(590, -25));
        add(new Position(1690, -25));
        add(new Position(1290, -25));
        add(new Position(1040, 100));
        add(new Position(540, -200));
        add(new Position(1600, -400));
        add(new Position(1600, -400));
    }};

    // Creation Position
    public static final Position creationPosition = new Position(895, -200);


    private List<DemianFlyingSwordNode> nodes = new ArrayList<>();
    private boolean stopSword;

    public static DemianFlyingSwordPath flyingSwordCreationPath() {
        DemianFlyingSwordPath demianFlyingSwordPath = new DemianFlyingSwordPath();

        DemianFlyingSwordNode node = new DemianFlyingSwordNode(DemianFlyingSwordNodeType.Bouncing, creationPosition);
        node.setVelocity((short) BossConstants.DEMIAN_SWORD_VELOCITY);
        node.setPathIdx(DemianFlyingSwordPathIdx.Creation);
        demianFlyingSwordPath.getNodes().add(node);

        return demianFlyingSwordPath;
    }

    public static DemianFlyingSwordPath flyingSwordBouncingPath(List<Position> path) {
        DemianFlyingSwordPath demianFlyingSwordPath = new DemianFlyingSwordPath();

        int size = path.size();
        for (int i = 0; i < size; i++) {
            Position position = path.get(i);
            DemianFlyingSwordNodeType nodeType = i >= size - 2 ? DemianFlyingSwordNodeType.Targeting : DemianFlyingSwordNodeType.Bouncing; // Last 2 Nodes
            DemianFlyingSwordNode node = new DemianFlyingSwordNode(nodeType, position);
            node.setPathIdx(DemianFlyingSwordPathIdx.Bouncing2); // Bouncing Path
            flyingSwordLogic(size, i, node);
            demianFlyingSwordPath.getNodes().add(node);
        }

        return demianFlyingSwordPath;
    }

    public static DemianFlyingSwordPath flyingSwordTargetingPath(Position position) { // targets given Position
        DemianFlyingSwordPath demianFlyingSwordPath = new DemianFlyingSwordPath();

        demianFlyingSwordPath.setStopSword(true);
        int size = 2;
        for (int i = 0; i < size; i++) {
            DemianFlyingSwordNode node = new DemianFlyingSwordNode(DemianFlyingSwordNodeType.Bouncing, position);
            node.setPathIdx(DemianFlyingSwordPathIdx.Targeting); // Targeting Position Path
            flyingSwordLogic(size, i, node);
            demianFlyingSwordPath.getNodes().add(node);
        }

        return demianFlyingSwordPath;
    }

    private static void flyingSwordLogic(int size, int i, DemianFlyingSwordNode node) {
        node.setNodeIdx((short) i);
        if (i >= size - 1) { // Last Node
            node.setHide(true);
            node.setEndDelay(endDelay);
        }
        if (i == size - 2) { // 2nd Last Node
            node.setStartDelay(startDelay);
            node.setVelocity((short) BossConstants.DEMIAN_SWORD_TARGETING_VELOCITY);
        }
    }

    public List<DemianFlyingSwordNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<DemianFlyingSwordNode> nodes) {
        this.nodes = nodes;
    }

    public boolean isStopSword() {
        return stopSword;
    }

    public void setStopSword(boolean stopSword) {
        this.stopSword = stopSword;
    }

    public void encodeDemianFlyingSwordPath(OutPacket outPacket) {
        outPacket.encodeByte(isStopSword());
        outPacket.encodeInt(getNodes().size());
        getNodes().forEach(n -> n.encodeNodeData(outPacket));
    }
}
