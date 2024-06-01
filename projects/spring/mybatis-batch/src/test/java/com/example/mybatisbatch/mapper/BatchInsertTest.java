package com.example.mybatisbatch.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mybatisbatch.domain.Computer;

@SpringBootTest
@Transactional
class BatchInsertTest {

	@Autowired
	private ComputerMapper computerMapper;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	void for_문으로_삽입하는_경우() {
		List<Computer> computers = COMPUTERS_FIXTURE(1000);

		long start = System.currentTimeMillis();
		for (Computer computer : computers) {
			computerMapper.insert(computer);
		}
		long end = System.currentTimeMillis();


		List<Computer> actual = computerMapper.selectAll();
		assertThat(actual).usingRecursiveComparison().isEqualTo(computers);
		log.info("----------걸린 시간: {} (ms)", (end - start));
	}

	@Test
	void mybatic_foreach로_삽입하는_경우() {
		List<Computer> computers = COMPUTERS_FIXTURE(1000);

		long start = System.currentTimeMillis();
		computerMapper.insertBatch(computers);
		long end = System.currentTimeMillis();


		List<Computer> actual = computerMapper.selectAll();
		assertThat(actual).usingRecursiveComparison().isEqualTo(computers);
		log.info("----------걸린 시간: {} (ms)", (end - start));
	}

	private List<Computer> COMPUTERS_FIXTURE(int size) {
		List<Computer> result = new ArrayList<>();
		for (long i = 1; i <= size; i++) {
			result.add(new Computer(i, "name" + i));
		}
		return result;
	}
}