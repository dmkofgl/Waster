package waster.domain.helper;

import org.jgrapht.Graph;

import javax.persistence.AttributeConverter;
import java.io.*;

public class GraphConverter implements AttributeConverter<Graph,byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(Graph graph) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bout);
            out.writeObject(graph);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bout.toByteArray();

    }

    @Override
    public Graph convertToEntityAttribute(byte[] bytes) {
        Graph result;
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bin);
            result= (Graph) in.readObject();
        } catch (IOException |ClassNotFoundException  e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
        return  result;
    }
}
