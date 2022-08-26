package xin.cosmos.basic.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * xml与bean之间相互转换
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

    public static void main(String[] args) {
        User user = new User();
        user.setName("zhangsan");
        user.setAge(2000);
        Hobby hobby = new Hobby();
        hobby.setName("篮球");
        hobby.setDesc("打篮球");
        List<Hobby> list = new ArrayList<>();
        list.add(hobby);
        user.setHobbies(list);
        System.out.println("beanToXml(user) = " + beanToXml((Person)user));
    }
}



@XmlRootElement(name = "Person")
class Person {

}
@XmlRootElement(name = "Person")
class User extends Person {
    private String name;
    private int age;
    private List<Hobby> hobbies;

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @XmlElementWrapper(name = "Hobbies")
    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }
}

class Hobby {
    private String name;

    private String desc;

    @XmlElement(name = "HobbyName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
