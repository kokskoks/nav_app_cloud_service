package pl.lodz.p.ind179640.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlanParserService {

    private final Logger log = LoggerFactory.getLogger(PlanParserService.class);

}
