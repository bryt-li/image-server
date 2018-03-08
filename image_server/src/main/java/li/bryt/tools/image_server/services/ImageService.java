package li.bryt.tools.image_server.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@IocBean
public class ImageService {
	protected final Log LOG;

	private final String HASH_FILE_DIR = "/tmp/image_server";

	@Inject
	private ApplicationService appService;

	@Inject("java:$config.get('host')")
	private String HOST;

	public ImageService() {
		this.LOG = Logs.get();
	}

	public String getImageUrlFromText(String text) throws Exception {
		String hash = getHash(text);
		String path = HASH_FILE_DIR + File.pathSeparator + hash + ".png";

		Path f = Paths.get(path);
		Path d = Paths.get(HASH_FILE_DIR);

		if (!Files.isDirectory(d)) {
			new File(HASH_FILE_DIR).mkdirs();
		}

		if (!Files.exists(f)) {
			if (text.trim().startsWith("@startuml")) {
				generatePlantumlImage(text, path);
			} else if (text.trim().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
				generateActivitiImage(text, path);
			}
		}
		return HOST + "/png/" + hash;
	}
	
	public byte[] getImageFromText(String text) throws Exception {
		String hash = getHash(text);
		String path = HASH_FILE_DIR + File.pathSeparator + hash + ".png";

		Path f = Paths.get(path);
		Path d = Paths.get(HASH_FILE_DIR);

		if (!Files.isDirectory(d)) {
			new File(HASH_FILE_DIR).mkdirs();
		}
		
		if (!Files.exists(f)) {
			if (text.trim().startsWith("@startuml")) {
				generatePlantumlImage(text, path);
			} else if (text.trim().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
				generateActivitiImage(text, path);
			}else {
				LOG.debug(text);
				throw new Exception("invalid format.");
			}
		}
		
		return getHashPng(hash);
	}


	public byte[] getHashPng(String hash) throws Exception {
		String path = HASH_FILE_DIR + File.pathSeparator + hash + ".png";
		FileInputStream fis = new FileInputStream(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = fis.read();

		while (reads != -1) {
			baos.write(reads);
			reads = fis.read();
		}
		fis.close();
		return baos.toByteArray();
	}

	private void generateActivitiImage(String xml, String filePath) throws Exception {
		RepositoryService repositoryService = this.appService.getEngine().getRepositoryService();

		Deployment deploy = repositoryService.createDeployment().addString("foo.bpmn", xml).deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploy.getId()).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
		InputStream imageStream = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = imageStream.read();

		while (reads != -1) {
			baos.write(reads);
			reads = imageStream.read();
		}
		imageStream.close();
		repositoryService.deleteDeployment(deploy.getId(), true);

		FileOutputStream outputStream = new FileOutputStream(filePath);
		outputStream.write(baos.toByteArray());
		outputStream.close();
	}

	private void generatePlantumlImage(String uml, String filePath) throws Exception {
		FileOutputStream outputStream = new FileOutputStream(filePath);
		SourceStringReader reader = new SourceStringReader(uml);
		reader.outputImage(outputStream, new FileFormatOption(FileFormat.PNG, false));
		outputStream.close();
	}

	private String getHash(String text) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(text));
		String hash = String.format("%032x", new BigInteger(1, md5.digest()));
		return hash;
	}

	public String getClientJs() {
		String s = String.join("\n"
		         , "function setImageUrl(text,img){"
		         , "  if (text.indexOf(\"@startuml\") == 0 ||"
		         , "      text.indexOf('<?xml version=\"1.0\" encoding=\"UTF-8\"?>') == 0) {"
		         , "    $.ajax({"
		         , "      type: \"POST\","
		         , "      url: \"" + HOST + "/text\","
		        	 , "      data: text,"
		        	 , "      crossDomain: true,"
		        	 , "      dataType: \"json\""
		        	 , "    }).done(function(data) {"
		        	 , "      if (data.ok) {"
		        	 , "        img.attr(\"src\", data.payload.url);"
		        	 , "      }"
		        	 , "    });"
		        	 , "  }"
		        	 , "}"
		        	 , "function getImageUrl(src){return \"" + HOST + "/url?src=\" + src;}");
		return s;
	}
}
