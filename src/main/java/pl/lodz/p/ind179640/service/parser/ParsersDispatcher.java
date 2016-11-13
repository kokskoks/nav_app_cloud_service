package pl.lodz.p.ind179640.service.parser;

import org.springframework.stereotype.Service;

public interface ParsersDispatcher {

	void parse(byte[] bytes, String dept) throws ParserNotFoundException;

}
