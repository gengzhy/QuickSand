package xin.cosmos.basic.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * xml与bean之间相互转换
 *
 * 基于javax.xml，不支持继承
 */
public class XmlBeanUtils {

    public static String beanToXml(Object bean) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
            StringWriter writer = new StringWriter();
            marshaller.marshal(bean, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T xmlToBean(String xml, Class<T> beanClazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(beanClazz);
            Unmarshaller um = context.createUnmarshaller();
            StringReader sr = new StringReader(xml);
            return (T) um.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
