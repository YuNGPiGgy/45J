/******45J lab***********
//yungchc1 Yung-Chu Chuang 50926235
//tzulingw Tzu-Ling Wang   77721457
//
//
************************

 o	A class representing all the information for the Simulation. This includes:
	o	A CrapsGame object
	o	A CrapsMetricsMonitor object
	o	The user name
	o	The user balance
	o	The user bet
	o	The current win streak
	o	The current lose streak
	
o	Public methods that this class must implement are:
	o	CrapsSimulation()
		o	Constructor that initializes all fields to default values and 
			constructs any objects used (i.e. Scanner, CrapsMetricsMonitor, �)
	o	void start()
		o	Main loop of a single simulation run. This is where the user inputs 
			their name, balance, and bet, runs the simulation, and 
			continues to do so if the user wants to run it again.
 */
import java.util.Scanner;

public class CrapsSimulation
{
	private CrapsGame crapsGame;
	private CrapsMetricsMonitor crapsMetricsMonitor;
	private String userName;
	private int userBalance;
	private int userBet;
	private String replay;
	private String rebet;
	private int currentWinStreak;
	private int currentLoseStreak;
	
	private Scanner input;
	
	//Constructor
	public CrapsSimulation()
	{
		this.crapsMetricsMonitor = new CrapsMetricsMonitor();
		this.crapsGame = new CrapsGame(crapsMetricsMonitor);
		this.userName = "";
		this.userBalance = 0;
		this.userBet = 0;
		this.replay = "";
		this.rebet = "";
		this.currentWinStreak = 0;
		this.currentLoseStreak = 0;
		this.input = new Scanner(System.in);
		start();
	}
	
	//Ask for user name
	public String askName()
	{
		System.out.println("Welcome to SimCraps! Enter your user name: ");
		return input.nextLine();
	}
	
	//Ask for balance
	public int askBalance()
	{
		System.out.println("Enter the amount of money you will bring to the table: ");
		return input.nextInt();
	}
	
	//Ask for bet
	public int askBet()
	{
		System.out.println("Enter the bet amount between $1 and $" + userBalance + ": ");
		return input.nextInt();
	}
	
	//Ask for replay
	public String askReplay()
	{
		System.out.println("Play again? 'y' or 'n': ");
		return input.next();
	}
	
	public String askReBet()
	{
		System.out.println("Bet again? 'y' or 'n': ");
		return input.next();
	}
	
	//The Game Simulation that prompts the user for the name, budget, and bets
	public void start()
	{
		boolean Playing = true;
		while (Playing)
		{
			//User Information
			//Ask User Name
			userName = askName();
			try 
			{
				CrapsGame.checkName(userName);
			}
			catch(Exception e)
			{
				System.out.println("Exception occured: " + e);
				askName();
			}
			System.out.println("Hello " + userName + "!");
			//Ask User Balance
			userBalance = askBalance();
			
			try
			{
				CrapsGame.checkBalance(userBalance);
			}
			catch(Exception e)
			{
				System.out.println("Exception occured: " + e);
				askBalance();
			}
			crapsMetricsMonitor.setMaxBalance(userBalance);
			
			//Ask User Bet
			boolean keepBetting = true;
			while (keepBetting && userBalance > 0)
			{
				userBet = askBet();
				try
				{
					CrapsGame.checkBet(userBet, userBalance);
				}
				catch(Exception e)
				{
					System.out.println("Exception occured: " + e);
					askBet();
				}
				
				try
				{
					CrapsGame.checkNegativeBet(userBet);
				}
				catch(Exception e)
				{
					System.out.println("Exception occured: " + e);
					askBet();
				}
				System.out.println(userName + " bets $" + userBet);
				
				//Start Game
				boolean winGame = crapsGame.playGame();
				if (winGame)
				{
					crapsMetricsMonitor.updateGamesWon();
					currentWinStreak++;
					currentLoseStreak = 0;
					userBalance += userBet;
					
					if(currentWinStreak > crapsMetricsMonitor.getMaxWin())
					{
						crapsMetricsMonitor.setMaxWin(currentWinStreak);
					}
				}
				else
				{
					crapsMetricsMonitor.updateGamesLost();
					currentWinStreak = 0;
					currentLoseStreak++;
					userBalance -= userBet;
					
					if(currentLoseStreak > crapsMetricsMonitor.getMaxLose())
					{
						crapsMetricsMonitor.setMaxLose(currentLoseStreak);
					}
				}
				System.out.println(userName + "'s balance:" +userBalance);
				if (crapsMetricsMonitor.getMaxBalance() < userBalance)
				{
					crapsMetricsMonitor.setMaxBalance(userBalance);
					crapsMetricsMonitor.setBestGame(crapsMetricsMonitor.getGamesPlayed());
				}
				if (userBalance > 0)
				{
					rebet = askReBet();
					try
					{
						CrapsGame.checkAnswer(rebet);
					}
					catch(Exception e)
					{
						System.out.println("Exception occured: " + e);
						askReBet();
					}
					
					if (rebet.equalsIgnoreCase("y"))
					{
						continue;
					}
					else
					{
						keepBetting = false;
					}
				}
				else
				{
					keepBetting = false;
				}
			}
			//Ask for replay or quit
			crapsMetricsMonitor.printStatistics();
			replay = askReplay();
			try
			{
				CrapsGame.checkAnswer(replay);
			}
			catch(Exception e)
			{
				System.out.println("Exception occured: " + e);
				askReplay();
			}
			if (replay.equalsIgnoreCase("y"))
			{
				crapsMetricsMonitor.reset();
			}
			else
			{
				System.exit(0);
			}
		}
	}
}