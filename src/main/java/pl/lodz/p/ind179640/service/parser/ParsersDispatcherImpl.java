package pl.lodz.p.ind179640.service.parser;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.lodz.p.ind179640.domain.Building;
import pl.lodz.p.ind179640.domain.Version;
import pl.lodz.p.ind179640.repository.VersionRepository;

@Service
public class ParsersDispatcherImpl implements ParsersDispatcher{
	
	private final ApplicationContext appContext;
	private final Map<String, Parser> parsersMap;
	private final VersionRepository versionRepo;
	
	
	@Autowired
	public ParsersDispatcherImpl(ApplicationContext appContext, VersionRepository versionRepo) {
		this.appContext = appContext;
		this.versionRepo = versionRepo;
		
		parsersMap = appContext.getBeansOfType(Parser.class);
	}
	

	@Override
	public void parse(byte[] bytes, String dept) throws ParserNotFoundException {
		
		Parser parser = parsersMap.get(dept);
		
		if(parser == null){
			throw new ParserNotFoundException();
		}
		
		parser.parse(bytes);
		
		updateVersion(dept);
		
	}


	@Transactional
	protected void updateVersion(String dept) {

		Version version = new Version();
		version.setName(dept);
		
		Example<Version> versionExample = Example.of(version);
		Version versionResult = versionRepo.findOne(versionExample);
		
		if(versionResult != null){
			version = versionResult;
			Integer ver = version.getVer();
			
			if(ver != null){
				ver++;
			} else {
				ver = 0;
			}
			
			version.setVer(ver);
			
		} else {
			version.setVer(0);
			version = versionRepo.save(version);
		}
		
		
	}

}
