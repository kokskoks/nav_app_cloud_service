package pl.lodz.p.ind179640.service.parser;

import org.springframework.stereotype.Service;

public interface PlanParsersDispatcher {

	void parse(byte[] bytes, String dept) throws ParserNotFoundException;

}
