// 
// File: ahnentafel.c 
//
// @author Denis Zhenilov <dxz6228>
// // // // // // // // // // // // // // // // // // // // // // // 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

/**
 * A function that was initially to be used to calculate the number of generations
 * between the "self" and the relative identified by a number. Now it is used
 * to estimate by how many bytes a string for binary number has to be "padded"
 * to accept the complete number.
 * Generations calculations are performed inside the processing functions.
 *
 * param base10 the ahnentafel number in base 10
 */
int generationsCounter(int base10){
	int gens;
	gens=0;
	while(base10>1){
		if(base10 % 2 == 0){
			gens++;
			base10 = base10 / 2;
		}
		if(base10 % 2 != 0){
			gens++;
			base10 = base10-1;
			base10 = base10 / 2;
		}
	}
	return gens;
}

/**
 * A simple function the sole purpose of which is to be called in special case,
 * when ahnentafel number is 1, or "self" was entered as relationship.
 * It then proceeds to print the correct format of the output for those cases.
 * 
 * param opt an integer representing the "option" from which printSelf was
 * called, telling the function which format to use.
 */
void printSelf(int opt){
	if(opt==2){
		printf("ahnentafel number in binary: 1\n");
		printf("self\n");
		printf("generations back: 0\n");
	}
	if(opt==3){
		printf("base-10 value: 1\n");
		printf("self\n");
		printf("generations back: 0\n");
	}
	if(opt==4){
		printf("ahnentafel number in binary: 1\n");
		printf("ahnentafel number in base-10: 1\n");
		printf("generations back: 0\n");
	}
}

/**
 * A function to process the base 10 ahnentafel number. It starts by
 * creating a string representation of corresponding base 2 number -
 * albeit reversed because of the conversion method. Methods for
 * printing the base 2 number account for the reversal.
 * It then uses the string of base 2 number to create a relationship
 * chain and, finally, calcualte the number of generations.
 *
 * param b10 base-10 ahnentafel number
 */
void base10process(int b10){
	if(b10==1){
		printSelf(2);
	}
	else{
		int gens;
		char binary[generationsCounter(b10)+2];
		int b2conv;
		b2conv=b10;
		int builder;
		builder=0;
		while(b2conv>0){
			int remainder;
			char remainC;
			remainder=b2conv % 2;
			if(remainder==0){
				remainC='0';
			}
			if(remainder==1){
				remainC='1';
			}
			binary[builder]=remainC;
			b2conv = b2conv / 2;
			builder++;
		}
		binary[builder]='\0';
		gens = strlen(binary)-1;
		printf("ahnentafel number in binary: ");
		int backwardsCount;
		backwardsCount=strlen(binary)-1;
		while(backwardsCount>=0){
			printf("%c", binary[backwardsCount]);
			backwardsCount--;
		}
		printf("\nfamily relation: ");
		backwardsCount=strlen(binary)-2;
		while(backwardsCount>=0){
			if(binary[backwardsCount]=='0'){
				printf("father");
			}
			if(binary[backwardsCount]=='1'){
				printf("mother");
			}
			if(backwardsCount!=0){
				printf("\'s ");
			}
			backwardsCount--;
		}
		printf("\ngenerations back: %d\n", gens);
	}
}

/**
 * A function to process a string representation of base 2 ahnentafel number,
 * it calculates the integer representing the corresponding base-10 number
 * and uses base-2 string to create a relationship chain and calculate
 * generations.
 *
 * param binary a base-2 ahnentafel number as a string
 */
void base2process(char binary[]){
	int decimal;
	decimal=0;
	int power;
	power = strlen(binary)-1;
	unsigned int i;
	i=0;
	while(i<strlen(binary)){
		if(binary[i]=='1'){
			int pos;
			pos=0;
			int powCalc;
			powCalc = power-i;
			if(powCalc==0){
				pos=1;
			}
			if(powCalc!=0){
				pos=2;
				powCalc=powCalc-1;
				while(powCalc>0){
					pos=pos*2;
					powCalc--;
				}
			}
			decimal+=pos;
		}
		i++;
	}
	if(decimal==1){
		printSelf(3);
	}
	else{
		printf("base-10 value: %d\n", decimal);
		printf("family relation: ");
		unsigned int shipCounter;
		shipCounter=1;
		while(shipCounter<strlen(binary)){
			if(binary[shipCounter]=='0'){
				printf("father");
			}
			if(binary[shipCounter]=='1'){
				printf("mother");
			}
			if(shipCounter!=strlen(binary)-1){
				printf("\'s ");
			}
			shipCounter++;
		}
		printf("\ngenerations back: %d\n", (int) strlen(binary)-1);
	}
}

