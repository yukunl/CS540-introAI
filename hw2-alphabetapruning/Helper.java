
/*
 * Name: Yukun Li
 * CS540 2019Fall P2 
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */
public class Helper {

	/**
	 * Class constructor.
	 */
	private Helper() {
	}

	/**
	 * This method is used to check if a number is prime or not
	 * 
	 * @param x A positive integer number
	 * @return boolean True if x is prime; Otherwise, false
	 */
	public static boolean isPrime(int x) {

		for (int i = 2; i < x; i++) {
			if (x % i == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * This method is used to get the largest prime factor
	 * 
	 * @param x A positive integer number
	 * @return int The largest prime factor of x
	 */
	public static int getLargestPrimeFactor(int x) {

		if (isPrime(x)) {
			return x;
		}
		int largestPrime = 1;
		for (int i = 2; i < x; i++) {
			if (x % i == 0) {
				if (isPrime(i)) {
					largestPrime = i;
				}
			}
		}return largestPrime;
	}
}