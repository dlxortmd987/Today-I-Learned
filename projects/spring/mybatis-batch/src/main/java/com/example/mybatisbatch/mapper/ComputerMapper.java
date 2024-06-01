package com.example.mybatisbatch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.mybatisbatch.domain.Computer;

@Mapper
public interface ComputerMapper {

	void insert(Computer computer);

	List<Computer> selectAll();
}
