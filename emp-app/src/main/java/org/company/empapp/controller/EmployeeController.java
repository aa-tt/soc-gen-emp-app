package org.company.empapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.company.empapp.business.EmployeeService;
import org.company.empapp.common.AgeLimitHRValid;
import org.company.empapp.model.Employee;

@RestController
@RequestMapping("/v1")
@Validated
public class EmployeeController {

	public EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping(value = "/employees", produces = { "application/json" })
	@AgeLimitHRValid(message="HR minimum age should be 30")
	public ResponseEntity<?> getAllEmployees(@RequestParam(name = "dept", required = false) String dept,
			@RequestParam(name = "age", required = false) Integer age) {
		List<Employee> employees;
		if (null != dept) {
			if (null != age)
				employees = employeeService.getEmployeesFilteredByDeptAndAge(dept, age);
			else
				employees = employeeService.getEmployeesFilteredByDept(dept);
		} else
			employees = employeeService.getAllEmployees();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@GetMapping(value = "/employees/{empId}", produces = { "application/json" })
	public ResponseEntity<Employee> getEmployee(@PathVariable(value = "empId") Integer id) {
		Employee employee = employeeService.getEmployeeById(id);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@DeleteMapping(value = "/employees/{id}", produces = { "application/json" })
	public ResponseEntity<Employee> deleteEmployee(@PathVariable(name = "id") Integer id) {
		Employee employee = employeeService.deleteById(id);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/employees", consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<List<Employee>> addEmployee(@RequestBody @Valid List<Employee> employees) {
		List<Employee> employeeSaved = employeeService.addEmployee(employees);
		return new ResponseEntity<List<Employee>>(employeeSaved, HttpStatus.OK);
	}

}
