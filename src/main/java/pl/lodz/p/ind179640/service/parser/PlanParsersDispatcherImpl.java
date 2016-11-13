package pl.lodz.p.ind179640.service.parser;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PlanParsersDispatcherImpl implements PlanParsersDispatcher{
	
	private final ApplicationContext appContext;
	private final Map<String, Parser> parsersMap;
	
	
	
	@Autowired
	public PlanParsersDispatcherImpl(ApplicationContext appContext) {
		this.appContext = appContext;
		
		parsersMap = appContext.getBeansOfType(Parser.class);
	}
	

	@Override
	public void parse(byte[] bytes, String dept) throws ParserNotFoundException {
		
		Parser parser = parsersMap.get(dept);
		
		if(parser == null){
			throw new ParserNotFoundException();
		}
		
		parser.parse(bytes);
		
	}

}
