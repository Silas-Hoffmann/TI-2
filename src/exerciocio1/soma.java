package exerciocio1;
import java.util.Scanner;
// alteracao
class soma {
	public static void main (String args[]) {
		Scanner sc = new Scanner(System.in);
		
		int num1 = sc.nextInt();
		int num2 = sc.nextInt();
		int soma = num1+num2;
		
		System.out.println(num1 + " + " + num2 + " = " + soma);
		
		sc.close();
	}
}
