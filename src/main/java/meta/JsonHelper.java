package meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

public class JsonHelper {

	public static void marshall(FormMetaData fmd, String filename) {
		File file = new File("src/main/resources/");
		
		try {
			File jsonFile = new File(file, filename);
			if (!jsonFile.exists()) {
				jsonFile.createNewFile();
			}
			
			OutputStream out = new FileOutputStream(jsonFile);
			
			JAXBContext jc = JAXBContext.newInstance(FormMetaData.class);

			Marshaller marshaller = jc.createMarshaller();

			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE,
					"application/json");

			marshaller
					.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			marshaller.marshal(fmd, out);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FormMetaData unmarshall(String filename) {
		FormMetaData ret = null;

		File file = new File("src/main/resources/");

		try {
			InputStream in = new FileInputStream(new File(file, filename));

			JAXBContext jc = JAXBContext.newInstance(FormMetaData.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();

			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE,
					"application/json");

			unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT,
					true);

			ret = unmarshaller.unmarshal(new StreamSource(in),
					FormMetaData.class).getValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static void main(String[] args) {
		FormMetaData fmd = new FormMetaData();
		fmd.setTableName("UCESNICI_FILMA");
		fmd.setTitle("foooo");
		
		marshall(fmd, "ucesnici_filma.json");
	}
}
