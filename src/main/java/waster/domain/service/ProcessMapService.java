package waster.domain.service;

import waster.domain.entity.ProcessMap;

import java.util.List;

public interface ProcessMapService {

     List<Long> getShortestPath(ProcessMap processMap);
}
