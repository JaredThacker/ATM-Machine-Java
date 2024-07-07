import java.io.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account {

	private int customerNumber;
	private int pinNumber;
	private double checkingBalance = 0;
	private double savingBalance = 0;
	private final String accountsPath = "accounts/";
	private final String logsPath = "logs/";
	private enum TransactionType{
		DEPOSIT,
		WITHDRAW,
		TRANSFER,
	}
	private enum AccountType{
		CHECKING,
		SAVINGS,
	}

	Scanner input = new Scanner(System.in);
	DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");

	public Account() {
	}

	public Account(int customerNumber, int pinNumber) {
		this.customerNumber = customerNumber;
		this.pinNumber = pinNumber;
		this.saveAccount(this.accountsPath);
	}

	public Account(int customerNumber, int pinNumber, double checkingBalance, double savingBalance) {
		this.customerNumber = customerNumber;
		this.pinNumber = pinNumber;
		this.checkingBalance = checkingBalance;
		this.savingBalance = savingBalance;
		this.saveAccount(this.accountsPath);
	}

	public int setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
		return customerNumber;
	}

	public int getCustomerNumber() {
		return customerNumber;
	}

	public int setPinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
		return pinNumber;
	}

	public int getPinNumber() {
		return pinNumber;
	}

	public double getCheckingBalance() {
		return checkingBalance;
	}

	public double getSavingBalance() {
		return savingBalance;
	}

	public double calcCheckingWithdraw(double amount) {
		checkingBalance = (checkingBalance - amount);
		this.saveAccount(this.accountsPath);
		logTransaction(logsPath, TransactionType.WITHDRAW, AccountType.CHECKING, amount);
		return checkingBalance;
	}

	public double calcSavingWithdraw(double amount) {
		savingBalance = (savingBalance - amount);
		this.saveAccount(this.accountsPath);
		logTransaction(logsPath, TransactionType.WITHDRAW, AccountType.SAVINGS, amount);
		return savingBalance;
	}

	public double calcCheckingDeposit(double amount) {
		checkingBalance = (checkingBalance + amount);
		this.saveAccount(this.accountsPath);
		logTransaction(logsPath, TransactionType.DEPOSIT, AccountType.CHECKING, amount);
		return checkingBalance;
	}

	public double calcSavingDeposit(double amount) {
		savingBalance = (savingBalance + amount);
		this.saveAccount(this.accountsPath);
		logTransaction(logsPath, TransactionType.DEPOSIT, AccountType.SAVINGS, amount);
		return savingBalance;
	}

	public void calcCheckTransfer(double amount) {
		checkingBalance = checkingBalance - amount;
		savingBalance = savingBalance + amount;
		this.saveAccount(this.accountsPath);
		logTransaction(logsPath, TransactionType.TRANSFER, AccountType.CHECKING, amount);
	}

	public void calcSavingTransfer(double amount) {
		savingBalance = savingBalance - amount;
		checkingBalance = checkingBalance + amount;
		this.saveAccount(this.accountsPath);
		logTransaction(logsPath, TransactionType.TRANSFER, AccountType.SAVINGS, amount);
	}

	public void getCheckingWithdrawInput() {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nCurrent Checking Account Balance: " + moneyFormat.format(checkingBalance));
				System.out.print("\nAmount you want to withdraw from Checking Account: ");
				double amount = input.nextDouble();
				if ((checkingBalance - amount) >= 0 && amount >= 0) {
					calcCheckingWithdraw(amount);
					System.out.println("\nCurrent Checking Account Balance: " + moneyFormat.format(checkingBalance));
					end = true;
				} else {
					System.out.println("\nBalance Cannot be Negative.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				input.next();
			}
		}
	}

	public void getSavingWithdrawInput() {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(savingBalance));
				System.out.print("\nAmount you want to withdraw from Savings Account: ");
				double amount = input.nextDouble();
				if ((savingBalance - amount) >= 0 && amount >= 0) {
					calcSavingWithdraw(amount);
					System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(savingBalance));
					end = true;
				} else {
					System.out.println("\nBalance Cannot Be Negative.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				input.next();
			}
		}
	}

	public void getCheckingDepositInput() {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nCurrent Checking Account Balance: " + moneyFormat.format(checkingBalance));
				System.out.print("\nAmount you want to deposit into Checking Account: ");
				double amount = input.nextDouble();
				if ((checkingBalance + amount) >= 0 && amount >= 0) {
					calcCheckingDeposit(amount);
					System.out.println("\nCurrent Checking Account Balance: " + moneyFormat.format(checkingBalance));
					end = true;
				} else {
					System.out.println("\nBalance Cannot Be Negative.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				input.next();
			}
		}
	}

	public void getSavingDepositInput() {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(savingBalance));
				System.out.print("\nAmount you want to deposit into your Savings Account: ");
				double amount = input.nextDouble();

				if ((savingBalance + amount) >= 0 && amount >= 0) {
					calcSavingDeposit(amount);
					System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(savingBalance));
					end = true;
				} else {
					System.out.println("\nBalance Cannot Be Negative.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				input.next();
			}
		}
	}

	public void getTransferInput(String accType) {
		boolean end = false;
		while (!end) {
			try {
				if (accType.equals("Checking")) {
					System.out.println("\nSelect an account you wish to transfer funds to:");
					System.out.println("1. Savings");
					System.out.println("2. Exit");
					System.out.print("\nChoice: ");
					int choice = input.nextInt();
					switch (choice) {
					case 1:
						System.out.println("\nCurrent Checking Account Balance: " + moneyFormat.format(checkingBalance));
						System.out.print("\nAmount you want to deposit into your Savings Account: ");
						double amount = input.nextDouble();
						if ((savingBalance + amount) >= 0 && (checkingBalance - amount) >= 0 && amount >= 0) {
							calcCheckTransfer(amount);
							System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(savingBalance));
							System.out.println(
									"\nCurrent Checking Account Balance: " + moneyFormat.format(checkingBalance));
							end = true;
						} else {
							System.out.println("\nBalance Cannot Be Negative.");
						}
						break;
					case 2:
						return;
					default:
						System.out.println("\nInvalid Choice.");
						break;
					}
				} else if (accType.equals("Savings")) {
					System.out.println("\nSelect an account you wish to transfer funds to: ");
					System.out.println("1. Checking");
					System.out.println("2. Exit");
					System.out.print("\nChoice: ");
					int choice = input.nextInt();
					switch (choice) {
					case 1:
						System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(savingBalance));
						System.out.print("\nAmount you want to deposit into your savings account: ");
						double amount = input.nextDouble();
						if ((checkingBalance + amount) >= 0 && (savingBalance - amount) >= 0 && amount >= 0) {
							calcSavingTransfer(amount);
							System.out.println("\nCurrent checking account balance: " + moneyFormat.format(checkingBalance));
							System.out.println("\nCurrent savings account balance: " + moneyFormat.format(savingBalance));
							end = true;
						} else {
							System.out.println("\nBalance Cannot Be Negative.");
						}
						break;
					case 2:
						return;
					default:
						System.out.println("\nInvalid Choice.");
						break;
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				input.next();
			}
		}
	}

	public void showBalances() {
		System.out.printf("\n-------------------\nSavings: %.2f\nChecking: %.2f\n-------------------\n", this.savingBalance, this.checkingBalance);
	}

	public void saveAccount(String filePath) {
		String formattedFilePath = String.format("%saccount_%d.txt", filePath, this.getCustomerNumber());
		try {
			File file = new File(formattedFilePath);
			FileWriter writer = new FileWriter(file, false);
			writer.write(String.format("%.2f, %.2f, %d, %d", this.checkingBalance, this.savingBalance, this.customerNumber, this.pinNumber));
			writer.close();
		} catch (IOException exception) {
			System.out.println("\nFailed to save account");
			System.out.println(exception.getMessage());
		}
	}

	public void logTransaction(String filePath, TransactionType transactionType, AccountType accountType, double amount) {
		String formattedFilePath = String.format("%saccount_%d.txt", filePath, this.getCustomerNumber());
		try {
			File file = new File(formattedFilePath);
			FileWriter writer = new FileWriter(file, true);
			String Timestamp = new Timestamp(System.currentTimeMillis()).toString();
			String logTransactionType = transactionType == TransactionType.WITHDRAW ? "Withdraw" : transactionType == TransactionType.DEPOSIT ? "Deposit" : "Transfer";
			String logAccountType = accountType == AccountType.SAVINGS ? "Savings" : "Checking";
			writer.write(String.format(Timestamp + ": %s, %s, %.2f \n", logTransactionType, logAccountType, amount));
			writer.close();
		} catch (IOException exception) {
			System.out.println("\nFailed to log transaction");
			System.out.println(exception.getMessage());
		}
	}

	public void viewTransactionLogs() {
		String formattedFilePath = String.format("%saccount_%d.txt", logsPath, this.getCustomerNumber());
		try {
			BufferedReader br = new BufferedReader(new FileReader(formattedFilePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			sb.append(System.lineSeparator());
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String wholeFile = sb.toString();
			System.out.println(wholeFile);
		} catch (IOException exception) {
			System.out.println("\nNo transactions to list");
		}
	}
}
