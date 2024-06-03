package com.example.mybatisbatch.domain;

public class Computer {
	private Long id;
	private String name;

	public Computer(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Computer{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}
