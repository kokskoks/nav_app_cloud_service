package pl.lodz.p.ind179640.service.parser;

import javax.inject.Inject;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.lodz.p.ind179640.domain.Version;
import pl.lodz.p.ind179640.repository.VersionRepository;

@Aspect
@Component
public class VersionAspect {
	
	@Inject
	private VersionRepository versionRepo;
	
	@Pointcut("execution(public * *(..))")
	private void anyPublicOperation() {}
	
	@AfterReturning("anyPublicOperation() && @annotation(versionUpdate)")
	@Transactional
	public void updateVersion(VersionUpdate versionUpdate){
		String name = versionUpdate.name();
		Version version = new Version();
		version.setName(name);
		
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
		}
		version = versionRepo.save(version);
	}

}
