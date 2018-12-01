package backend;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class FileManager {

	public static ArrayList<Match> ImportFile() {
		return ImportFile(System.getProperty("user.home") + "/Desktop/duplicates.xml");
	}
	
	public static ArrayList<Match> ImportFile(String url) {
		ArrayList<Match> matches = new ArrayList<Match>();

		try {
			File fXmlFile = new File(url);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Match");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					ArrayList<String> deviations = new ArrayList<String>();

					for(String s : eElement.getElementsByTagName("deviations").item(0).getTextContent().split("###")) {
						if(s.length() > 0) {
							System.out.println(s);
							deviations.add(s);
						}
					}

					matches.add(
							new Match(
									eElement.getElementsByTagName("matching-text").item(0).getTextContent(), 
									Boolean.parseBoolean(eElement.getElementsByTagName("hidden").item(0).getTextContent()), 
									deviations));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return matches;
	}
	
	public static void ExportFile(ArrayList<Match> matches) {
		ExportFile(matches, System.getProperty("user.home") + "/Desktop/duplicates.xml");
	}

	public static void ExportFile(ArrayList<Match> matches, String saveTo) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("matches");
			doc.appendChild(rootElement);

			int i = 0;

			for(Match m : matches) {
				// match elements
				Element staff = doc.createElement("Match");
				rootElement.appendChild(staff);

				// set attribute to match element
				Attr attr = doc.createAttribute("id");
				attr.setValue(i + "");
				staff.setAttributeNode(attr);

				// matching text elements
				Element firstname = doc.createElement("matching-text");
				firstname.appendChild(doc.createTextNode(m.searchedOn));
				staff.appendChild(firstname);

				// number of duplicates elements
				Element lastname = doc.createElement("num-duplicates");
				lastname.appendChild(doc.createTextNode(m.numMatches + ""));
				staff.appendChild(lastname);

				// number of duplicates elements
				Element hidden = doc.createElement("hidden");
				hidden.appendChild(doc.createTextNode(m.hidden + ""));
				staff.appendChild(hidden);

				// deviations elements
				String deviations = "";
				for(String s : m.matches) {
					deviations = s + "###" + deviations;
				}
				Element nickname = doc.createElement("deviations");
				nickname.appendChild(doc.createTextNode(deviations));
				staff.appendChild(nickname);

				i++;
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(saveTo));

			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
