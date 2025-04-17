package com.ducnh.oauth2_server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ducnh.oauth2_server.model.PolylineMap;

@Repository
public interface MapRepository extends CrudRepository<PolylineMap, String>{
}
