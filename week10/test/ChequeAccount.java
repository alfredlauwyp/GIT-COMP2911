public class ChequeAccount extends BankAccount
{
	public int makeWithdrawal(int amount, int withdrawalType) 
	{
		// Check history for num of ChequeWithdrawals
		super.makeWithdrawal(amount, withdrawalType);
	}
}
