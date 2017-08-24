package com.imcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.imcs.bonus.pojo.Bonus;
import com.imcs.bonus.pojo.Employee;
import com.imcs.bonus.service.BonusService;
import com.imcs.bonus.service.BonusServiceImpl;
import com.imcs.bonus.service.EmployeeBonusService;
import com.imcs.bonus.service.EmployeeBonusServiceImpl;
import com.imcs.bonus.service.EmployeeService;
import com.imcs.bonus.service.EmployeeServiceImpl;
import org.apache.log4j.Logger;

public class Client {

	BonusService bonusService = new BonusServiceImpl();
	EmployeeBonusService employeeBService = new EmployeeBonusServiceImpl();
	EmployeeService employeeService = new EmployeeServiceImpl();
	final static Logger log = Logger.getLogger(Client.class);
	final String bonusPath = "..\\BonusCalculationAPP\\Resources\\Bonus.txt";
	final String empDataPath = "..\\BonusCalculationAPP\\Resources\\EmployeeData.txt";
	private void loadData() {
		log.info("LOADING DATA FROM FILE STARTS...");
		List<Bonus> bonusList = new ArrayList<>();
		List<Employee> employeeList = new ArrayList<>();
		File bonusData = new File(bonusPath);
		File employeeData = new File(empDataPath);
		try (BufferedReader empReader = new BufferedReader(new FileReader(employeeData))) {
			if (employeeData.exists()) {

				String line = null;
				boolean keepReading = true;
				int index = 0;
				while (keepReading) {
					line = empReader.readLine();
					if (line == null || line.equals("")) {
						break;
					}
					if (index != 0) {
						Employee emp = parseEmployeeLine(line);
						employeeList.add(emp);
					}
					index++;
				}
				employeeService.loadEmployee(employeeList);
			}
			log.info("SUCCESSFULLY LOADED BONUS FILE  ");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		try (BufferedReader bonusReader = new BufferedReader(new FileReader(bonusData))) {
			if (bonusData.exists()) {

				String line = null;
				boolean keepReading = true;
				int index = 0;
				while (keepReading) {
					line = bonusReader.readLine();
					if (line == null || line.equals("")) {
						break;
					}
					if (index != 0) {
						Bonus b = parseBonusLine(line);
						bonusList.add(b);
					}
					index++;
				}
				bonusService.loadBonus(bonusList);
			}
			log.info("SUCCESSFULLY LOADED BONUS FILE  ");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	private Employee parseEmployeeLine(String line) throws NumberFormatException, ParseException{
		String[] tokens = line.split("\\s+");
		return new Employee(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
				new SimpleDateFormat("yyyy-MM-dd").parse(tokens[2]),
				new SimpleDateFormat("yyyy-MM-dd").parse(tokens[3]), Integer.parseInt(tokens[4]),
				Integer.parseInt(tokens[5]));
	}
	private Bonus parseBonusLine(String line) {
		String[] tokens = line.split("\\s+");
		Bonus bonus = new Bonus(Integer.parseInt(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[1]));
		return bonus;
	}

	public static void main(String[] args) {		
		log.info("APPLICATION STARTED");
		Client client = new Client();
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.println("CHOOSE AN OPTION\n1. Load Bonus from File\n2.Allocate Bonus\n3.QUITT FROM APP");
				Integer choice = Integer.valueOf(sc.nextLine());
				switch (choice) {
				case 1:
					client.loadData();
					break;
				case 2:
					client.employeeBService.allocateBonus();
					log.info("Application Ended");
					break;
				case 3:
					log.info("Application Ended");
					System.exit(0);
					break;
				default:
					System.out.println("Enter a valid choice");
					break;
				}
			}
		}
	}

}
