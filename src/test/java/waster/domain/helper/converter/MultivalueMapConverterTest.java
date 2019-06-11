package waster.domain.helper.converter;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import waster.domain.entity.Setting;

import static org.junit.Assert.assertEquals;

public class MultivalueMapConverterTest {

    @Test
    public void convertToDatabaseColumn_withOneValue() {
        String expected = "1:1;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = new LinkedMultiValueMap<>();
        map.add(1L, Setting.builder().id(1L).build());
        String result = multivalueMapConverter.convertToDatabaseColumn(map);
        assertEquals(expected, result);
    }

    @Test
    public void convertToDatabaseColumn_withSomeKeys() {
        String expected = "1:1;2:1;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = new LinkedMultiValueMap<>();
        map.add(1L, Setting.builder().id(1L).build());
        map.add(2L, Setting.builder().id(1L).build());
        String result = multivalueMapConverter.convertToDatabaseColumn(map);
        assertEquals(expected, result);
    }

    @Test
    public void convertToDatabaseColumn_withSomeKeysAndValues() {
        String expected = "1:1;2:1,2,3;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = new LinkedMultiValueMap<>();
        map.add(1L, Setting.builder().id(1L).build());
        map.add(2L, Setting.builder().id(1L).build());
        map.add(2L, Setting.builder().id(2L).build());
        map.add(2L, Setting.builder().id(3L).build());
        String result = multivalueMapConverter.convertToDatabaseColumn(map);
        assertEquals(expected, result);
    }

    @Test
    public void convertToDatabaseColumn_withSomeValue() {
        String expected = "1:1,2;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = new LinkedMultiValueMap<>();
        map.add(1L, Setting.builder().id(1L).build());
        map.add(1L, Setting.builder().id(2L).build());
        String result = multivalueMapConverter.convertToDatabaseColumn(map);
        assertEquals(expected, result);
    }

    @Test
    public void convertToEntityAttribute_whenOneKeyAndValue() {
        String source = "1:1;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = multivalueMapConverter.convertToEntityAttribute(source);
        assertEquals(1, map.keySet().size());
        assertEquals(1,map.get(1L).size());
    }
    @Test
    public void convertToEntityAttribute_whenOneKeyAndSomeValue() {
        String source = "1:1,2;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = multivalueMapConverter.convertToEntityAttribute(source);
        assertEquals(1, map.keySet().size());
        assertEquals(2,map.get(1L).size());
    }
    @Test
    public void convertToEntityAttribute_whenSomeKeyAndSomeValue() {
        String source = "1:1,2;2:1,2;";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = multivalueMapConverter.convertToEntityAttribute(source);
        assertEquals(2, map.keySet().size());
        assertEquals(2,map.get(1L).size());
    }
    @Test
    public void convertToEntityAttribute_whenWithoutLastSeparator() {
        String source = "1:1,2;2:1,2";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = multivalueMapConverter.convertToEntityAttribute(source);
        assertEquals(2, map.keySet().size());
        assertEquals(2,map.get(1L).size());
    }
    @Test
    public void convertToEntityAttribute_whenWithoutFirstSeparator() {
        String source = "1:1,22:1,2";
        MultivalueMapConverter multivalueMapConverter = new MultivalueMapConverter();
        MultiValueMap<Long, Setting> map = multivalueMapConverter.convertToEntityAttribute(source);
        assertEquals(1, map.keySet().size());
        assertEquals(2,map.get(1L).size());
    }
}