package com.javainuse.fanoutexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.fanoutexchange.service.RabbitMQFanoutExhangeSender;
import com.javainuse.model.Employee;

@RestController
@RequestMapping(value = "/javainuse-rabbitmq-fanout/")
public class RabbitMQWebControllerFanout {

	@Autowired
	RabbitMQFanoutExhangeSender rabbitMQSender;

	@GetMapping(value = "/producer")
	public String producer(@RequestParam("empName") String empName,@RequestParam("empId") String empId) {
	
	Employee emp=new Employee();
	emp.setEmpId(empId);
	emp.setEmpName(empName);
		rabbitMQSender.send(emp);

		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}

}

