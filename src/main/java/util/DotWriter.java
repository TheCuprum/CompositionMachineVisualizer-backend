package util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import machine.BaseConnectedQuiver;
import machine.Quiver;
import machine.internal.Arrow;
import machine.internal.DotEdge;
import machine.internal.DotNode;

public class DotWriter {
    private static final String DEFAULT_NAME = "default";
    private static final DOTExporter<DotNode, DotEdge> EXPORTER = getDotTExporter();

    String dotOutputDirectory;
    String folder;
    boolean useCache;

    DefaultDirectedGraph<DotNode, DotEdge> cachedGraph = null;
    ArrayList<ArrayList<Arrow>> cachedArrowList = null;

    public static DOTExporter<DotNode, DotEdge> getDotTExporter() {
        DOTExporter<DotNode, DotEdge> exporter = new DOTExporter<>();
        exporter.setGraphAttributeProvider(() -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("size", DefaultAttribute.createAttribute("\"512,512\""));
            map.put("pad", DefaultAttribute.createAttribute("0.1"));
            map.put("forcelabels", DefaultAttribute.createAttribute("true"));
            map.put("rankdir", DefaultAttribute.createAttribute("LR"));
            map.put("ranksep", DefaultAttribute.createAttribute("0.75"));
            map.put("nodesep", DefaultAttribute.createAttribute("0.5"));

            return map;
        });

        exporter.setVertexAttributeProvider((vertex) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("xlabel", DefaultAttribute.createAttribute("x_".concat(String.valueOf(vertex.getValue()))));
            map.put("group", DefaultAttribute.createAttribute(String.valueOf(vertex.getGroup())));
            map.put("shape", DefaultAttribute.createAttribute("point"));
            return map;
        });

        exporter.setEdgeAttributeProvider((edge) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            String head;
            String width;
            if (edge.getConnection() > 0) {
                // head = "normal";
                head = "open";
                width = "1.0";
            } else {
                head = "none";
                width = "0.0";
            }
            map.put("arrowHead", DefaultAttribute.createAttribute(head));
            map.put("arrowsize", DefaultAttribute.createAttribute(width));
            map.put("penwidth", DefaultAttribute.createAttribute(width));
            // TODO

            return map;
        });

        return exporter;
    }

    public DotWriter(String dotOutputPath, String folderName, boolean useCache) {
        this.useCache = useCache;
        this.dotOutputDirectory = FileUtil.createOrChangeDirectory(dotOutputPath).toString();
        this.changeFolder(folderName);
    }

    public void changeFolder(String folderName) {
        this.folder = folderName != null ? folderName : DEFAULT_NAME;
        this.folder = FileUtil.createOrChangeDirectory(
                Path.of(this.dotOutputDirectory, this.folder).toString()).getName();
    }

    public void clearCache() {
        this.cachedArrowList = null;
        this.cachedGraph = null;
    }

    public <CQ extends BaseConnectedQuiver<CQ>> Graph<DotNode, DotEdge> constructGraph(Quiver<CQ> quiver) {
        DefaultDirectedGraph<DotNode, DotEdge> graph;
        ArrayList<ArrayList<Arrow>> arrowIterables;

        if (this.useCache && this.cachedGraph != null && this.cachedArrowList != null) {
            // this.cachedGraph.removeAllEdges(this.cachedGraph.edgeSet());
            // this.cachedGraph.edgeSet().;
            // System.out.println(this.cachedGraph.edgeSet().size());
            HashSet<DotEdge> set = new HashSet<DotEdge>(this.cachedGraph.edgeSet());
            this.cachedGraph.removeAllEdges(set);
            graph = this.cachedGraph;
            arrowIterables = this.cachedArrowList;
        } else {
            // construct graph directly
            graph = new DefaultDirectedGraph<>(DotEdge.class);
            arrowIterables = new ArrayList<>();
            int offset = 0;
            for (int group = 0; group < quiver.size(); group++) {
                CQ connectedQuiver = quiver.get(group);
                int maxIndex = 0;
                ArrayList<Arrow> arrowList = new ArrayList<>();
                for (Arrow a : connectedQuiver.getArrowStates().keySet()) {
                    arrowList.add(a);
                    graph.addVertex(DotNode.create(a.getSourceIndex() + offset, group));
                    graph.addVertex(DotNode.create(a.getTargetIndex() + offset, group));
                    maxIndex = Math.max(maxIndex, Math.max(a.getSourceIndex(), a.getTargetIndex()));
                }
                arrowIterables.add(arrowList);
                offset += (maxIndex + 1);
            }

            //hack for correctly order the connected quivers
            ArrayList<DotNode> nodeArray = new ArrayList<DotNode>(graph.vertexSet());
            graph.removeAllVertices(nodeArray);
            for (int i = nodeArray.size() - 1; i >= 0; i--)
                graph.addVertex(nodeArray.get(i));

            if (this.useCache) {
                this.cachedGraph = graph;
                this.cachedArrowList = arrowIterables;
            }
        }
        // add edges
        int offset = 0;
        int group = 0;
        for (ArrayList<Arrow> arrowList : arrowIterables) {
            int maxIndex = 0;
            for (Arrow a : arrowList) {
                if (quiver.get(group).getArrowState(a) > 0) {
                    graph.addEdge(
                            DotNode.create(a.getSourceIndex() + offset, group),
                            DotNode.create(a.getTargetIndex() + offset, group));
                } else {
                    DotEdge edge = DotEdge.create(0);
                    graph.addEdge(
                            DotNode.create(a.getSourceIndex() + offset, group),
                            DotNode.create(a.getTargetIndex() + offset, group),
                            edge);
                }

                maxIndex = Math.max(maxIndex, Math.max(a.getSourceIndex(), a.getTargetIndex()));
            }
            offset += (maxIndex + 1);
            group++;
        }
        return graph;
    }

    public <CQ extends BaseConnectedQuiver<CQ>> void writeDotFile(Quiver<CQ> quiver, String fileName) {
        Graph<DotNode, DotEdge> graph = this.constructGraph(quiver);
        Path dotPath = Path.of(this.dotOutputDirectory, this.folder, fileName);
        EXPORTER.exportGraph(graph, dotPath.toFile());
    }
}
