package ie.szymon.services;

import ie.szymon.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeServiceImpl implements OfficeService {
    @Autowired
    OfficeRepo officeRepo;
}
