package xml;

import xml.mp.Mt102;

import javax.xml.bind.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XmlHelper {

    public static <T> T unmarshall(InputStream stream, Class<T> T)
            throws Exception {
        Source source = new StreamSource(stream);

        JAXBContext ctx = JAXBContext.newInstance(T.getPackage().getName());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        JAXBElement<T> root = unmarshaller.unmarshal(source, T);

        return root.getValue();
    }

    public static <T> String marshall(T obj) {
        StringWriter sw = new StringWriter();

        Class<?> objClazz = obj.getClass();
        try {
            Class<?> objFactoryClazz = Class.forName(objClazz.getPackage()
                    .getName() + ".ObjectFactory");

            JAXBContext ctx = JAXBContext.newInstance(objClazz.getPackage()
                    .getName());

            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            Object objectFactory = objFactoryClazz.newInstance();

            Method createMethod = objFactoryClazz.getMethod(
                    "create" + objClazz.getSimpleName(), objClazz);

            @SuppressWarnings("unchecked")
            JAXBElement<T> el = (JAXBElement<T>) createMethod.invoke(
                    objectFactory, obj);

            marshaller.marshal(el, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

    public static void writeToFile(Object object, String filename) {
        String marshalled = marshall(object);

        try {
            File file = new File("src/main/resources/export/" + filename + ".xml");
            if (!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(marshalled);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XmlHelper() {
    }
}