/**
 * A function that processes the relationship chain in form of a string.
 * It splits the relationships chain into tokens and parses them to create
 * a base-2 ahnentafel number string and calculate the base-10 number and
 * generations.
 *
 * param relation a relationships chain as a string
 */
void relationProcess(char relation[]){
	if(relation[0]=='s'){
		printSelf(4);
		return;
	}
	char binary[(int) (((strlen(relation)+3)/8)+2)];
	binary[0]='1';
	binary[1]='\0';
	int decimal;
	decimal = 1;
	int gens;
	gens=0;
	char* token;
	token = strtok(relation, " ");
	while(token!=NULL){
		if(token[0]=='f'){
			strcat(binary, "0");
			decimal=decimal*2;
		}
		if(token[0]=='m'){
			strcat(binary, "1");
			decimal=decimal*2;
			decimal=decimal+1;
		}
		gens++;
		token = strtok(NULL, " ");
	}
	printf("ahnentafel number in binary: %s\n", binary);
	printf("ahnentafel number in base-10: %d\n", decimal);
	printf("generations back: %d\n", gens);	
}

/**
 * The main function. If the user provides no command line arguments, the
 * function enters an endless loop, displaying textual "menu" and prompting
 * user for input. The user can quit the function from the menu.
 * If the user does supply command line arguments, the function parses them
 * the same way it would've parsed arguments in one of its three processing
 * modes, depending on what those arguments are. If a number is supplied, only
 * the first argument is relevant and looked at. If argument is not numerical,
 * it is assumed to be a relationship string and only in that case will more
 * than one argument be looked at.
 */
int main (int argc, char *argv[]){
	int ahn10;
	char relation[901];
	if(argc==1){
		while (1){
			int input;
			printf("\n1) description\n");
			printf("2) ahnentafel number (base 10)\n");
			printf("3) ahnentafel number (base 2)\n");
			printf("4) relation (e.g. mother's mother's father)\n");
			printf("5) exit\n\n> ");
			scanf("%d", &input);
			if(input==1){
				printf("The Ahnentafel number is used to determine the relation\n");
				printf("between an individual and each of his/her direct ancestors.\n");
			}
			if(input==2){
				char decimalString[81];
				printf("Enter the ahnentafel number in base-10: ");
				scanf("%s", decimalString);
				char *ptrDec;
				ahn10 = (int) strtol(decimalString, &ptrDec, 10);
				base10process(ahn10);
			}
			if(input==3){
				printf("Enter the ahnentafel number in binary: ");
				int valid;
				valid=1;
				char binary[901];
				scanf("%s", binary);
				for(unsigned int i=0; i<strlen(binary); i++){
					if(binary[i]!='1' && binary[i]!='0'){
						fprintf(stderr, "Invalid string!\n");
						valid=0;
						break;
					}
				}
				if(valid==1){
					base2process(binary);
				}
			}
			if(input==4){
				printf("Enter family relation (e.g.) \"father's mother\": ");
				int c;
				while ((c = getchar()) != '\n' && c != EOF);
				fgets(relation, 901, stdin);
				relationProcess(relation);
			}
			if(input==5){
				printf("Goodbye.\n");
				return EXIT_SUCCESS;
			}
			if(input>5 || input<1){
				fprintf(stderr, "Unknown operation!\n");
			}
		}
	}
	if(argc>1){
		if(isdigit(argv[1][0])){
			if(argv[1][strlen(argv[1])-1]=='b'){
				char binaryNoTrail[strlen(argv[1])];
				strncpy(binaryNoTrail, argv[1], strlen(argv[1])-1);
				unsigned int i;
				i=0;
				while(i<strlen(binaryNoTrail)){
					if(binaryNoTrail[i]!='0' && binaryNoTrail[i]!='1'){
						fprintf(stderr, "Invalid string!\n");
						return EXIT_FAILURE;
					}
					i++;
				}
				base2process(binaryNoTrail);
				return EXIT_SUCCESS;
			}
			if(argv[1][strlen(argv[1])-1]!='b'){
				int dec;
				char *ptr;
				dec = (int) strtol(argv[1], &ptr, 10);
				base10process(dec);
				return EXIT_SUCCESS;
			}
		}
		if(!isdigit(argv[1][0])){
			char relationComLine[(5*(argc-1))+(3*(argc-2))+1];
			int relCounter;
			relCounter=1;
			strncpy(relationComLine, argv[relCounter], strlen(argv[relCounter]));
			relCounter++;
			while(relCounter<argc){
				strcat(relationComLine, " ");
				strcat(relationComLine, argv[relCounter]);
				relCounter++;
			}
			relationProcess(relationComLine);
			return EXIT_SUCCESS;
		}
	}
}
