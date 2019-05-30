package waster.domain.helper;

import waster.domain.entity.BenchScheduler;

import javax.persistence.AttributeConverter;
import java.io.*;

public class BenchSchedulerConverter implements AttributeConverter<BenchScheduler, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(BenchScheduler benchScheduler) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bout);
            out.writeObject(benchScheduler);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bout.toByteArray();

    }

    @Override
    public BenchScheduler convertToEntityAttribute(byte[] bytes) {
        BenchScheduler result;
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bin);
            result = (BenchScheduler) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }
}
