package li.bryt.tools.activiti_image_server.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
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

@IocBean
public class ImageService {
	protected final Log LOG;
	
	private final String HASH_FILE_DIR = "/tmp/activiti_image_server";
	
	@Inject
	private ApplicationService appService;
	
	public ImageService() {
		this.LOG = Logs.get();
	}
	
	public Object deployFromUrlReturnImage(String src) {
		try {
			RepositoryService repositoryService = this.appService.getEngine().getRepositoryService();
			URL website = new URL(src);
			InputStream in = website.openStream();
			Deployment deploy = repositoryService.createDeployment().addInputStream("tmp.bpmn", in).deploy();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
			InputStream imageStream = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int reads = imageStream.read();

			while (reads != -1) {
				baos.write(reads);
				reads = imageStream.read();
			}
			repositoryService.deleteDeployment(deploy.getId(), true);
			return baos.toByteArray();
		}catch(Exception e) {
			LOG.error(e);
			return null;			
		}		
	}

	public String getActivitiPngHash(String xml) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(xml));
		String hash = String.format("%032x", new BigInteger(1, md5.digest()));
		String path = HASH_FILE_DIR + File.pathSeparator + hash + ".png";
		
		Path f = Paths.get(path);
		Path d = Paths.get(HASH_FILE_DIR);
		
		if(!Files.isDirectory(d)) {
			new File(HASH_FILE_DIR).mkdirs();
		}

		if (!Files.exists(f)) {	
			RepositoryService repositoryService = this.appService.getEngine().getRepositoryService();
			
			Deployment deploy = repositoryService.createDeployment().addString("foo.bpmn", xml).deploy();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
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
	
			FileOutputStream outputStream = new FileOutputStream(path);
			outputStream.write(baos.toByteArray());
			outputStream.close();
		}
		return hash;
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
}
